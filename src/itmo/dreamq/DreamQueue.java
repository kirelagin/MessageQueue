package itmo.dreamq;

import itmo.mq.MessageQueue;
import itmo.mq.Message;
import itmo.mq.Task;

import javax.jws.WebService;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


@WebService
public class DreamQueue implements MessageQueue {

    private final static int ARRAY_BLOCKING_QUEUE_SIZE = 10000;

    private Map<Integer, BlockingQueue<Message>> messageQueue = new ConcurrentHashMap<Integer, BlockingQueue<Message>>();

    private Map<Integer, Message> messagePool = new HashMap<Integer, Message>();
    private List<Integer> sentMessages = new LinkedList<Integer>();


    @Override
    public void ack(int id) {
        messagePool.remove(id);
    }

    @Override
    public void put(int tag, Message m) {
        if (!messageQueue.containsKey(tag)){
            messageQueue.put(tag, new ArrayBlockingQueue<Message>(ARRAY_BLOCKING_QUEUE_SIZE));
        }
        try {
            messageQueue.get(tag).put(m);
        } catch (InterruptedException e) {
            System.err.println("Can not put a message");
        }
    }

    @Override
    public Task get() {
        return null;
    }

    @Override
    public Task get(int tag) {
        return null;
    }

}
