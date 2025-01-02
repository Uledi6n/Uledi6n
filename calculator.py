import math
from kivymd.app import MDApp
from kivy.lang import Builder
from kivymd.uix.boxlayout import MDBoxLayout 


kv="""
MDBoxLayout:
    orientation:"vertical"
    md_bg_color:(0,0.2,0.2,1)
    
    MDTopAppBar:
        left_action_items:[["menu"]]
        title:"Casio fx-991ES PLUS"
        md_bg_color:(0.2,0.3,0.3,1)
    MDCard:
        orientation:"vertical"
        size_hint:(1,0.9)
        md_bg_color:(0.1/6,0.2,0.3/2,1)
        pos_hint:{"center_x":0.5,"center_y":0.5}
        
        
        MDTextFieldRect:
            id:display
            multiline:True 
            size_hint:(1,0.3)
            md_bg_color:(0.5,0.7,0.8,1)
            readonly:True
            font_size:"25sp"
            pos_hint:{"center_x":0.5,"center_y":0.7}
        MDGridLayout:
            cols:5
            rows:12
            spacing:20
            height: self.minimum_height
            font_size:"30sp"
            MDRaisedButton:
                text: "MD"
                pos_hint:{"center_x":0.5,"center_y":0.9}
                md_bg_color:(0,0.4,0.4,1) 
                elevation:5
                bd:29
            MDRaisedButton:
                text: "ALP"
                
                md_bg_color:(0,0.3,0.3,1)
                
                elevation:5
            MDRaisedButton:
                text: "SHF"
                
                md_bg_color:(0.1,0.3,0.3,1)
                
                elevation:5
            MDRaisedButton:
                text: "AC"
                on_release: app.clear_display()
                md_bg_color:(1,0,0,1)
                
                elevation:5
            MDRaisedButton:
                text: "DEL"
                on_release: app.delete_last_character()
                md_bg_color:(1,0.3,0.3,1)
                
                elevation:5 
            MDRaisedButton:
                text: "sin"
                
                md_bg_color:(0.3,0.3,0.3,1)
                
                elevation:5
            MDRaisedButton:
                text: "tan"
                
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "cos"
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "M+"
                
                md_bg_color:(0,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "π"
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "√"
                on_release: app.add_character("√")
                elevation:5
                md_bg_color:(0.3,0.3,0.3,1)
            MDRaisedButton:
                text: "^"
                on_release: app.add_character("^")
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "MR"
                
                elevation:5
                md_bg_color:(0,0.3,0.3,1)
            MDRaisedButton:
                text: "%"
                on_release: app.add_character("%")
                elevation:5
                md_bg_color:(0.3,0.3,0.3,1)
            MDRaisedButton:
                text: "e"
                
                md_bg_color:(0.4,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "ln"
                
                elevation:5
                md_bg_color:(0.5,0.3,0.3,1)
            MDRaisedButton:
                text: "log"
                
                elevation:5
                md_bg_color:(0,0.3,0.3,1)
            MDRaisedButton:
                text: "("
                md_bg_color:(0.4,0.3,0.3,1)
                on_release: app.add_character("(")
                elevation:5
                
            MDRaisedButton:
                text: ")"
                on_release: app.add_character(")")
                elevation:5
                md_bg_color:(0.4,0.3,0.3,1)
            MDRaisedButton:
                text: "+"
                on_release: app.add_character("+")
                elevation:5
                md_bg_color:(0,0.6,0.3,1)
            MDRaisedButton:
                text: "M-"
                
                md_bg_color:(0,0.3,0.3,1)
                
                elevation:5
            MDRaisedButton:
                text: "9"
                on_release: app.add_character("9")
                md_bg_color:(0.3,0,1,1)
                elevation:5
            MDRaisedButton:
                text: "8"
                md_bg_color:(0.3,0,1,1)
                elevation:5
                on_release: app.add_character("8")
            MDRaisedButton:
                text: "7"
                on_release: app.add_character("7")
                md_bg_color:(0.3,0,1,1) 
                elevation:5   
            MDRaisedButton:
                text: "*"
                md_bg_color:(0,0.6,0.3,1)
                elevation:5
                on_release: app.add_character("*")
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: "6"
                on_release: app.add_character("6")
                md_bg_color:(0.3,0,1,1)
                elevation:5
            MDRaisedButton:
                text: "5"
          
                elevation:5
                on_release: app.add_character("5")
                md_bg_color:(0.3,0,1,1)
            MDRaisedButton:
                text: "4"
                md_bg_color:(0.3,0,1,1)
                elevation:5
                on_release: app.add_character("4")
            MDRaisedButton:
                text: "-"
                on_release: app.add_character("-")
                md_bg_color:(0,0.6,0.3,1)
                elevation:5    
                           
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "3"
                md_bg_color:(0.3,0,1,1)
                elevation:5
                on_release: app.add_character("3")
            MDRaisedButton:
                text: "2"
                md_bg_color:(0.3,0,1,1)
                elevation:5
                on_release: app.add_character("2")
            MDRaisedButton:
                text: "1"
                md_bg_color:(0.3,0,1,1)
                elevation:5
                on_release: app.add_character("1")  
            MDRaisedButton:
                text: "÷"
                on_release: app.add_character("/")
                md_bg_color:(0,0.6,0.3,1)
                elevation:5
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "0"
                md_bg_color:(0.3,0,1,1)
                elevation:5
                on_release: app.add_character("0")
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1) 
                elevation:5   
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1) 
                elevation:5   
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0,0.7,0.3,1)
                elevation:5
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: ""
                
                elevation:5
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1) 
                elevation:5   
            MDRaisedButton:
                text: ""
                
                md_bg_color:(0.3,0.3,0.3,1)
                elevation:5
            MDRaisedButton:
                text: "."
                md_bg_color:(0,0.2,0.5,1)
                elevation:5
                on_release: app.add_character(".")
            MDRaisedButton:
                text: "="
                on_release: app.calculate_result()
                md_bg_color:(1,0,0,1)
                elevation:5
            
                
        
"""

class calc(MDApp):
    def build(self):
        return Builder.load_string(kv)
        
    def add_character(self, char):
        display = self.root.ids.display
        if display.text == "0":
            display.text = char
        else:
            display.text += char 
            
    def calculate_result(self):
        """Evaluate the expression in the display."""
        display = self.root.ids.display
        try:
            result = str(eval(display.text,{"__builtins__": None},{
                "sin": math.sin,
                "cos": math.cos,
                "tan": math.tan,
                "sqrt": math.sqrt,
                "log": math.log,
                "exp": math.exp,
                "pi": math.pi,
                "**": math.pow
            }))
            display.text = result
        except Exception:
            display.text = "Error"
    def clear_display(self):
        """Clear the display."""
        self.root.ids.display.text = "0"

    def delete_last_character(self):
        """Delete the last character from the display."""
        display = self.root.ids.display
        if len(display.text) > 1:
            display.text = display.text[:-1]
        else:
            display.text = "0"                   

calc().run()                