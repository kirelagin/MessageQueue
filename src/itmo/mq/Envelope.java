package itmo.mq;

public class Envelope {

    private Message msg;
    private int tag;
    private long ticketId;

    public Envelope(Message msg, int tag, long ticketId) {
        this.msg = msg;
        this.tag = tag;
        this.ticketId = ticketId;
    }

    public Envelope() {
        msg = null;
        ticketId = 0;
        tag = 0;
    }

    public Message getMsg() {
        return msg;
    }

    public int getTag() {
        return tag;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public void setTag(int t) {
        tag = t;
    }

    public void setTicketId(long t) {
        ticketId = t;
    }

}
