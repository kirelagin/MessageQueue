package itmo;

import itmo.mq.Envelope;
import itmo.mq.Message;
import itmo.mq.MessageQueue;
import itmo.submiter.Pack;
import itmo.submiter.SubInfo;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.*;
import java.net.URL;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public class Client {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        URL url = new URL("http://localhost:9999/mq?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://dreamq.itmo/", "DreamQueueService");

        Service service = Service.create(url, qname);

        MessageQueue hello = service.getPort(MessageQueue.class);

        Envelope e = hello.getAny();
        ByteArrayInputStream is = new ByteArrayInputStream(e.getMsg().getMsg());
        ObjectInputStream ois = new ObjectInputStream(is);
        Pack p = (Pack) ois.readObject();

        double c = p.getA() + p.getB();

        System.out.println(c);

        URL url2 = new URL(p.getHost() + "?wsdl");
        QName q = new QName("http://submiter.itmo/", "SubmiterService");

        Service service1 = Service.create(url2, q);

        QName qz = new QName("http://submiter.itmo/", "SubmiterPort");

        SubInfo sub = service1.getPort(qz, SubInfo.class);

        byte[] r;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(c);
        r = os.toByteArray();
        Message mr = new Message(r);
        sub.put(mr);

    }

}
