package com.company;

import javax.swing.*;

public class Snake
{
    int x,y;
    Snake(int x, int y)
    {
        JFrame snake=new JFrame();
        this.x=x;
        this.y=y;
        x=x*10;
        y=y*10;
        System.out.println(x+" "+y);
        snake.setTitle("Snake");
        snake.setSize(x,y);
        snake.setLocationRelativeTo(null);
        snake.setVisible(true);
    }

}
