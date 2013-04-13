package itmo.submiter;

import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
import itmo.mq.Message;
import itmo.mq.MessageQueue;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class Submiter implements SubInfo{

    private static String urlString = "http://localhost:9999/mq?wsdl";

    private static QName serviceQName = new QName("http://dreamq.itmo/", "DreamQueueService");
    private static QName portName = new QName("http://dreamq.itmo/", "DreamQueuePort");

    private static Service service;

    private static MessageQueue mq;

    private boolean flag = false;
    private double result = 0;

    static {
        try{
            URL url = new URL(urlString);
            service = Service.create(url, serviceQName);
        } catch (MalformedURLException mur){
            System.exit(0);
        }
        mq = service.getPort(portName, MessageQueue.class);
    }

    @Override
    public double result(double a, double b) throws IOException, InterruptedException {
        Pack p = new Pack(a, b);
        System.out.println(a + " " + b);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(os);
        o.writeObject(p);
        byte[] bytes = os.toByteArray();
        Message m = new Message(bytes);
        mq.put(1, m);
        while (!flag){
            Thread.sleep(1000);
        }
        return result;
    }

    @Override
    public void put(Message m) throws IOException, ClassNotFoundException {
        ByteArrayInputStream is = new ByteArrayInputStream(m.getMsg());
        ObjectInputStream ois = new ObjectInputStream(is);
        Double d = (Double) ois.readObject();
        result = d;
        flag = true;
    }



}
