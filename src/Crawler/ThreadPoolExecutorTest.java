package Crawler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class Summer {
}

public class ThreadPoolExecutorTest {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        System.out.println("Test");
    }
}
