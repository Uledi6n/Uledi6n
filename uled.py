import kivy
from kivy.app import App
from kivy.uix.label import Label 
from kivy.uix.image import Image
from kivy.uix.boxlayout import BoxLayout
class myapp(App):
    def image(self):
        
        layout=BoxLayout (orientation="vertical",spacing=10,padding=10)
        
        img=Image(source="stan4.png")
        layout.add_widget(img)
        
        return layout 
myapp().run()