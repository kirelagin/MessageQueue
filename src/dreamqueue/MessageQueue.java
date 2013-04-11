package dreamqueue;

import message.Message;
import message.Task;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by IntelliJ IDEA.
 * User: Влад
 * Date: 11.04.13
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */

@WebService
public interface MessageQueue {

    @WebMethod
    void ack(int id);

    @WebMethod
    void put(int tag, Message m);

    @WebMethod
    Task get();
    
    @WebMethod
    Task get(int tag);
}
