package itmo.mq;

public class Envelope {

    public Message msg;
    public int tag;
    public long ticketId;

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

}
