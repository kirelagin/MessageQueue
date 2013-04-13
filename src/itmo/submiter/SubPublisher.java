package itmo.submiter;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public class SubPublisher {

    public static void main(String[] args) throws InterruptedException, IOException {
        Endpoint.publish("http://localhost:8888/sq", new Submiter());
        Thread.sleep(3000);
        System.out.println("ready to fuck");
        URL url2 = new URL("http://localhost:8888/sq" + "?wsdl");
        QName q = new QName("http://submiter.itmo/", "SubmiterService");

        Service service1 = Service.create(url2, q);

        QName qz = new QName("http://submiter.itmo/", "SubmiterPort");

        SubInfo sub = service1.getPort(qz, SubInfo.class);
        System.out.println(sub.result(1, 7));
    }
}
