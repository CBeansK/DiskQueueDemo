package implementation;

public interface BaseQueue {

    // Returns max amount of items that can be held in memory at a given time (set on creation)
    int getMaxSizeInMemory();

    // Returns total number of items in queue
    int count();

    // Returns total amount of items in memory
    int getSizeInMemory();


    // Returns total amount of items on disk
    int getSizeOnDisk();

    // Adds an object to the queue
    void enqueue(Object value);

    // Removes item from queue and returns it
    Object dequeue();

    // Returns the first item in the queue
    Object peek();
}
