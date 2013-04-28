package itmo.mq;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface MessageQueue {

    @WebMethod
    void ack(long ticketId);

    @WebMethod
    void put(int tag, Message m);

    @WebMethod
    Envelope get(int tag);

    @WebMethod
    Envelope getAny();

    @WebMethod
    boolean createQueue(int tag);

    @WebMethod
    Envelope getBlocking(int tag);

    @WebMethod
    Envelope getAnyBlocking();

}
