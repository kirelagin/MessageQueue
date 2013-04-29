#!/usr/bin/env python

import random
import time
from suds.client import Client

def work(failing = False):
    url = "http://localhost:9999/mq/itmo.mq.MessageQueue?wsdl"
    client = Client(url)
    print client
    while True:
        tag = random.randint(-2, -1)
        msg = client.service.getBlocking(tag)
        print msg
        time.sleep(2)
        if not failing:
            client.service.put(tag, msg)

if __name__ == "__main__":
    work()
