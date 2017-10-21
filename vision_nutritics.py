import io
import os
import requests

from google.cloud import vision

# Instantiates a client
vision_client = vision.Client()

# The name of the image file to annotate
file_name = os.path.join(
    os.path.dirname(__file__),
    'resources/apple.jpg')

# Loads the image into memory
with io.open(file_name, 'rb') as image_file:
    content = image_file.read()
    image = vision_client.image(
        content=content)

# Performs label detection on the image file
web_images = image.detect_web(limit=5)
foods = {'apple' : 95, 'bannana' : 105, 'orange' : 45, 
'kiwi' : 42, 'cheeseburger' : 350, 'pizza' : 400}

for entity in web_images.web_entities:
	#print(entity.description)
	if str.lower(str(entity.description)) in foods:
		print "You have Eaten: "
		print(entity.description)
		food_name = entity.description
		#Query
		r = requests.get('https://www.nutritics.com/api/v1.1/list/&food='+food_name, auth=('bturcott', 'Mustang1'))
		data = r.json()
		food_id = data['1']['id']
		r_1 = requests.get('https://www.nutritics.com/api/v1.1/DETAIL/&food='+str(food_id)+'quantity=serving', auth=('bturcott', 'Mustang1'))
		meta_data = r_1.json()
		print 'Calories: ' + str(meta_data['energyKcal']['val'])
		#print meta_data 