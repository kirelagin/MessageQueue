package message;

public class Message {

    private byte[] msg;

    public Message(byte[] msg){
        this.msg = msg;
    }

    public byte[] getMsg(){
        return msg;
    }

}
