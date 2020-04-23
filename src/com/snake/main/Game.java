package com.snake.main;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class Game extends JFrame {
    public Game(){
       startGame();
    }

    private void startGame() {
        add(new gameScreen());
        setResizable(false); //Can't change size
        pack();

        setTitle("Snake Game"); //Title of game
        setLocationRelativeTo(null); //Puts application in middle of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Ensures that applications closes when you exit
    }

    public static void main(String[] args){
        EventQueue.invokeLater(() -> {
            JFrame frame = new Game();
            frame.setVisible(true); //Visible
        });
    }

}
