package implementation;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class DiskQueue implements BaseQueue, Serializable {

    // Node class used for storing data
    private class Node implements Serializable {
        private final Object value;
        private Node next;

        public Node(Object value){
            this.value = value;
        }

        public Object getValue(){
            return this.value;
        }

        public boolean equals(Node other){
             return (this.value == other.value && this.next == other.next);
        }
    }

    private final int maxSizeInMemory;
    private Node head;
    private Node tail;

    public DiskQueue(int maxMemSize){
        this.maxSizeInMemory = maxMemSize;
    }

    @Override
    public int getMaxSizeInMemory() {
        return this.maxSizeInMemory;
    }

    // need to return the total amt of EXISTING items
    @Override
    public int count() {
        return this.getSizeInMemory() + this.getSizeOnDisk();
    }

    // returns the total amt of items CURRENTLY in memory
    @Override
    public int getSizeInMemory() {
        return this.maxSizeInMemory;
    }

    // returns the total amt of items currently on disk
    // TODO: implement
    @Override
    public int getSizeOnDisk() {
        return 0;
    }

    /*
        TODO:
        store elements on disk
        pull queue from disk
        append 2 queues
     */

    public void storeQueue(String filename) {
        ObjectOutputStream oos = establishOutputStream(filename);

        // validate output stream
        if (oos == null){
            System.out.println("Failed to establish output stream.");
            return;
        }

        try{

            // make a new copy of our queue and store it
            DiskQueue temp = copyQueue();
            oos.writeObject(temp);
            oos.flush();
            oos.close();

        } catch(NotSerializableException e){
            System.out.println("Error. The object you are trying to write " +
                    "does not implement the Serializable interface.");
            e.printStackTrace();
        } catch(IOException e){
            System.out.println("Unknown IO exception occurred.");
            e.printStackTrace();
        }
    }

    public DiskQueue retrieveQueueFromFile(String filename){
        ObjectInputStream inputStream = establishInputStream(filename);

        if (inputStream == null){
            System.out.println("Failed to establish input stream.");
            return null;
        }

        try {
            DiskQueue queue = (DiskQueue) inputStream.readObject();
            inputStream.close();
            return queue;
        } catch (ClassNotFoundException e){
            System.out.println("The class you are trying to obtain does not exist in the file.");
        } catch (IOException e){
            System.out.println("Unknown IO exception occurred.");
        }
        return null;
    }

    private ObjectInputStream establishInputStream(String filename) {
        try {
            return new ObjectInputStream(new FileInputStream(filename));

        } catch(FileNotFoundException e){
            System.out.println("Could not find storage file.");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Unknown IO exception occurred.");
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }

    @NotNull
    private DiskQueue copyQueue(){
        DiskQueue newQueue = new DiskQueue(this.maxSizeInMemory);

        Node current = this.head;
        while(current != null){
            newQueue.enqueue(current);
            current = current.next;
        }

        return newQueue;
    }

    private ObjectOutputStream establishOutputStream(String filename){

        try {
            return new ObjectOutputStream(new FileOutputStream(filename));
        } catch (FileNotFoundException e){
            System.out.printf("File %s not found or able to access." +
                    " Check directory to make sure file exists and is accessible", filename);
        } catch (IOException e){
            System.out.println("Error occurred while writing stream header");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void enqueue(Object value) {
        Node node = new Node(value);

        // if list is empty, then initialize as head and tail
        if (this.tail == null || this.head == null){
            this.head = node;
            this.tail = node;
            return;
        }

        // append item to queue
        this.tail.next = node;
        this.tail = node;
    }

    // Removes an item from the queue and returns it
    @Override
    public Object dequeue() {
        Node head = this.head;

        // if we are removing the last element, make sure we remove the tail reference too
        if (this.head == this.tail){
            this.tail = null;
        }

        this.head = this.head.next;

        return head.getValue();
    }

    public boolean equals(DiskQueue other){
        Node thisCurrent = this.head;
        Node otherCurrent = other.head;

        while(thisCurrent != null && otherCurrent != null){
            if (!thisCurrent.equals(otherCurrent)) return false;

            thisCurrent = thisCurrent.next;
            otherCurrent = otherCurrent.next;
        }
        return true;
    }

    // Returns the first item (front / head) of the queue
    @Override
    public Object peek() {
        return this.head.getValue();
    }
}
