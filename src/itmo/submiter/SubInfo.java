package itmo.submiter;

import itmo.mq.Message;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.io.IOException;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
@WebService
public interface SubInfo {

    @WebMethod
    double result(double a, double b) throws IOException, InterruptedException;

    @WebMethod
    void put(Message m) throws IOException, ClassNotFoundException;

}
