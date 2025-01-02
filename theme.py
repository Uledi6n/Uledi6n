from kivymd.app import MDApp
from kivy.lang import Builder


kv="""
MDBoxLayout:
    orientation:"vertical"
    MDIconButton:
        icon:"eye"
        pos_hint:{"center_x":0.9,"center_y":0.9}
        on_release: app.open_theme()
    AsyncImage:
        source:"logo.jpg"


"""







class Mydata(MDApp):
    def build(self):
        return Builder.load_string(kv)
        
    def open_theme(self):
        if self.theme_cls.theme_style=="Light":
            self.theme_cls.theme_style="Dark"  
        else:
            self.theme_cls.theme_style="Light"
            
                        
Mydata().run()        