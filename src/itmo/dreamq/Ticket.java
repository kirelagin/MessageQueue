package itmo.dreamq;

public class Ticket {

    private long ticket;
    private long expirationTime;

    public Ticket(long ticket, long expirationTime) {
        this.ticket = ticket;
        this.expirationTime = expirationTime;
    }

    public long getTicket() {
        return ticket;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

}
