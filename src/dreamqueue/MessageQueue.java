package dreamqueue;

import message.Message;
import message.Task;

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface MessageQueue {

    @WebMethod
    void put(int tag, Message m);

    @WebMethod
    Task get();
    
    @WebMethod
    Task get(int tag);

    @WebMethod
    void ack(int id);

}
