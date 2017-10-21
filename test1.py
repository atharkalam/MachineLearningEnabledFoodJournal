#Example to use an API call with Python v2 and authentication
#You have to use these basic libraries
import urllib2
import sys
import re
import base64
import json
from urlparse import urlparse

theurl = 'https://www.nutritics.com/api/v1.1/LIST/food=bannana'
username = 'bturcott'
password = 'Mustang1'

req = urllib2.Request(theurl)
try:
    handle = urllib2.urlopen(req)
except IOError, e:
    # Here we want to fail
    pass
else:
    sys.exit(1)

if not hasattr(e, 'code') or e.code != 401:
    # Uknown error (no response code)
    sys.exit(1)

#We extract scheme and realm
authline = e.headers['www-authenticate']
authobj = re.compile(r'''(?:\s*www-authenticate\s*:)?\s*(\w*)\s+realm=['"]([^'"]+)['"]''',re.IGNORECASE)
matchobj = authobj.match(authline)

if not matchobj:
    # here something is wrong (you have to catch an error)
    print authline
    sys.exit(1)

scheme = matchobj.group(1)
realm = matchobj.group(2)
# here we've extracted the scheme and the realm from the header
if scheme.lower() != 'basic':
    # here something is wrong (you have to catch an error)
    sys.exit(1)

base64string = base64.encodestring('%s:%s' % (username, password))[:-1]
authheader =  "Basic %s" % base64string
req.add_header("Authorization", authheader)

try:
    handle = urllib2.urlopen(req)
except IOError, e:
    # authentication failed
    print "The username or password is wrong."
    sys.exit(1)

#We read the result
thepage = handle.read()

#Print the returned json data out
print thepage;

jsondata = json.loads(thepage)
#Loop through each object and print out their id
for item in jsondata:
    print item["id"]