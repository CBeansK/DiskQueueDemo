package implementation.driver;

import implementation.tests.QueueTester;

public class Main {
    public static void main(String[] args){
        QueueTester tester = new QueueTester();
        assert tester.testQueueInitialization() : "Queue initialization does not work as intended.";
        assert tester.testStoringQueue() : "Failed to store queue";
        assert tester.testRetrievingQueue() : "Failed to retrieve the same queue from file";
    }
}
