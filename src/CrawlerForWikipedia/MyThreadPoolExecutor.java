package CrawlerForWikipedia;

import java.util.concurrent.*;

class MyThreadPoolExecutor extends ThreadPoolExecutor {
    //ThreadPoolExecutor threadPoolExecutor;
    volatile Integer count = 0;

    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    private class MyThread implements Runnable {

        Runnable command;

        public MyThread(Runnable command) {
            this.command = command;
        }

        @Override
        public void run() {
            command.run();
            synchronized (count) {
                count--;
            }
        }
    }

    @Override
    public void execute(Runnable command) {
        synchronized (count) {
            count++;
        }

        new Thread(new MyThread(command)).start();

    }

    public synchronized int getCount() {
        return count;
    }


}
