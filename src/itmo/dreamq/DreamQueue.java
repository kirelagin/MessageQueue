package itmo.dreamq;

import itmo.mq.Message;
import itmo.mq.MessageQueue;
import itmo.mq.Envelope;

import javax.jws.WebService;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebService(endpointInterface = "itmo.mq.MessageQueue")
public class DreamQueue implements MessageQueue {

    private final static int ARRAY_BLOCKING_QUEUE_SIZE = 10000;
    private final static long TIME_OUT = 3000;

    private final static long ONE_SECOND = 1000;

    private static volatile long ticket = 0;

    private static ConcurrentHashMap<Integer, Queue<Message>> messageQueue = new ConcurrentHashMap<Integer, Queue<Message>>();

    private static Map<Long , Envelope> messagePool = new ConcurrentHashMap<Long, Envelope>();
    private static Queue<Ticket> sentMessages = new ConcurrentLinkedQueue<Ticket>();

    static {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("current thread is " + Thread.currentThread().getName());
                try{
                    while (!Thread.currentThread().isInterrupted()) {
                        if (sentMessages.isEmpty()) {
                            Thread.sleep(ONE_SECOND);
                        } else {
                            Ticket t = sentMessages.peek();
                            long difference = t.getExpirationTime() - System.currentTimeMillis();
                            if (difference > 0) {
                                Thread.sleep(difference);
                            } else {
                                sentMessages.remove();
                                Envelope tempTask = messagePool.remove(t.getTicket());
                                if (tempTask != null) {
                                    messageQueue.get(tempTask.getTag()).offer(tempTask.getMsg());
                                }
                            }
                        }
                    }
                } catch (InterruptedException e){
                    System.err.println("timer thread is interrupted");
                }
            }
        }, 1000);
    }

    @Override
    public void ack(long ticketId) {
        messagePool.remove(ticketId);
    }

    @Override
    public void put(int tag, Message m) {
        messageQueue.putIfAbsent(tag, new ArrayBlockingQueue<Message>(ARRAY_BLOCKING_QUEUE_SIZE));
        messageQueue.get(tag).add(m);
    }

    @Override
    public Envelope getAny() {
        Message tempMessage = null;
        int tempTag = 0;
        for (Map.Entry<Integer, Queue<Message>> entry : messageQueue.entrySet()){
            tempMessage = entry.getValue().poll();
            tempTag = entry.getKey();
            if (tempMessage != null){
                break;
            }
        }
        return createEnvelope(tempMessage, tempTag);
    }

    @Override
    public Envelope get(int tag) {
        Message tempMessage = null;
        if (messageQueue.get(tag) != null) {
            tempMessage = messageQueue.get(tag).poll();
        }
        return createEnvelope(tempMessage, tag);
    }

    private Envelope createEnvelope(Message msg, int tag) {
        Envelope envelope;
        if (msg != null) {
            envelope = new Envelope(msg, tag, ticket++);
            Ticket t = new Ticket(envelope.getTicketId(), System.currentTimeMillis() + TIME_OUT);
            messagePool.put(envelope.getTicketId(), envelope);
            sentMessages.add(t);
        } else {
            envelope = new Envelope();
        }
        System.out.println("Ticket " + envelope.getTicketId());
        return envelope;
    }

}
