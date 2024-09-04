import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    int boardWith;
    int boardHeight;
    int tileSize = 25;
    // Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    // Food
    Tile food;
    Random random;

    // Game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    public SnakeGame(int boardWith, int boardHeight) {
        this.boardWith = boardWith;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWith, this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);// we want SnakeGame to be the one listening for key presses

        snakeHead = new Tile(5, 5);// default starting place
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();

    }

    private void resetGame() {
        snakeHead = new Tile(5, 5); // reset starting place
        snakeBody.clear(); // clear the snake body
        placeFood();

        velocityX = 0;
        velocityY = 0;
        gameOver = false;

        gameLoop.start(); // restart the game loop
        repaint(); // repaint the screen
    }

    public void paintComponent(Graphics g) {// Graphics-> used for drawing
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid
//        for (int i = 0; i < boardWith / tileSize; i++) {
//            // (x1, y1, x2, y2)
//            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight); // vertical line
//            g.drawLine(0, i * tileSize, boardWith, i * tileSize); // horizontal line
//        }

        // Food
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        // Snake Head
        g.setColor(Color.cyan);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Snake Body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        // Score and Game Over text
        g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over, press SPACE for restart!", tileSize - 16, tileSize);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize + 20);
        } else {
            g.setColor(Color.green);
            g.drawString("For restart, press SPACE.", tileSize - 16, tileSize);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize + 20);
        }
    }

    private void placeFood() {
        food.x = random.nextInt(boardWith / tileSize); // 600/25 = 24
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    // every every 100 millisecond actionPerformed method called, it repaint,
    // it means it calls draw() method in the above over and over again
    @Override
    public void actionPerformed(ActionEvent e) {
        move();// update the X and Y position of the snake
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    public void move() {
        // eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        // Snake Body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {// make snake body move along with the head
            Tile snakePart = snakeBody.get(i);
            if (i == 0) { // if this is first member of snake body, comes right after the snake head
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {// x and y position of the body part that is before it
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Snake Head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            // collide with the snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWith ||
                snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getExtendedKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getExtendedKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getExtendedKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getExtendedKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        } else if (e.getExtendedKeyCode() == KeyEvent.VK_SPACE) {
            resetGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
