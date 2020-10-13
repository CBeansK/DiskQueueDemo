package implementation.tests;

import implementation.DiskQueue;

import java.io.File;

public class QueueTester {

    public boolean testQueueInitialization(){

        // set up queue
        DiskQueue queue = new DiskQueue(10);
        queue.enqueue(5);
        queue.enqueue(3);
        queue.enqueue(2);


        Object[] test = new Object[3];
        for(int i = 0; i < test.length; i++){
            test[i] = queue.dequeue();
        }

        int[] test2 = {5, 3, 2};

        // perform deep check to make sure both are equal
        for(int i = 0; i < test.length; i++){
            if ((int) test[i] != test2[i]) return false;
        }
        return true;
    }

    public boolean testStoringQueue(){
        DiskQueue queue = defaultQueue();

        queue.storeQueue("testfile");
        return new File("testfile").exists();
    }

    private DiskQueue defaultQueue(){
        DiskQueue queue = new DiskQueue(10);
        for(int i = 0; i < 10; i++){
            queue.enqueue(i);
        }
        return queue;
    }

    public boolean testRetrievingQueue(){
        DiskQueue queue = defaultQueue();

        DiskQueue queueFromFile = queue.retrieveQueueFromFile("testfile");

        return (queue.equals(queueFromFile));
    }
}
