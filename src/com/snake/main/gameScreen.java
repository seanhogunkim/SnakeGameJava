package com.snake.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class gameScreen extends JPanel implements ActionListener {

    private final int SCREEN_WIDTH = 300;
    private final int SCREEN_HEIGHT = 300;
    private final int DOT_ICON_SIZE = 20;
    private final int DOT_ICONS = 225;
    private final int RAND_POS = 14; //(300 divided by 20) - 1
    private final int DELAY = 140;

    private final int x[] = new int[DOT_ICONS];
    private final int y[] = new int[DOT_ICONS];

    private int snake_dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection;
    private boolean rightDirection;
    private boolean upDirection;
    private boolean downDirection;
    private boolean inSnakeGame;

    private Timer timer;
    private Image snake_body;
    private Image apple;
    private Image snake_head;

    public gameScreen() {
        createScreen();
    }

    private void createScreen() {
        addKeyListener(new GameKeyAdapter());
        setBackground(Color.black);
        setFocusable(true);
        initialConditions(); //Sets initial conditions of game

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); //Sets size of the JFrame
        loadIcons(); //Method that puts snake and apple icons
        initSnakeGame(); //Spawn entities
    }

    private void initialConditions() { //Setting initial conditions of game
        inSnakeGame = true; //Set the game state to true so it can start
        leftDirection = false;
        rightDirection = true; //Face right so snake doesn't accidentally start going left and instant die over and over
        upDirection = false;
        downDirection = false;
    }

    private void loadIcons() { //Loads the pictures in for the snake and apple

        ImageIcon iisnakepart = new ImageIcon("resources/snakebody.png");
        snake_body = iisnakepart.getImage();

        ImageIcon iiapple = new ImageIcon("resources/appel.png");
        apple = iiapple.getImage();

        ImageIcon iisnakehead = new ImageIcon("resources/snakehead.png");
        snake_head = iisnakehead.getImage();
    }

    private void initSnakeGame() { //This method spawns the snake and the apple
        snake_dots = 5;
        for (int z = 0; z < snake_dots; z++) { //Starting position of game
            x[z] = 100 - z * 20;
            y[z] = 60;
        }
        spawnApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    private void drawGame(Graphics g) {
        if (inSnakeGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int z = 0; z < snake_dots; z++) {
                if (z == 0) {
                    g.drawImage(snake_head, x[z], y[z], this);
                } else {
                    g.drawImage(snake_body, x[z], y[z], this);
                }
            }
//            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g); //When inSnakeGame = false ie. death
        }
    }

    private void gameOver(Graphics g) { //Creates the game over screen when you die
        String[] deathMessages = new String[5];
        deathMessages[0] = "GG NOOB";
        deathMessages[1] = "REKT";
        deathMessages[2] = "?????";
        deathMessages[3] = "u is ded";
        deathMessages[4] = ":(";
        int rand = (int) (Math.random() * 5);
        Font font = new Font("Comic sans" ,Font.BOLD, 24);
        FontMetrics metric = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(deathMessages[rand], (SCREEN_WIDTH - metric.stringWidth(deathMessages[rand])) / 2, 40);
        addRestartButton();
    }

    private void addRestartButton() { //This method creates the restart game button when you die
        JButton restartButton = new JButton();
        restartButton.setIcon(new ImageIcon("resources/rewindtime.jpg"));
        setLayout(null);
        restartButton.setBounds(4,70,292, 230);
        add(restartButton);

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createScreen(); //Reset the game
                restartButton.setVisible(false); //Makes the button invisible again after you press it as it is in the way
            }
        });
    }

    private void findCollision() { //Checks for a collision with itself or wall

        for (int z = snake_dots; z > 0; z--) {

            if ((z > 1) && (x[0] == x[z]) && (y[0] == y[z])) {
                inSnakeGame = false;
            }
        }
        if (y[0] >= SCREEN_HEIGHT) {
            inSnakeGame = false;
        }
        if (y[0] < 0) {
            inSnakeGame = false;
        }
        if (x[0] >= SCREEN_WIDTH) {
            inSnakeGame = false;
        }
        if (x[0] < 0) {
            inSnakeGame = false;
        }
        if (!inSnakeGame) {
            timer.stop();
        }
    }

    private void checkApple() { //Checks if the apple has been eaten
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            snake_dots++;
            spawnApple();
        }
    }

    private void spawnApple() { //Spawns the apple in a random position
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_ICON_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_ICON_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent event) { //Checks what happens each tick
        if (inSnakeGame) {
            checkApple();
            findCollision();
            move();
        }
        repaint();
    }

    private void move() { //Moves the snake
        for (int z = snake_dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (leftDirection) {
            x[0] -= DOT_ICON_SIZE;
        }
        if (rightDirection) {
            x[0] += DOT_ICON_SIZE;
        }
        if (upDirection) {
            y[0] -= DOT_ICON_SIZE;
        }
        if (downDirection) {
            y[0] += DOT_ICON_SIZE;
        }
    }

    private class GameKeyAdapter extends KeyAdapter { //Sets actions for corresponding inputs

        @Override
        public void keyPressed(KeyEvent event) {
            int key = event.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}