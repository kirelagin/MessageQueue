#!/usr/bin/env python

import time
from suds.client import Client

def work(failing = False):
    url = "http://localhost:9999/mq/itmo.mq.MessageQueue?wsdl"
    client = Client(url)
    print client
    while True:
        envelope = client.service.getAnyBlocking()
        print envelope
        time.sleep(2)
        if not failing:
            client.service.put(envelope.tag, envelope.msg)

if __name__ == "__main__":
    work()
