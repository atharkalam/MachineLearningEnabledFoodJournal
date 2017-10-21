import io
import os

# Imports the Google Cloud client library
from google.cloud import vision

# Instantiates a client
vision_client = vision.Client()

# The name of the image file to annotate
file_name = os.path.join(
    os.path.dirname(__file__),
    'resources/pizza_coke.jpg')

# Loads the image into memory
with io.open(file_name, 'rb') as image_file:
    content = image_file.read()
    image = vision_client.image(
        content=content)

# Performs label detection on the image file
web_images = image.detect_web(limit=5)
foods = {'apple' : 95, 'bannana' : 105, 'orange' : 45, 
'kiwi' : 42, 'cheeseburger' : 350, 'pizza' : 400}

#for web_image in web_images.web_entities:
#	if web_image.description in foods:
#		print 'You have eaten a',web_image.description,
#		print 'it contains',foods[web_image.description],'calories.'
#	print web_image.description

for entity in web_images.web_entities:
	print(entity.description)
	if str.lower(str(entity.description)) in foods:
		print('=' * 20)
		print(entity.description)

#class Foods(object):
#	food_list = {'apple' : 95, 'bannana' : 105, 'orange' : 45, 
#				'kiwi' : 42, 'cheeseburger' : 350, 'pizza' : 400}



