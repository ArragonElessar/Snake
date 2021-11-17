import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener {
    // defining some constants
    // max height, max width set the size of game window
    // tile size defines the side of a square tile
    // delay (ms) in which the frame updates
    
    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    public static int max_height, max_width, tile_size;
    //public static final int max_height = 800, max_width = 800, tile_size = 25, DELAY = 100;
    public static int delay = 100;
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
        max_height = (int)(size.getHeight()/2);
        max_width = max_height;
        tile_size = (int)(max_height * 0.03125);

        // set the size of JComponent to max_width and max height
        this.setPreferredSize(new Dimension(max_width, max_height));

        // set bg color to black
        this.setBackground(Color.BLACK);

        // set focusable property and add a keyListener to the component
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());

        // instantiate random
        random = new Random();

        // start a game
        startGame(1);

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
            timer = new Timer(delay, this);
            timer.start();

            // generate new snake
            snakes[0] = new Snake(0, 0);
        }

        // for more than 1 players
        else if (i > 1) {
            // loop through all snakes
            for (int j = 0; j < i; j++) {
                // generate snake head on random square inside our game surface
                snakes[j] = new Snake(random.nextInt((int) (max_height / tile_size)) * tile_size,
                        random.nextInt((int) (max_width / tile_size)) * tile_size);
            }
            // set player number
            playerNumber = i;
            // initialise and start timer
            timer = new Timer(delay, this);
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
        fruitPosY = random.nextInt((int) (max_height / tile_size)) * tile_size;
        fruitPosX = random.nextInt((int) (max_width / tile_size)) * tile_size;
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
        for (int i = 0; i < (max_width / tile_size); i++) {
            g.drawLine(i * tile_size, 0, i * tile_size, max_height);
        }
        for (int i = 0; i < (max_height / tile_size); i++) {
            g.drawLine(0, i * tile_size, max_height, i * tile_size);
        }

        // this for loop is for drawing all the snakes on the screen
        
        // for loop for iterating through all the snakes (players)
        for (int j = 0; j < playerNumber; j++) {
            // for loop to iterate through every point of a snake
            for (int i = 0; i < snakes[j].getLength(); i++) {
                
                // i = 0 --> this is the snake's head (red color)
                if (i == 0) {
                    g.setColor(Color.red);
                    g.fillRect(snakes[j].xPos[i], snakes[j].yPos[i], tile_size, tile_size);
                } 
                // i != 0 --> this is the snake's body (green color)
                else {
                    g.setColor(Color.green);
                    g.fillRect(snakes[j].xPos[i], snakes[j].yPos[i], tile_size, tile_size);
                }

            }
        }
        // food is blue colored
        g.setColor(Color.blue);
        g.fillOval(fruitPosX, fruitPosY, tile_size, tile_size);
    }

    // implements actionPerformed method from ActionListener class
    public void actionPerformed(ActionEvent e) {
        // for every action event, all snakes have to be moved
        this.move();

        // the for loops check if any snakes have self collided
        for (int i = 0; i < playerNumber; i++) {
            for (int j = 0; j < playerNumber; j++) {
                snakes[i].selfCollision(snakes[j]);
            }
        }

        // checks if any snake has eaten a fruit. If yes, a new fruit is spawned
        for (int i = 0; i < playerNumber; i++) {
            if (snakes[i].checkFood(fruitPosX, fruitPosY)) {
                this.spawnFruit();
            }

        }
        // repaints the screen, so that the updated frame is displayed
        repaint();
    }

    // inner class to handle all keyboard inputs
    public class myKeyAdapter extends KeyAdapter {

        // implements keyPressed method from KeyAdapter class
        @Override
        public void keyPressed(KeyEvent e) {
            // key event e is the event of a keyboard key being pressed
            switch (e.getKeyCode()) {
                // get keycode fetches what key has been pressed from the user keyboard
                // the switch case sets the new direction according to the keypress
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

            // same piee of code for the second player
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
