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
public class MessageQueueImpl implements MessageQueue {

    private final static int ARRAY_BLOCKING_QUEUE_SIZE = 10000;
    private final static long TIME_OUT = 3000;

    private static volatile long ticket = 0;

    private static Map<Integer, Queue<Message>> messageQueue = new ConcurrentHashMap<Integer, Queue<Message>>();

    private static Map<Long , Envelope> messagePool = new ConcurrentHashMap<Long, Envelope>();
    private static Queue<Ticket> sentMessages = new ConcurrentLinkedQueue<Ticket>();

    static {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                while (true) {
                    if (sentMessages.isEmpty()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("timer is interrupted");
                        }
                    } else {
                        Ticket t = sentMessages.peek();
                        long difference = System.currentTimeMillis() - t.getTimeOut();
                        if (difference < 0) {
                            try {
                                Thread.sleep(Math.abs(difference));
                            } catch (InterruptedException e) {
                                System.err.println("timer is interrupted while waiting for a task to accomplish");
                            }
                        } else {
                            sentMessages.remove();
                            Envelope tempTask = messagePool.remove(t.getTicket());
                            if (tempTask != null) {
                                messageQueue.get(tempTask.getTag()).offer(tempTask.getMsg());
                            }
                        }
                    }
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
        if (!messageQueue.containsKey(tag)) {
            messageQueue.put(tag, new ArrayBlockingQueue<Message>(ARRAY_BLOCKING_QUEUE_SIZE));
        }
        messageQueue.get(tag).offer(m);
    }

    @Override
    public Envelope getAny() {
        Message tempMessage = null;
        int tempTag = 0;
        if (!messageQueue.isEmpty()) {
            for (int i : messageQueue.keySet()) {
                tempMessage = messageQueue.get(i).poll();
                tempTag = i;
                if (tempMessage != null) {
                    break;
                }
            }
        }
        return createTicketAndTask(tempMessage, tempTag);
    }

    @Override
    public Envelope get(int tag) {
        Message tempMessage = null;
        if (messageQueue.get(tag) != null) {
            tempMessage = messageQueue.get(tag).poll();
        }
        return createTicketAndTask(tempMessage, tag);
    }

    private Envelope createTicketAndTask(Message msg, int tag) { // TODO: rename me
        Envelope task;
        if (msg != null) {
            task = new Envelope(msg, tag, ticket++);
            Ticket t = new Ticket(task.getTicketId(), System.currentTimeMillis() + TIME_OUT);
            messagePool.put(task.getTicketId(), task);
            sentMessages.add(t);
        } else {
            task = new Envelope();
        }
        return task;
    }

}
