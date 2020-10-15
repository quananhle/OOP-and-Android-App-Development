package com.example.stockwatch;

import com.example.stockwatch.main.MainActivity;

public class ThreadedClass implements Runnable{
    public MainActivity mainActivity;
    public ThreadedClass(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void run(){

    }
    
}
