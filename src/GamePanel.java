import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {
    // defining some constants
    // max height, max width set the size of game window
    // tile size defines the side of a square tile
    // delay (ms) in which the frame updates
    public static final int MAX_HEIGHT = 800, MAX_WIDTH = 800, TILE_SIZE = 25, DELAY = 100;

    // currently unused
    // private static int tileNumber = MAX_HEIGHT * MAX_WIDTH / TILE_SIZE;

    // boolean variable to show game is running or not
    private boolean running = false;

    // Timer object to for the usage of time
    private Timer timer;

    // random object
    Random random;

    // preset direction
    protected char direction = 'R';

    // array of snakes, during the start
    Snake[] snakes = new Snake[2];

    // positions for fruits
    private int fruitPosX, fruitPosY;

    // integer to store player number (as two players can currently play the game)
    public int playerNumber;

    GamePanel() {
        // set the size of JComponent to max_width and max height
        this.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));

        // set bg color to black
        this.setBackground(Color.BLACK);

        // set focusable property and add a keyListener to the component
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());

        // instantiate random
        random = new Random();

        // start a game
        startGame(2);

    }

    // function to start a game, parameter i -> number of players
    public void startGame(int i) {
        // set boolean game running to true
        this.running = true;
        // spawn the first fruit
        spawnFruit();

        // if i == 1, i.e single player game
        if (i == 1) {
            // assign player number
            playerNumber = 1;
            // initialse and start timer
            timer = new Timer(DELAY, this);
            timer.start();

            // generate new snake
            snakes[0] = new Snake(0, 0);
        }

        // for more than 1 players
        else if (i > 1) {
            // loop through all snakes
            for (int j = 0; j < i; j++) {
                // generate snake head on random square inside our game surface
                snakes[j] = new Snake(random.nextInt((int) (MAX_HEIGHT / TILE_SIZE)) * TILE_SIZE,
                        random.nextInt((int) (MAX_WIDTH / TILE_SIZE)) * TILE_SIZE);
            }
            // set player number
            playerNumber = i;
            // initialise and start timer
            timer = new Timer(DELAY, this);
            timer.start();
        }
    }

    // moves all snakes
    public void move() {
        // loop through all the snakes
        for (int j = 0; j < playerNumber; j++) {
            // move each snake
            snakes[j].move();
        }
    }

    // function to randomly generate fruits in the game
    public void spawnFruit() {
        // set a random (x,y) within game boundaries
        fruitPosY = random.nextInt((int) (MAX_HEIGHT / TILE_SIZE)) * TILE_SIZE;
        fruitPosX = random.nextInt((int) (MAX_WIDTH / TILE_SIZE)) * TILE_SIZE;
    }

    // Swing part below
    // function to paint graphic component on screen
    public void paintComponent(Graphics g) {
        // paint graphic component from the super class (JPanel)
        super.paintComponent(g);

        // call the draw function
        draw(g);
    }

    // the actual function which puts things on the screen
    public void draw(Graphics g) {
        
        // the below two for loops make the grid lines
        for (int i = 0; i < (MAX_WIDTH / TILE_SIZE); i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, MAX_HEIGHT);
        }
        for (int i = 0; i < (MAX_HEIGHT / TILE_SIZE); i++) {
            g.drawLine(0, i * TILE_SIZE, MAX_HEIGHT, i * TILE_SIZE);
        }

        // this for loop is for drawing all the snakes on the screen
        
        // for loop for iterating through all the snakes (players)
        for (int j = 0; j < playerNumber; j++) {
            // for loop to iterate through every point of a snake
            for (int i = 0; i < snakes[j].getLength(); i++) {
                
                // i = 0 --> this is the snake's head (red color)
                if (i == 0) {
                    g.setColor(Color.red);
                    g.fillRect(snakes[j].xPos[i], snakes[j].yPos[i], TILE_SIZE, TILE_SIZE);
                } 
                // i != 0 --> this is the snake's body (green color)
                else {
                    g.setColor(Color.green);
                    g.fillRect(snakes[j].xPos[i], snakes[j].yPos[i], TILE_SIZE, TILE_SIZE);
                }

            }
        }
        // food is blue colored
        g.setColor(Color.blue);
        g.fillOval(fruitPosX, fruitPosY, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.move();
        for (int i = 0; i < playerNumber; i++) {
            for (int j = 0; j < playerNumber; j++) {
                snakes[i].selfCollision(snakes[j]);
            }
        }

        for (int i = 0; i < playerNumber; i++) {
            if (snakes[i].checkFood(fruitPosX, fruitPosY)) {
                this.spawnFruit();
            }

        }
        repaint();
    }

    public class myKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                snakes[0].setDirection('L');
                break;
            case KeyEvent.VK_RIGHT:
                snakes[0].setDirection('R');
                break;
            case KeyEvent.VK_UP:
                snakes[0].setDirection('U');
                break;
            case KeyEvent.VK_DOWN:
                snakes[0].setDirection('D');
                break;

            default:
                break;
            }
            switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                snakes[1].setDirection('L');
                break;
            case KeyEvent.VK_D:
                snakes[1].setDirection('R');
                break;
            case KeyEvent.VK_W:
                snakes[1].setDirection('U');
                break;
            case KeyEvent.VK_S:
                snakes[1].setDirection('D');
                break;

            default:
                break;
            }

        }
    }

}
