package server;

/**
 * @author Vladislav Kononov vincent@yandex-team.ru
 */
public class QueueMessage {

    private byte[] data;

    public QueueMessage(){
        data = null;
    }

    public QueueMessage(byte[] data){
        this.data = data;
    }

    public void setData(byte[] data){
        this.data = data;
    }

    public byte[] getData(){
        return data;
    }
}
