
package com.my.tuto

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.telephony.SmsManager

import android.os.Bundle
import android.graphics.BitmapFactory
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.launch
import android.view.*
import androidx.core.view.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set dark green status bar background
        window.statusBarColor = android.graphics.Color.rgb(0, 77, 64)
        // Use light content (white icons) on the dark status bar
        WindowCompat.getInsetsController(window, window.decorView)
            ?.isAppearanceLightStatusBars = false

        setContent {
            FishOrderingSystemApp()
        }
    }
}


@Composable
fun DrawerContent(onItemClick: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.fish),
                    contentDescription = "Fish Logo",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = "Pages",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Divider(thickness = 2.dp, color = Color.Gray)
        }

        listOf(
		    "Home" to Icons.Default.Home,
            "Register Fisherman" to Icons.Default.AccountCircle,
            "Fisherman Update" to Icons.Default.Lock,
            "Order Update" to Icons.Default.Lock,
            "Place Order" to Icons.Default.ShoppingCart,
			"Orders Made" to Icons.Default.Info
        ).forEach { (label, icon) ->
            item {
                DrawerItemRow(label, icon) {
                    onItemClick(label)
                }
            }
        }
    }
}


@Composable
fun FishOrderingSystemApp() {
    val scaffoldState = rememberScaffoldState()
    val drawerState = scaffoldState.drawerState
    val coroutineScope = rememberCoroutineScope()

    var selectedScreen by remember { mutableStateOf("Home") }
    var selectedEmail by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val window = (context as Activity).window
        window.statusBarColor = Color(0xFF004D40).toArgb() // Teal dark
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent {
                selectedScreen = it
                coroutineScope.launch { drawerState.close() }
            }
        }
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                CustomTopAppBar {
                    coroutineScope.launch { drawerState.open() }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Replace with actual screens
                when (selectedScreen) {
					"Home"->HomeScreen(
					    onEmailSelected = {
                            selectedEmail = it
                            selectedScreen = "Place Order"
                        },
                        dbHelper = DB_Helper(context)
					)
                    "Register Fisherman" -> SignInScreen()
                    "Fisherman Update" -> FisherUpdate()
                    "Order Update" -> OrderUpdate()
                    "Place Order" -> FisherDetails(email = selectedEmail, dbHelper = DB_Helper(context))
					"Orders Made" -> Orders()
                    else -> HomeScreen(
                        onEmailSelected = {
                            selectedEmail = it
                            selectedScreen = "Place Order"
                        },
                        dbHelper = DB_Helper(context)
                    )
                }
            }
        }
    }
}


@Composable
fun CustomTopAppBar(onMenuClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF00695C), Color(0xFF004D40))
                )
            )
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Fish Ordering System",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                }
            }
        )
    }
}


@Composable
fun DrawerItemRow(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF00695C)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}


