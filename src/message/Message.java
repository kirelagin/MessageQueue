package message;

/**
 * Created by IntelliJ IDEA.
 * User: Влад
 * Date: 11.04.13
 * Time: 21:19
 * To change this template use File | Settings | File Templates.
 */
public class Message {

    private byte[] msg;

    public Message(byte[] msg){
        this.msg = msg;
    }

    public Message(){
    }

    public void setMsg(byte[] msg){
        this.msg = msg;
    }

    public byte[] getMsg(){
        return msg;
    }


}
