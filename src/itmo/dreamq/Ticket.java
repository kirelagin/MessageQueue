package itmo.dreamq;

public class Ticket {

    private long ticket;
    private long timeOut;

    public Ticket(long ticket, long timeOut) {
        this.ticket = ticket;
        this.timeOut = timeOut;
    }

    public long getTicket() {
        return ticket;
    }

    public long getTimeOut() {
        return timeOut;
    }

}
