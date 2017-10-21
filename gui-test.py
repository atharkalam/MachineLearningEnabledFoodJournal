#Program: ML_Food_Journal
#By: Brad Turcott
#Date: 04/29/2017
import Tkinter
import tkMessageBox
import io
import os

from Tkinter import *
from PIL import Image, ImageTk
from google.cloud import vision

#Creates the window and labels it
top = Tkinter.Tk()
top.wm_title("ML_Food_Journal")

#Instantiates a client
vision_client = vision.Client()

#Sets the apple background image
bkg_img = Image.open('/Users/bturcott/Pictures/food_journal.jpg')
tkimage = ImageTk.PhotoImage(bkg_img)
Tkinter.Label(top,image = tkimage).grid(row=0, column=3, rowspan=4, sticky=W+E+N+S, padx=5, pady=5)

#Label for IP Address
label1 = Label(top, text = "Enter Atlas IP Address:")
label1.grid(row=0, column=0, sticky=E+S)

#Textbox for IP Entry
entry1 = Entry(top, bd = 4)
entry1.grid(row=0, column=1, sticky=S)

#Label for filepath Address
label2 = Label(top, text = "Enter filepath to image:")
label2.grid(row=1, column=0, sticky=E+N)

#Textbox for Filepath Entry
entry2 = Entry(top, bd = 4)
entry2.grid(row=1, column=1, sticky=N)

def scoreImage():
	file_name = os.path.join(
		os.path.dirname(__file__)+'resources/',
		entry2.get())
	# Loads the image into memory
	with io.open(file_name, 'rb') as image_file:
	    content = image_file.read()
	    image = vision_client.image(
	        content=content)
	labels = image.detect_labels()
	#for label in labels:
		#print label.description, 'Score: ', label.score
	for i in labels[0:2]:
		print labels[i]
	print len(labels)
	#tkMessageBox.showinfo(labels[0].description)


#Button for sending data
button1 = Tkinter.Button(top,text = "Classify Image", command=scoreImage)
button1.grid(row=3,column=1, sticky=E)

top.mainloop()
