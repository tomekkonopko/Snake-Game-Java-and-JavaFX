package com.company;

public class Time extends Thread {
    int time=0;
    public void run()
    {
        System.out.println("Zaczynamy liczyc czas");
        while (true) {
            System.out.println(time);
            time++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
