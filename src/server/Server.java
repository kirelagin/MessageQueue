package server;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
@WebService
public interface Server {

    @WebMethod
    QueueMessage get(byte[] data);

    @WebMethod
    QueueMessage put(byte[] data);

    @WebMethod
    QueueMessage complete(byte[] data);

}
