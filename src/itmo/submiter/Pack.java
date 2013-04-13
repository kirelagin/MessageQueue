package itmo.submiter;

import java.io.Serializable;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public class Pack implements Serializable {

    public String host;
    public double a;
    public double b;

    public Pack(double a, double b){
        host = "http://localhost:8888/sq";
        this.a = a;
        this.b = b;
    }

    public String getHost(){
        return host;
    }

    public double getA(){
        return a;
    }

    public double getB(){
        return b;
    }

}
