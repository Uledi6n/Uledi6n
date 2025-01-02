from kivy.lang import Builder
from kivymd.app import MDApp
from kivymd.uix.screen import MDScreen
from kivy.uix.screenmanager import ScreenManager, Screen
import sqlite3
from kivymd.uix.dialog import MDDialog
from kivymd.uix.button import MDRaisedButton
from kivy.app import App
from kivy.uix.image import Image
from kivymd.uix.pickers import MDTimePicker
from kivymd.uix.pickers import MDDatePicker 
from kivymd.uix.card import MDCard


kv = '''
ScreenManager:
    LoginScreen:
    HomeScreen:
    ProfileScreen:
    SettingsScreen:

<LoginScreen>:
    md_bg_color:0,0,1,1
    name:"loginpage"
    MDProgressBar:
        id:progress_bar
        orientation:"vertical"
        pos_hint:{"center_x":0.9,"center_y":0.5}
        max:3
        size_hint:(0.07,0.9)
        back_color:(1,0,0,1)
        value:0
        color:(0,1,0,1)
        elevation:30
    MDLabel:
        text:"Loginpage"
        halign:"center"
    MDCard:
        elevation:10
        orientation:"vertical"
        size_hint:0.7,0.9
        pos_hint:{"center_x":0.4,"center_y":0.5}
        md_bg_color:1,0,0,1
        spacing:"50sp"
        MDCard:
            md_bg_color:1,0,0,1
            elevation:15
            size_hint:(0.8,0.2)
            pos_hint:{"center_x":0.5,"center_y":0.5}
            MDLabel:
                text:"Login"
                halign:"center"
                valign:"top"
                font_style:"H3"
                theme_text_color:"Custom"
                text_color:0,0,1,1
            
           
        MDCard:
            orientation:'vertical'
            elevation:25
            size_hint:(0.9,0.6)
            pos_hint:{"center_x":0.5,"center_y":0.5}
            MDLabel:
            MDTextField:
                id:user
                required:True
                hint_text:"Username"
                icon_left:"account"
                size_hint_x:0.7
                pos_hint:{"center_x":0.5,"center_y":0.9}   
                on_text: root.update_progress_bar()
                line_color_normal:(0,0,1,1)
                line_color_focus:(0,1,0,1)
                hint_text_color_normal:(0,0,1,1)
                hint_text_color_focus:(0,1,0,1)
                helper_text_color_normal:(0,0,1,1)
                max_text_length:100
            
            
            MDTextField:
                id:email
                hint_text:"Email"
                icon_left:"email"
                max_text_length:150
            
                email:True
                helper_text:"Enter your Email"
                size_hint_x:0.7
                required:True
                pos_hint:{"center_x":0.5,"center_y":0.5}  
                on_text: root.update_progress_bar()
                line_color_normal:(0,0,1,1)
                line_color_focus:(0,1,0,1)
                hint_text_color_normal:(0,0,1,1)
                hint_text_color_focus:(0,1,0,1)
                helper_text_color_normal:(0,0,1,1)
            
            
            MDTextField:
                id:passw
                max_text_length:100
                hint_text:"Password"
                icon_left:"lock"
                password:True
                required:True
                helper_text:"Enter your password "
                size_hint_x:0.7
                pos_hint:{"center_x":0.5,"center_y":0.5}
                on_text: root.update_progress_bar()
                line_color_normal:(0,0,1,1)
                line_color_focus:(0,1,0,1)
                hint_text_color_normal:(0,0,1,1)
                hint_text_color_focus:(0,1,0,1)
                helper_text_color_normal:(0,0,1,1)
                theme_icon_color:"Custom"
                icon_color_focus:0,0.3,0,1
            
            
         
            MDRaisedButton:
                text:"Login"
                font_style:"H6"
                md_bg_color:1,0,0,1
                text_color:(0,0,1,1)
                pos_hint:{"center_x":0.5,"center_y":0.5}
                on_press:root.submit()
            MDLabel:
                 
        MDCard:
            size_hint:(0.8,0.2)
            pos_hint:{"center_x":0.5,"center_y":0.5}
            md_bg_color:(1,0,0,1)
            elevation :15   
            MDIconButton:
                icon:"facebook"
            
                pos_hint:{"center_x":0.5,"center_y":0.5}
                theme_text_color:"Custom"
                text_color:0,0,1,1
                on_press:root.Facebook()
            MDIconButton:
                icon:"whatsapp"
                theme_text_color:"Custom"
                text_color:0,0,1,1
                pos_hint:{"center_x":0.5,"center_y":0.5}
                on_press:root.whatsapp()
            MDIconButton:
                icon:"instagram"
                theme_text_color:"Custom"
                text_color:0,0,1,1
                pos_hint:{"center_x":0.5,"center_y":0.5}
                on_press:root.Instagram()
            MDIconButton:
                icon:"twitter"
                theme_text_color:"Custom"
                text_color:0,0,1,1 
                pos_hint:{"center_x":0.5,"center_y":0.5}
                on_press:root.twitter()                      
        
        
                
        
        
                       
<HomeScreen>:
    md_bg_color:0,0,1,1
    name:"Homepage"
    id:home
    MDBoxLayout:
        orientation:'vertical'
        MDTopAppBar:
            md_bg_color:(0.1,0.2,0.4,1)
            elevation:10
            icon_left:"key"
            title:"To You MyLove"
            left_action_items:[["menu",lambda x:nav.set_state("open")]] 
            
            pos_hint:{"top":1}
     
        MDCard:
            ripple_behavior:True
            orientation:"vertical"
            md_bg_color:0,0.4,0.3,1
            MDSegmentedButton:
                size_hint:(1,0.1)
                md_bg_color:0,0.5,0.3,1
                MDSegmentedButtonItem:
                    text:"sob"  
                MDSegmentedButtonItem:
                    text:"Date"
                    on_press:root.date()  
                MDSegmentedButtonItem:
                    text:"Time"
                    on_press:root.time()
            MDSmartTile:
                source:"stan6.png"
                
    MDNavigationDrawer:
        id:nav
        orientation:"vertical"
        md_bg_color:(0.2,0.3,0.5,1)
        drawer_logo:"stan4.png"
        MDBoxLayout:
            orientation:"vertical"
            MDTopAppBar:
                md_bg_color:(0.1,0.2,0.4,1)
                elevation:10
                title:"My Profile" 
                pos_hint:{"top":1}
                right_action_items:[["cancel", lambda x:nav.set_state("close")]]
                
            Image:
                source:"stan6.png"
            ScrollView:
                MDList:
                    OneLineIconListItem:
                        text:"Login"
                        font_style:"H6"
                        theme_text_color:"Custom"
                        text_color:(1,1,0.5,1)
                        on_press:
                            nav.set_state("close")
                            app.root.current="loginpage"
                        IconLeftWidget:
                            icon:"key"
                            theme_text_color:"Custom"
                            text_color:(1,0.4,1,1)
                    OneLineIconListItem:
                        text:"Love"
                        theme_text_color:"Custom"
                        text_color:(1,1,0.5,1)
                        font_style:"H6"
                        on_press:
                            nav.set_state("close")
                            app.root.current="settingspage"
                        IconLeftWidget:
                            icon:"heart"
                            theme_text_color:"Custom"
                            text_color:(1,0.4,1,1)
                    OneLineIconListItem:
                        text:"Love more"
                        theme_text_color:"Custom"
                        text_color:(1,1,0.5,1)
                        font_style:"H6"
                        on_press:
                            nav.set_state("close")
                            app.root.current="profilepage"
                        IconLeftWidget:
                            icon:"hand-heart"
                            theme_text_color:"Custom"
                            text_color:(1,0.4,1,1)
     
<ProfileScreen>:
    md_bg_color:1,0,0,1
    name:"profilepage"
    MDBoxLayout:
        spacing:50
        orientation:"vertical"
        md_bg_color:(0.6,0.6,0.6,1)
        MDTopAppBar:
            title:"PEACE & LOVE"
            font_style:"Subtitle1"
            left_action_items:[["heart", lambda x:prof.set_state("open")]] 
            md_bg_color:(0.5,0.5,0.3,1)
            
        MDCard:
            elevation:10
            size_hint:(0.9,0.8)
            pos_hint:{"center_x":0.5,"center_y":0.5}
            orientation:"vertical"
            md_bg_color:(0.5,0.5,0.1,1)
            MDLabel:
                text:"The pillars of Love"
                font_style:"H4"
                opacity:30 
                halign:"center"
            MDScrollView:
                MDList:
                    OneLineListItem:
                        text:"1.Patience" 
                        font_style:"H6"
                    OneLineListItem:
                        text:"2.Displine" 
                        font_style:"H6" 
                    OneLineListItem:
                        text:"3.respect" 
                        font_style:"H6"
                    OneLineListItem:
                        text:"4.Calm of heart" 
                        font_style:"H6"
                    OneLineListItem:
                        text:"5.Goodness to neighbors" 
                        font_style:"H6"
                    OneLineListItem:
                        text:"6.Grace to others" 
                        font_style:"H6"
                        
                                                        
        MDCard:
            elevation:10
            size_hint:(0.9,0.8)
            pos_hint:{"center_x":0.5,"center_y":0.5}
            orientation:"vertical"
            md_bg_color:(0.4,0.4,0.1,1)
            padding:25
            MDIcon:
                icon:"hand-heart"
                theme_text_color:"Custom"
                text_color:(1,0,0,1)
                font_size:"50sp"
                pos_hint:{"center_x":0.5,"center_y":0.5}
            ScrollView:
                MDLabel:
                    text:"As you love someone you want to be loved with the someone who loves but if he or she denied you, feelings that you got its so hurting , as how you feel pain and others feel that pain... Love bring health, joy and peace to the someone else.. love bring undefined harmony to someone else" 
                    font_style:"H6"
                    halign:"justify"
                    adaptive_height:True
            MDIcon:
                icon:"heart"          
                font_size:"40sp"
                pos_hint:{"center_x":0.5,"center_y":0.5}
                theme_text_color:"Custom"
                text_color:(1,0,0,1)    
                                                                                                                      
   
    MDNavigationDrawer:
        id:prof
        orientation:"vertical"
        md_bg_color:(0.9,0.9,0.5,1)
        drawer_logo:"stan4.png"
        MDBoxLayout:
            orientation:"vertical"
            MDTopAppBar:
                md_bg_color:(0.6,0.6,0.2,1)
                elevation:10
                title:"Real love" 
                pos_hint:{"top":1}
                right_action_items:[["cancel", lambda x: prof.set_state("close")]]
                
            Image:
                source:"stan6.png"
            ScrollView:
                MDList:
                    OneLineIconListItem:
                        text:"Login"
                        font_style:"H6"
                        theme_text_color:"Custom"
                        text_color:(0.3,0.3,0.3,1)
                        on_press:
                            prof.set_state("close")
                            app.root.current="loginpage"
                        IconLeftWidget:
                            icon:"lock"
                            theme_text_color:"Custom"
                            text_color:(1,0,0,1)
                    OneLineIconListItem:
                        text:"Home"
                        theme_text_color:"Custom"
                        text_color:(0.3,0.3,0.3,1)
                        font_style:"H6"
                        on_press:
                            prof.set_state("close")
                            app.root.current="Homepage"
                        IconLeftWidget:
                            icon:"home"
                            theme_text_color:"Custom"
                            text_color:(1,0,0,1)
                    OneLineIconListItem:
                        text:"Love"
                        theme_text_color:"Custom"
                        text_color:(0.3,0.3,0.3,1)
                        font_style:"H6"
                        on_press:
                            prof.set_state("close")
                            app.root.current="settingspage"
                        IconLeftWidget:
                            icon:"heart"
                            theme_text_color:"Custom"
                            text_color:(1,0,0,1)
     

            
<SettingsScreen>:
    name:"settingspage"
    orientation:"vertical"
    md_bg_color:(0,1,1,1)
    MDBoxLayout:
        orientation:"vertical"
        md_bg_color:(0,1,1,1)
        MDTopAppBar:
            title:"LOVE MORE"
            font_style:"Subtitle1"
            left_action_items:[["heart", lambda x:bottom.set_state("open")]] 
            md_bg_color:(0,0.4,0.4,1)
        MDCard:
            orientation:"vertical"
            size_hint:(0.8,0.6)
            elevation:10
            md_bg_color:(0,0.7,0.7,1)
            pos_hint:{"center_x":0.5,"center_y":0.5}
           
            MDIcon:
                icon:"hand-heart"
                theme_text_color:"Custom"
                text_color:(1,0.2,0.2,1)
                font_size:"50sp"
                pos_hint:{"center_x":0.5,"center_y":0.5}
            MDLabel:
                font_style:"H6"
                halign:"center"
                text:"My heart beats for you everytime,I feel in love with you"
        MDLabel:
            text:"Hearts with full of loves feel a lot of joy. All the time smile internally,Looks Shy with a lot of love" 
            font_style:"H6"
            halign:"center"                    
        MDCard:
            orientation:"vertical"
            elevation:15
            size_hint:(0.7,0.9)
            pos_hint:{"center_x":0.5,"center_y":0.5}
            md_bg_color:(0,0.6,0.6,1)
            MDLabel:
                text:"My Love"
                font_style:"H6"
                text_color:(0.5,0.7,0.6,1)
                halign:"center"
            MDIcon:
                text:"message"
                icon:"heart"
                badge_icon:"numeric-10"
                theme_text_color:"Custom"
                text_color:(0,0.2,0.8,1)
                font_size:"30sp"
                pos_hint:{"center_x":0.5,"center_y":0.5}
            MDLabel:
                text:"Some One loves you finds you please find him or her"
                font_style:"Subtitle1"
                halign:"center"  
            
        MDIconButton:
            icon:"hand-heart"
            theme_text_color:"Custom"
            text_color:(1,0,0,1)           
            icon_size:"150sp"
            pos_hint:{"center_x":0.5,"center_y":0.5}
            elevation:10
            on_press:root.love()  

    MDNavigationDrawer:
        id:bottom
        orientation:"vertical"
        md_bg_color:(0.2,0.5,0.5,1)
        drawer_logo:"stan4.png"
        MDBoxLayout:
            orientation:"vertical"
            MDTopAppBar:
                md_bg_color:(0.2,0.4,0.4,1)
                elevation:10
                title:"My Love" 
                pos_hint:{"top":1}
                right_action_items:[["cancel", lambda x:bottom.set_state("close")]]
            Image:
                source:"stan6.png"
            ScrollView:
                MDList:
                    OneLineIconListItem:
                        text:"Login"
                        font_style:"H6"
                        theme_text_color:"Custom"
                        text_color:(1,1,0.5,1)
                        on_press:
                            bottom.set_state("close")
                            app.root.current="loginpage"
                        IconLeftWidget:
                            icon:"lock"
                            theme_text_color:"Custom"
                            text_color:(1,0.4,1,1)
                    OneLineIconListItem:
                        text:"Home"
                        theme_text_color:"Custom"
                        text_color:(1,1,0.5,1)
                        font_style:"H6"
                        on_press:
                            bottom.set_state("close")
                            app.root.current="Homepage"
                        IconLeftWidget:
                            icon:"home"
                            theme_text_color:"Custom"
                            text_color:(1,0.4,1,1)
                    OneLineIconListItem:
                        text:"love"
                        theme_text_color:"Custom"
                        text_color:(1,1,0.5,1)
                        font_style:"H6"
                        on_press:
                            bottom.set_state("close")
                            app.root.current="profilepage"
                        IconLeftWidget:
                            icon:"heart"
                            theme_text_color:"Custom"
                            text_color:(1,0.4,1,1)
                            
                    OneLineListItem:
                        spacing:10
                        padding:10
                        MDLabel:
                            text:"You Looks so beautiful"
                            halign:"center"
                            font_style:"H3"
                            opacity:50
                            theme_text_color:"Custom"
                            text_color:(1,1,0.4,1)
                        
                    OneLineListItem:
                        MDLabel:
                            text:"You looks so cute, fresh with a lot of favorite smile,, I try to be silent but my heart failed to pump your love,, On each I grow with a strong love to you,, I love you" 
                            halign:"center"
                            font_style:"Subtitle1"
                            opacity:30
                            theme_text_color:"Custom"
                            text_color:(0,0.2,0.4,1)
                            font_style:"H6" 
                                  
                            
                            
                            
                            
                            

                                                                        
'''                     
   
            
        

