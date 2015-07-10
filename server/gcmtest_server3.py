# GCMTest Server
# Tested with Python 3.4.3

import sys
import http.client
import json

import config

google_api_host = 'gcm-http.googleapis.com'

if hasattr(config, 'token'):
    destination = config.token
elif hasattr(config, 'topic'):
    destination = '/topics/' + config.topic
else:
    destination = '/topics/global'

def gcmtest_send(message):
    headers = {
        'Content-Type' : 'application/json',
        'Authorization' : 'key=' + config.api_key
    }
    content = {
        'to' : destination,
        'data' : { 'message' : message }
    }
    conn = http.client.HTTPSConnection(google_api_host)
    conn.request('POST', '/gcm/send', json.dumps(content), headers)
    resp = conn.getresponse()
    ans = resp.read() if resp.status == http.client.OK else resp.reason
    conn.close()
    return ans

def prompt_and_read_line():
    prompt = '> '
    sys.stdout.write(prompt)
    sys.stdout.flush()
    return sys.stdin.readline()

if __name__ == '__main__':
    print('api_key=' + config.api_key)
    print('destination=' + destination)
    for message in iter(prompt_and_read_line, ''):
        print(gcmtest_send(message))
