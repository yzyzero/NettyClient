package com.xyd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTest {
    public static void main(String[] args) {
         //创建一个只有一个线程的线程池
         ExecutorService executorService = Executors.newSingleThreadExecutor();
         //创建任务，并提交任务到线程池中
         executorService.execute(new MyRunable("任务1"));
         executorService.execute(new MyRunable("任务2"));
         executorService.execute(new MyRunable("任务3"));
         
         executorService.shutdown();
    }
}

class MyRunable implements Runnable{

    private String taskName;

    public MyRunable(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        System.out.println("线程池完成任务："+taskName);
    }
}