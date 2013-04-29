#!/usr/bin/env python

import time
from suds.client import Client

def work(failing = False):
    url = "http://localhost:9999/mq/itmo.mq.MessageQueue?wsdl"
    client = Client(url)
    print client
    while True:
        msg = client.service.getAnyBlocking()
        print msg
        time.sleep(2)
        if not failing:
            client.service.put(msg.getTag(), msg)

if __name__ == "__main__":
    work()
