#!/usr/bin/env python

import time
from suds.client import Client

def work(lazy = True):
    url = "http://localhost:9999/mq/itmo.mq.MessageQueue?wsdl"
    client = Client(url)
    print client
    while True:
        envelope = client.service.getAnyBlocking()
        print envelope
        time.sleep(2)
        if lazy:
            client.service.put(envelope.tag, envelope.msg)
            client.service.ack(envelope.ticketId)

if __name__ == "__main__":
    work()
