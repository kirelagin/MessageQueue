package itmo.mq;

public class Ticket {

    private int ticket;
    private long timeOut;

    public Ticket(int ticket, long timeOut) {
        this.ticket = ticket;
        this.timeOut = timeOut;
    }

    public int getTicket() {
        return ticket;
    }

    public long getTimeOut() {
        return timeOut;
    }

}