@Composable
fun HomeScreen(onEmailSelected: (String) -> Unit, dbHelper: DB_Helper) {
    val context = LocalContext.current
    val db = remember { dbHelper }

    var fisherList by remember { mutableStateOf(listOf<UserData>()) }

    LaunchedEffect(Unit) {
        fisherList = db.getAllFishers()
    }

    if (fisherList.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text("No fishers found", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Fisher count: 0")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            items(fisherList) { fisher ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header
                        Text(
                            text = "Fisherman: ${fisher.username}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00695C)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User Icon",
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(end = 16.dp),
                                tint = Color.Gray
                            )
                            Column {
                                Text("Phone: ${fisher.phone}", fontSize = 14.sp)
                                Text("Email: ${fisher.email}", fontSize = 14.sp)
                                Text("Other Descriptions", fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Register order
                        Button(
                            onClick = { onEmailSelected(fisher.email) },
                            shape = RoundedCornerShape(25.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF00796B),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Place Order")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Divider()

                        // Action Row
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            ActionIcon("Call", Icons.Default.Phone) {
                                val intent = Intent(Intent.ACTION_CALL).apply {
                                    data = Uri.parse("tel:${fisher.phone}")
                                }
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    context.startActivity(intent)
                                } else {
                                    ActivityCompat.requestPermissions(
                                        context as Activity,
                                        arrayOf(Manifest.permission.CALL_PHONE),
                                        1
                                    )
                                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                                }
                            }

                            ActionIcon("Email", Icons.Default.Email) {
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:${fisher.email}")
                                    putExtra(Intent.EXTRA_SUBJECT, "Business Inquiry")
                                    putExtra(Intent.EXTRA_TEXT, "Hello ${fisher.username},")
                                }
                                context.startActivity(emailIntent)
                            }

                            ActionIcon("Message", Icons.Default.Send) {
                                val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("smsto:${fisher.phone}")
                                }
                                context.startActivity(smsIntent)
                            }

                            ActionIcon("Share", Icons.Default.Share) {
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Check out this fisherman: ${fisher.username}")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActionIcon(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(24.dp), tint = Color.DarkGray)
        Spacer(modifier = Modifier.height(2.dp))
        Text(label, fontSize = 12.sp)
    }
}


@Composable
fun SignInScreen() {
    val context = LocalContext.current
    val dbHelper = remember { DB_Helper(context) }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Main container
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F2F1)) // light green background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // allows vertical scrolling
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                backgroundColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Fish Image (Logo)
                    Image(
                        painter = painterResource(id = R.drawable.fish), // make sure fish.png exists
                        contentDescription = "Fish Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Register Fisherman",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF004D40),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This page is only for fishermen. To advertise your business well, please enter your details correctly.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Username Field
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Fisher Username") },
                        leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                        shape = RoundedCornerShape(30.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        shape = RoundedCornerShape(30.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Phone Field
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        shape = RoundedCornerShape(30.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        shape = RoundedCornerShape(30.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Register Button
                    Button(
                        onClick = {
                            val exist = dbHelper.userExists(email, phone)
                            when {
                                username.isBlank() -> Toast.makeText(context, "Enter Username", Toast.LENGTH_SHORT).show()
                                email.isBlank() -> Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show()
                                phone.isBlank() -> Toast.makeText(context, "Enter Phone Number", Toast.LENGTH_SHORT).show()
                                password.isBlank() -> Toast.makeText(context, "Enter Password", Toast.LENGTH_SHORT).show()
                                exist -> Toast.makeText(context, "Fisherman already exists", Toast.LENGTH_SHORT).show()
                                else -> {
                                    dbHelper.insertFisher(username, email, phone, password)
                                    username = ""
                                    email = ""
                                    phone = ""
                                    password = ""
                                    Toast.makeText(context, "Successfully Registered", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF00796B),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Register Now", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // extra space at bottom
        }
    }
}

@Composable
fun FisherUpdate() {
    val context = LocalContext.current
    val db = remember { DB_Helper(context) }

    var currentEmail by remember { mutableStateOf("") }
    var newUsername by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)),
        color = Color(0xFFF2F2F2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Card(
                elevation = 10.dp,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fish),
                        contentDescription = "Fish Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Update Fisher Details",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = currentEmail,
                        onValueChange = { currentEmail = it },
                        label = { Text("Current Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newUsername,
                        onValueChange = { newUsername = it },
                        label = { Text("New Username") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newPhone,
                        onValueChange = { newPhone = it },
                        label = { Text("New Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Phone") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            when {
                                currentEmail.isBlank() || newUsername.isBlank() || newPhone.isBlank() || newPassword.isBlank() -> {
                                    Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    val updated = db.updateFisherDetails(
                                        currentEmail.trim(),
                                        newUsername.trim(),
                                        newPhone.trim(),
                                        newPassword.trim()
                                    )
                                    if (updated) {
                                        Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
                                        currentEmail = ""
                                        newUsername = ""
                                        newPhone = ""
                                        newPassword = ""
                                    } else {
                                        Toast.makeText(context, "No matching fisher found", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF00695C),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update", fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@Composable
fun OrderUpdate() {
    val context = LocalContext.current
    val db = remember { DB_Helper(context) }

    var ord by remember { mutableStateOf(listOf<Orders>()) }
    var msg by remember { mutableStateOf(listOf<UserData>()) }

    var email by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var orderDetail by remember { mutableStateOf("") }

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "SMS permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)),
        color = Color(0xFFF2F2F2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.fish),
                        contentDescription = "Fish Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Order Update",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Enter Your Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "Email Icon")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Enter New Location") },
                        leadingIcon = {
                            Icon(Icons.Default.Place, contentDescription = "Location Icon")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = orderDetail,
                        onValueChange = { orderDetail = it },
                        label = { Text("Enter New Order Details") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            when {
                                email.isBlank() -> Toast.makeText(context, "Enter your Email", Toast.LENGTH_LONG).show()
                                location.isBlank() -> Toast.makeText(context, "Enter your Location", Toast.LENGTH_LONG).show()
                                orderDetail.isBlank() -> Toast.makeText(context, "Enter your Order", Toast.LENGTH_LONG).show()
                                else -> {
                                    val matchingOrder = db.getAllOrders().find { it.email == email.trim() }

                                    if (matchingOrder != null) {
                                        val updateSuccess = db.updateOrderDetails(
                                            matchingOrder.id,
                                            location.trim(),
                                            orderDetail.trim()
                                        )

                                        if (updateSuccess) {
                                            Toast.makeText(context, "Order updated", Toast.LENGTH_SHORT).show()
                                            email = ""
                                            location = ""
                                            orderDetail = ""

                                            val fisherPhone = db.getAllFishers().find {
                                                it.email == matchingOrder.fisher_email
                                            }?.phone

                                            if (fisherPhone != null) {
                                                val message = """
                                                    Hello Fisherman,
                                                    Order updated by: ${matchingOrder.username}
                                                    
                                                    New Location: $location
                                                    Order Details: $orderDetail
                                                    
                                                    Contact: ${matchingOrder.phone_number}
                                                """.trimIndent()

                                                if (ContextCompat.checkSelfPermission(
                                                        context,
                                                        android.Manifest.permission.SEND_SMS
                                                    ) != PackageManager.PERMISSION_GRANTED
                                                ) {
                                                    smsPermissionLauncher.launch(android.Manifest.permission.SEND_SMS)
                                                } else {
                                                    try {
                                                        val smsManager = SmsManager.getDefault()
                                                        smsManager.sendTextMessage(
                                                            fisherPhone,
                                                            null,
                                                            message,
                                                            null,
                                                            null
                                                        )
                                                        Toast.makeText(
                                                            context,
                                                            "Message sent to fisherman",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    } catch (e: Exception) {
                                                        Toast.makeText(
                                                            context,
                                                            "SMS failed: ${e.message}",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(context, "Fisherman's phone not found", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "No order found for this email", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF00695C),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update Order", fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FisherDetails(
    dbHelper: DB_Helper = DB_Helper(LocalContext.current),
    email: String // selected fisher email
) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var buyer_email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var order by remember { mutableStateOf("") }

    // SMS Permission Request
    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "SMS permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)),
        color = Color(0xFFF2F2F2)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Card(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Place Your Order",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Your Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = buyer_email,
                        onValueChange = { buyer_email = it },
                        label = { Text("Your Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Your Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Your Location") },
                        leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = order,
                        onValueChange = { order = it },
                        label = { Text("Order Details") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            when {
                                email.isBlank() -> Toast.makeText(context, "No fisherman selected", Toast.LENGTH_LONG).show()
                                username.isBlank() -> Toast.makeText(context, "Enter your name", Toast.LENGTH_LONG).show()
                                buyer_email.isBlank() -> Toast.makeText(context, "Enter your email", Toast.LENGTH_LONG).show()
                                phone.isBlank() -> Toast.makeText(context, "Enter your phone", Toast.LENGTH_LONG).show()
                                location.isBlank() -> Toast.makeText(context, "Enter your location", Toast.LENGTH_LONG).show()
                                order.isBlank() -> Toast.makeText(context, "Enter your order", Toast.LENGTH_LONG).show()
                                else -> {
                                    dbHelper.insertOrder(email, username, buyer_email, location, phone, order)

                                    Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_LONG).show()

                                    // Send SMS to fisherman
                                    val fisherPhone = dbHelper.getAllFishers().find { it.email == email }?.phone
                                    val message = """
                                        Hello Fisherman,
                                        A new order has been placed by $username.

                                        Order Details:
                                        $order

                                        Contact: $phone
                                    """.trimIndent()

                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            android.Manifest.permission.SEND_SMS
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        smsPermissionLauncher.launch(android.Manifest.permission.SEND_SMS)
                                    } else {
                                        try {
                                            if (!fisherPhone.isNullOrBlank()) {
                                                val smsManager = SmsManager.getDefault()
                                                smsManager.sendTextMessage(fisherPhone, null, message, null, null)
                                            }
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "SMS failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    // Clear fields
                                    username = ""
                                    buyer_email = ""
                                    phone = ""
                                    location = ""
                                    order = ""
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF00695C),
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Place Order", fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun Orders() {
    val context = LocalContext.current
    val db = remember { DB_Helper(context) }

    var currentEmail by remember { mutableStateOf("") }
    var orderList by remember { mutableStateOf(emptyList<Orders>()) }

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "SMS permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        try {
            orderList = db.getAllOrders()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to load orders: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    if (orderList.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text("No Orders found", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Order count: 0")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(10.dp)) {
            items(orderList) { order ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Customer: ${order.username}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00695C)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "User Icon",
                                modifier = Modifier
                                    .size(70.dp)
                                    .padding(end = 16.dp),
                                tint = Color.Gray
                            )
                            Column {
                                Text("Customer Details", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Phone number: ${order.phone_number}", fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Location: ${order.location}", fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Order Placed:\n${order.order_detail}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = currentEmail,
                                onValueChange = { currentEmail = it },
                                label = { Text("Enter Email") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email Icon"
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.width(12.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                IconButton(onClick = {
                                    if (currentEmail.isBlank()) {
                                        Toast.makeText(context, "Enter your Email", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val isCustomer = currentEmail == order.email
                                        val isFisher = currentEmail == order.fisher_email

                                        if (isCustomer || isFisher) {
                                            val message = if (isCustomer) {
                                                "Hello Fisherman,\n\n${order.username} has cancelled the order:\n${order.order_detail}"
                                            } else {
                                                "Hello ${order.username},\n\nThe fisherman has cancelled your order:\n${order.order_detail}"
                                            }

                                            val phoneToSend = if (isCustomer) {
                                                // send SMS to fisherman
                                                db.getAllFishers().find { it.email == order.fisher_email }?.phone
                                            } else {
                                                // send SMS to customer
                                                order.phone_number
                                            }

                                            val permissionGranted = ContextCompat.checkSelfPermission(
                                                context,
                                                android.Manifest.permission.SEND_SMS
                                            ) == PackageManager.PERMISSION_GRANTED

                                            if (!permissionGranted) {
                                                smsPermissionLauncher.launch(android.Manifest.permission.SEND_SMS)
                                            } else {
                                                try {
                                                    if (!phoneToSend.isNullOrBlank()) {
                                                        val smsManager = SmsManager.getDefault()
                                                        smsManager.sendTextMessage(
                                                            phoneToSend,
                                                            null,
                                                            message,
                                                            null,
                                                            null
                                                        )
                                                    }
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "SMS failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            db.deleteOrder(order.id)
                                            Toast.makeText(context, "Order deleted", Toast.LENGTH_SHORT).show()
                                            orderList = db.getAllOrders()
                                        } else {
                                            Toast.makeText(context, "Unauthorized to delete this order", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.Red
                                    )
                                }
                                Text(text = "Delete", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
