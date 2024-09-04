import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = 600;
        JFrame jFrame=new JFrame("Snake Game");
        jFrame.setVisible(true);
        jFrame.setSize(boardWidth, boardHeight);
        jFrame.setLocationRelativeTo(null); // window opens at the center of the screen
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// program will terminate when user clicks x button on the window

        SnakeGame snakeGame=new SnakeGame(boardWidth, boardHeight);
        jFrame.add(snakeGame);
        jFrame.pack();

        snakeGame.requestFocus();
    }
}