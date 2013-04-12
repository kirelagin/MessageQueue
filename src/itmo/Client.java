package itmo;

import itmo.mq.Envelope;
import itmo.mq.Message;
import itmo.mq.MessageQueue;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public class Client {

    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        URL url = new URL("http://localhost:9999/mq?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://dreamq.itmo/", "DreamQueueService");

        Service service = Service.create(url, qname);

        MessageQueue hello = service.getPort(MessageQueue.class);

        byte[] mb = new byte[3];
        mb[0] = mb[1] = mb[2] = 1;
        Message m = new Message(mb);
        Envelope e = hello.getAny();
        System.out.println(e.getTag());
        System.out.println(e.getTicketId());
        System.out.println((e.getMsg() == null ) ? -1 : 1);
    }

}
