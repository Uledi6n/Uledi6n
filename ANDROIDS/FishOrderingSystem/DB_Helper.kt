package com.my.tuto

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// Data classes
data class UserData(
    val id: Int,
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
)

data class Orders(
    val id: Int,
    val fisher_email: String,
    val username: String,
    val email: String,
    val location: String,
    val phone_number: String,
    val order_detail: String,
)

class DB_Helper(context: Context) :
    SQLiteOpenHelper(context, "FisherDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE fisherTB(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                email TEXT UNIQUE,
                phone TEXT UNIQUE,
                password TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE Order_tb(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fisher_email TEXT,
                username TEXT,
                email TEXT UNIQUE NOT NULL,
                location TEXT,
                phone_number TEXT UNIQUE,
                order_detail TEXT
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS fisherTB")
        db.execSQL("DROP TABLE IF EXISTS Order_tb")
        onCreate(db)
    }

    fun userExists(email: String, phone: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM fisherTB WHERE email=? AND phone=?",
            arrayOf(email, phone)
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }

    fun insertFisher(username: String, email: String, phone: String, password: String) {
        val db = writableDatabase
        val value = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("phone", phone)
            put("password", password)
        }
        db.insert("fisherTB", null, value)
        db.close()
    }

    fun insertBuyer(username: String, email: String, phone: String, password: String) {
        val db = writableDatabase
        val value = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("phone_number", phone)
            put("password", password)
        }
        db.insert("buyer_tb", null, value)
        db.close()
    }

    fun insertOrder(fisher_email: String, username: String, email: String, location: String, phone_number: String, order_detail: String) {
        val db = writableDatabase
        val value = ContentValues().apply {
            put("fisher_email", fisher_email)
            put("username", username)
            put("email", email)
            put("location", location)
            put("phone_number", phone_number)
            put("order_detail", order_detail)
        }
        db.insert("Order_tb", null, value)
        db.close()
    }

    fun getAllOrders(): List<Orders> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM Order_tb", null)
        val orders = mutableListOf<Orders>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val fisher_email = cursor.getString(cursor.getColumnIndexOrThrow("fisher_email"))
                val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val location = cursor.getString(cursor.getColumnIndexOrThrow("location"))
                val phone_number = cursor.getString(cursor.getColumnIndexOrThrow("phone_number"))
                val order_detail = cursor.getString(cursor.getColumnIndexOrThrow("order_detail"))

                orders.add(Orders(id, fisher_email, username, email, location, phone_number, order_detail))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return orders
    }

    fun getAllFishers(): List<UserData> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM fisherTB", null)
        val users = mutableListOf<UserData>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))

                users.add(UserData(id, username, email, phone, password))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }

    fun deleteOrder(orderId: Int): Boolean {
        val db = writableDatabase
        val affectedRows = db.delete("Order_tb", "id = ?", arrayOf(orderId.toString()))
        db.close()
        return affectedRows > 0
    }

    fun updateOrderDetails(orderId: Int, newLocation: String, newOrderDetail: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("location", newLocation)
            put("order_detail", newOrderDetail)
        }
        val result = db.update("Order_tb", values, "id=?", arrayOf(orderId.toString()))
        db.close()
        return result > 0
    }

    fun updateFisherDetails(email: String, newUsername: String, newPhone: String, newPassword: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", newUsername)
            put("phone", newPhone)
            put("password", newPassword)
        }
        val result = db.update("fisherTB", values, "email=?", arrayOf(email))
        db.close()
        return result > 0
    }

    fun uploadAllDataToServer(context: Context) {
        val allFishers = getAllFishers()
        val allOrders = getAllOrders()

        val fishersJsonArray = JSONArray().apply {
            allFishers.forEach {
                put(JSONObject().apply {
                    put("username", it.username)
                    put("email", it.email)
                    put("phone", it.phone)
                    put("password", it.password)
                })
            }
        }

        val ordersJsonArray = JSONArray().apply {
            allOrders.forEach {
                put(JSONObject().apply {
                    put("fisher_email", it.fisher_email)
                    put("username", it.username)
                    put("email", it.email)
                    put("location", it.location)
                    put("phone_number", it.phone_number)
                    put("order_detail", it.order_detail)
                })
            }
        }

        // Upload Fishermen
        Thread {
            try {
                val url = URL("https://yourdomain.com/api/upload-fishermen/")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.outputStream.use {
                    it.write(fishersJsonArray.toString().toByteArray())
                }

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("UPLOAD", "Fishermen uploaded successfully")
                } else {
                    Log.e("UPLOAD", "Fishermen upload failed: ${conn.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()

        // Upload Orders
        Thread {
            try {
                val url = URL("https://yourdomain.com/api/upload-orders/")
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json")
                conn.doOutput = true
                conn.outputStream.use {
                    it.write(ordersJsonArray.toString().toByteArray())
                }

                if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("UPLOAD", "Orders uploaded successfully")
                } else {
                    Log.e("UPLOAD", "Orders upload failed: ${conn.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
