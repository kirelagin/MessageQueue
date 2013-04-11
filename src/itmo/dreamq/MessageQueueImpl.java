package itmo.dreamq;

import itmo.mq.Message;
import itmo.mq.MessageQueue;
import itmo.mq.Task;
import itmo.mq.Ticket;

import javax.jws.WebService;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebService(endpointInterface = "itmo.mq.MessageQueue")
public class MessageQueueImpl implements MessageQueue {

    private final static int ARRAY_BLOCKING_QUEUE_SIZE = 10000;
    private final static long TIME_OUT = 3000;

    private static volatile int ticket = 0;

    private static Map<Integer, Queue<Message>> messageQueue = new ConcurrentHashMap<Integer, Queue<Message>>();

    private static Map<Integer, Task> messagePool = new ConcurrentHashMap<Integer, Task>();
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
                            Task tempTask = messagePool.remove(t.getTicket());
                            if (tempTask != null) {
                                messageQueue.get(tempTask.getTag()).offer(new Message(tempTask.getMsg()));
                            }
                        }
                    }
                }
            }
        }, 1000);
    }

    @Override
    public void ack(int id) {
        messagePool.remove(id);
    }

    @Override
    public void put(int tag, Message m) {
        if (!messageQueue.containsKey(tag)) {
            messageQueue.put(tag, new ArrayBlockingQueue<Message>(ARRAY_BLOCKING_QUEUE_SIZE));
        }
        messageQueue.get(tag).offer(m);
    }

    @Override
    public Task get() {
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
    public Task get(int tag) {
        Message tempMessage = null;
        if (messageQueue.get(tag) != null) {
            tempMessage = messageQueue.get(tag).poll();
        }
        return createTicketAndTask(tempMessage, tag);
    }

    private Task createTicketAndTask(Message msg, int tag) {
        Task task;
        if (msg != null) {
            task = new Task(msg.getMsg(), tag, ticket++);
            Ticket t = new Ticket(task.getTicket(), System.currentTimeMillis() + TIME_OUT);
            messagePool.put(task.getTicket(), task);
            sentMessages.add(t);
        } else {
            task = new Task();
        }
        return task;
    }

}
