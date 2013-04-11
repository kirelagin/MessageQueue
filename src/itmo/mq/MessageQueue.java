package itmo.mq;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface MessageQueue {

    @WebMethod
    void ack(int id);

    @WebMethod
    void put(int tag, Message m);

    @WebMethod(operationName = "emptyGet")
    Task get();

    @WebMethod
    Task get(int tag);

}
