import requests

r = requests.get('https://www.nutritics.com/api/v1.1/list/&food=bananna', auth=('bturcott', 'Mustang1'))
data = r.json()
food_id = data['1']['id']
r_1 = requests.get('https://www.nutritics.com/api/v1.1/DETAIL/&food='+str(food_id)+'quantity=serving', auth=('bturcott', 'Mustang1'))
meta_data = r_1.json()
print meta_data['energyKcal']['val']
