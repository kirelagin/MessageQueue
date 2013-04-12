package itmo.mq;

public class Message {

    public byte[] msg;

    public Message(byte[] msg) {
        this.msg = msg;
    }

    public Message() {
        msg = null;
    }

    public byte[] getMsg() {
        return msg;
    }

}
