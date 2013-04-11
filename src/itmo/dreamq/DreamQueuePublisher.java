package itmo.dreamq;

import javax.xml.ws.Endpoint;

public class DreamQueuePublisher {

    public static void main(String[] args) {
        System.out.println("before start");
        Endpoint.publish("http://localhost:9999/mq", new MessageQueueImpl());
        System.out.println("server is ready");
    }
}
