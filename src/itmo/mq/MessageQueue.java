package itmo.mq;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface MessageQueue {

    @WebMethod
    void ack(long ticketId);

    @WebMethod
    void put(int tag, Message m);

    @WebMethod
    Envelope get(int tag);

    @WebMethod
    Envelope getAny();

}
