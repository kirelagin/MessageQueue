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
        // FIXME: remove this constructor
        msg = null;
        ticketId = 0;
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
