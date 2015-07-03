# GCMTest Server
# Tested with Python 2.7.10

import sys
import httplib
import json

import config

google_api_host = 'gcm-http.googleapis.com'

destination = '/topics/global'
# destination = config.token

def gcmtest_send(message):
    headers = {
        'Content-Type' : 'application/json',
        'Authorization' : 'key=' + config.api_key
    }
    content = {
        'to' : destination,
        'data' : { 'message' : message }
    }
    conn = httplib.HTTPSConnection(google_api_host)
    conn.request('POST', '/gcm/send', json.dumps(content), headers)
    resp = conn.getresponse()
    if resp.status == 200:
        ans = resp.read()
    else:
        ans = resp.reason
        conn.close()
    return ans

def prompt_and_read_line():
    prompt = '> '
    sys.stdout.write(prompt)
    sys.stdout.flush()
    return sys.stdin.readline()

if __name__ == '__main__':
    for message in iter(prompt_and_read_line, ''):
        print(gcmtest_send(message))
