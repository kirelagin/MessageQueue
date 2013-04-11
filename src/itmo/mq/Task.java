package itmo.mq;


public class Task {

    private Message msg;
    private int tag;
    private int ticket;

    public Task(Message msg, int tag, int ticket){
        this.msg = msg;
        this.tag = tag;
        this.ticket = ticket;
    }

    public Message getMsg(){
        return msg;
    }

    public int getTag(){
        return tag;
    }

    public int getTicket(){
        return ticket;
    }

}
