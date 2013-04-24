package itmo.dreamq;

import itmo.mq.Message;
import itmo.mq.MessageQueue;
import itmo.mq.Envelope;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@WebService(endpointInterface = "itmo.mq.MessageQueue")
public class DreamQueue implements MessageQueue {

    private final static long TIME_OUT = 10000;
    private final static long EXPIRE_DELAY = 1000;

    private AtomicLong ticket;

    private ConcurrentHashMap<Integer, BlockingQueue<Message>> messageQueue;

    private Map<Long, Envelope> messagePool;
    private Queue<Ticket> sentMessages;


    public DreamQueue() {
        ticket = new AtomicLong();
        messageQueue = new ConcurrentHashMap<Integer, BlockingQueue<Message>>();
        messagePool = new ConcurrentHashMap<Long, Envelope>();
        sentMessages = new ConcurrentLinkedQueue<Ticket>();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("current thread is " + Thread.currentThread().getName());
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        if (sentMessages.isEmpty()) {
                            Thread.sleep(EXPIRE_DELAY);
                        } else {
                            Ticket t = sentMessages.peek();
                            long difference = t.getExpirationTime() - System.currentTimeMillis();
                            if (difference > 0) {
                                Thread.sleep(difference);
                            } else {
                                sentMessages.remove();
                                Envelope tempTask = messagePool.remove(t.getTicket());
                                if (tempTask != null) {
                                    messageQueue.get(tempTask.getTag()).add(tempTask.getMsg());
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    System.err.println("timer thread is interrupted");
                }
            }
        }, EXPIRE_DELAY);
    }

    @Override
    public boolean createQueue(int tag){
        if (messageQueue.containsKey(tag)) {
            return false;
        } else {
            BlockingQueue existingQueue = messageQueue.putIfAbsent(tag, new LinkedBlockingQueue<Message>());
            return existingQueue == null;
        }
    }

    @Override
    public void ack(long ticketId) {
        messagePool.remove(ticketId);
    }

    @Override
    public void put(int tag, Message m) {
        createQueue(tag);
        messageQueue.get(tag).add(m);
    }

    @Override
    public Envelope getAny() {
        Message tempMessage = null;
        int tempTag = 0;
        for (Map.Entry<Integer, BlockingQueue<Message>> entry : messageQueue.entrySet()) {
            try {
                tempMessage = entry.getValue().poll(0, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
            tempTag = entry.getKey();
            if (tempMessage != null) {
                break;
            }
        }
        return createEnvelope(tempMessage, tempTag);
    }

    @Override
    public Envelope get(int tag) {
        Message tempMessage = null;
        if (messageQueue.get(tag) != null) {
            try {
                tempMessage = messageQueue.get(tag).poll(0, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
        }
        return createEnvelope(tempMessage, tag);
    }

    private Envelope createEnvelope(Message msg, int tag) {
        Envelope envelope;
        if (msg != null) {
            envelope = new Envelope(msg, tag, ticket.getAndIncrement());
            Ticket t = new Ticket(envelope.getTicketId(), System.currentTimeMillis() + TIME_OUT);
            messagePool.put(envelope.getTicketId(), envelope);
            sentMessages.add(t);
        } else {
            envelope = new Envelope();
        }
        return envelope;
    }

}
