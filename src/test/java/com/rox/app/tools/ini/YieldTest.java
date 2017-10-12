package com.rox.app.tools.ini;

/**
 * Created by Rox on 2014/11/19.
 */
public class YieldTest {

    public static void main(String[] args) {
        Thread thread = new Thread() {
            public void run() {
                long t0 = System.currentTimeMillis();
                System.out.println("New Thread start....");
                try {
                    join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long t1 = System.currentTimeMillis();
                System.out.println("After yield(): " + (t1-t0));
            }
        };

        thread.start();

        System.out.println("Sleep....");

        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();
        System.out.println("Wake up....");
    }
}