class LoginScreen(MDScreen):
    def update_progress_bar(self):
        """Updates the progress bar based on input fields."""
        progress = 0
        if self.ids.user.text:
            progress += 1
        if self.ids.email.text:
            progress += 1
        if self.ids.passw.text:
            progress += 1
        self.ids.progress_bar.value = progress

    def submit(self):
        username = self.ids.user.text
        email = self.ids.email.text
        passw = self.ids.passw.text
        
        conn = sqlite3.connect("mydb")
        cur = conn.cursor()
        cur.execute("""
        CREATE TABLE IF NOT EXISTS login(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username varchar(120),
        email varchar(120),
        password varchar(100)
        )
        """)
        
        if username=="":
            self.dd=MDDialog(
            title=f"Sorry\nfill the username",
            md_bg_color=(1, 1, 0, 1),
            auto_dismiss=False,
            overlay_color=(1,0,1,1),
            
            buttons=[
            MDRaisedButton(
            text="OK",
            md_bg_color=(1, 0, 0, 1),
            on_press=self.close_dialog
            )
            
            ]
            )
            self.dd.open()
        elif email=="":
            self.dd=MDDialog(
            title=f"Sorry\nfill the email",
            md_bg_color=(1, 1, 0, 1),
            auto_dismiss=False,
            overlay_color=(1,0,1,1),
            buttons=[
            MDRaisedButton(
            md_bg_color=(1,0, 0, 1),
            text="OK",
            on_press=self.close_dialog
            )
            
            ]
            )
            self.dd.open()   
        elif "@" and "gmail.com" not in email:
            self.dd=MDDialog(
            title=f"Please!!\n fill a correct email with\n your_name@gmail.com",
            md_bg_color=(1, 1, 0, 1),
            auto_dismiss=False,
            overlay_color=(1,0,1,1),
            buttons=[
            MDRaisedButton(
            md_bg_color=(1, 0, 0, 1),
            text="OK",
            on_press=self.close_dialog
            )
            
            ]
            )
            self.dd.open()
            
        elif passw=="":
            self.dd=MDDialog(
            title=f"Sorry\nfill the username",
            md_bg_color=(1, 1, 0, 1),
            auto_dismiss=False,
            overlay_color=(1,0,1,1),
            buttons=[
            MDRaisedButton(
            text="OK",
            md_bg_color=(1,0, 0, 1),
            on_press=self.close_dialog
            )
            
            ]
            )
            self.dd.open()
            
        else:
            sql = cur.execute("INSERT INTO login(username, email, password) VALUES (?,?,?)", (username, email, passw))
            conn.commit()
            conn.close()
        
            if sql:
                btn = MDRaisedButton(
                text="Ok",
                md_bg_color=(0, 1, 0, 1),
                on_press=self.go_to_homepage
                )
            
                self.dialog = MDDialog(
                title="Sign in successfully",
                auto_dismiss=False,
                show_duration=3,
                hide_duration=5,
                text=f"Successfully inserted:\nUsername: {username}\nEmail: {email}\nPassword: {passw}",
                md_bg_color=(0, 1, 0, 1),
                overlay_color=(0.2, 0, 0.4, 1),
                size_hint=(0.6, 0.4),
                buttons=[btn]
                )
                self.dialog.open()
            else:
                self.dialog = MDDialog(
                text="Unsuccessful",
                )
                self.dialog.open()
                                              
        
    def go_to_homepage(self, *args):
        self.dialog.dismiss()
        App.get_running_app().root.current = "Homepage"
    
    def close_dialog(self,*args):
        self.dd.dismiss()
        
    def social(self,*args):
        self.dial.dismiss()    
        
    def Facebook(self,*args):
        self.dial=MDDialog(
        title=f"FACEBOOK:\nContact me \n+255-684-650-539",
        md_bg_color=(0, 0.2, 0.6, 1),
        auto_dismiss=False,
        overlay_color=(0.2,0.2,0.2,1),
        buttons=[
        MDRaisedButton(
        text="OK",
        md_bg_color=(0,0, 0, 1),
        on_press=self.social
        )
            
        ]
        
        
        )
        self.dial.open()
    def whatsapp(self,*args):
        self.dial=MDDialog(
        title=f"WHATSAPP:\nContact me \n+255-684-650-539",
        md_bg_color=(0, 1, 0.1, 1),
        auto_dismiss=False,
        overlay_color=(0.2,0.2,0.2,1),
        buttons=[
        MDRaisedButton(
        text="OK",
        text_color=(0,0.1,0.1,1),
        md_bg_color=(1,1, 0.2, 1),
        on_press=self.social
        )
            
        ]
        
        
        )
        self.dial.open()               
    def twitter(self,*args):
        self.dial=MDDialog(
        title=f"TWITTER:\nContact me \n+255-684-650-539",
        md_bg_color=(1, 0, 0, 1),
        auto_dismiss=False,
        overlay_color=(0,0.1,0.1,1),
        buttons=[
        MDRaisedButton(
        text="OK",
        md_bg_color=(0,0, 1, 1),
        on_press=self.social
        )
            
        ]
        
        
        )
        self.dial.open()  
    def Instagram(self,*args):
        self.dial=MDDialog(
        title=f"INSTAGRAM:\nContact me \n+255-684-650-539",
        md_bg_color=(1, 0.2, 0.2, 1),
        auto_dismiss=False,
        overlay_color=(0,0.1,0.1,1),
        buttons=[
        MDRaisedButton(
        text="OK",
        text_color=(0,0,1,1),
        md_bg_color=(1,1, 1, 1),
        on_press=self.social
        )
            
        ]
        
        
        )
        self.dial.open()
                     

class HomeScreen(MDScreen):
    def time(self,*args):
        file=MDTimePicker()
        file.open()
    def date(self,*args):
        file=MDDatePicker()
        file.open()    
        
class ProfileScreen(MDScreen):
    pass

class SettingsScreen(MDScreen):
    def love(self,*args):
        self.up=MDDialog(
        title=f"I Love you so much\n you are a source of joy to me",
        text="I love u ",
        md_bg_color=(0, 1, 0.2, 1),
        auto_dismiss=False,
        show_duration=3,
        overlay_color=(1,0.1,0.1,1),
        buttons=[
        MDRaisedButton(
        text="Thank",
        text_color=(1,1,1,1),
        md_bg_color=(0.2,0.4, 0.4, 1),
        on_press=self.love_close
        )
            
        ]
        
        
        )
        self.up.open()
        
    def love_close(self,*args):
        self.up.dismiss()    
class myApp(MDApp):
    def build(self):
        return Builder.load_string(kv)

if __name__ == "__main__":
    myApp().run()
    