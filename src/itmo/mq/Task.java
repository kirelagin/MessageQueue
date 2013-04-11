package itmo.mq;

public class Task {

    private byte[] msg;
    private int tag;
    private int ticket;

    public Task(byte[] msg, int tag, int ticket) {
        this.msg = msg;
        this.tag = tag;
        this.ticket = ticket;
    }

    public Task() {
    }

    public byte[] getMsg() {
        return msg;
    }

    public int getTag() {
        return tag;
    }

    public int getTicket() {
        return ticket;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

}
