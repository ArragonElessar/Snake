import java.util.Random;

public class Snake {
    // a snake is a collection of points

    // initial length of a snake
    private int length = 1;

    // integer arrays for storing positions(x,y) of each part of a snake
    public int[] xPos = new int[40];
    public int[] yPos = new int[40];

    // random number generator
    Random random = new Random();

    // initial direction of the snake is toward Right
    private Character direction = 'R';

    // constructor specifying initial position of snake
    Snake(int a, int b) {
        xPos[0] = a;
        yPos[0] = b;
    }

    // move function
    public void move() {
        // for loop to iterate through every point in a snake
        for (int i = length; i > 0; i--) {
            // move the snake forward by one step, copying each point to its next one
            xPos[i] = xPos[i - 1];
            yPos[i] = yPos[i - 1];

            // conditions for checking if snake has exited any boundaries
            // if yes, snake will reappear from the other side of the game surface
            if (xPos[0] > GamePanel.max_width - GamePanel.tile_size) {
                // exited from right
                xPos[0] = 0;

            }
            if (xPos[0] < 0) {
                // exited from left
                xPos[0] = (GamePanel.max_width - GamePanel.tile_size);

            }
            if (yPos[0] > GamePanel.max_height - GamePanel.tile_size) {
                // exited from bottom
                yPos[0] = 0;
            }
            if (yPos[0] < 0) {
                // exited from top
                yPos[0] = (GamePanel.max_height - GamePanel.tile_size);
            }
            System.out.println(xPos[0] + " " + yPos[0]);

        }
        // detecting change in direction and making the snake move in new direction
        switch (this.direction) {
        case 'R':
            // right
            xPos[0] += GamePanel.tile_size;
            break;
        case 'L':
            // left
            xPos[0] -= GamePanel.tile_size;
            break;
        case 'U':
            // up
            yPos[0] -= GamePanel.tile_size;
            break;
        case 'D':
            // down
            yPos[0] += GamePanel.tile_size;
            break;
        default:
            break;
        }
    }

    // this function checks if the snake has eaten itself
    public void selfCollision(Snake snake) {
        for (int i = 1; i < this.length; i++) {
            // loops through snake length
            if (snake.xPos[0] == xPos[i] && snake.yPos[0] == yPos[i]) {
                // checks if head of snake is at the same position as some other point of the
                // snake
                // generate a better response
                System.out.println("collided self");
                this.setLength(i);
            }
        }
    }

    // sets length of snake
    private void setLength(int i) {
        this.length = i;
    }

    // checks if snake has eaten a food point
    public boolean checkFood(int a, int b) {
        // given (a,b) as the position of food, return true if head matches, else false
        if (xPos[0] == a && yPos[0] == b) {
            this.length++;
            return true;
        }
        return false;

    }

    // gets current length of snake
    public int getLength() {
        return length;
    }

    public void setDirection(char a) {
        // sets the direction of movement for the snake
        if (!((a == 'L' && this.direction == 'R') || (a == 'R' && this.direction == 'L')
                || (a == 'U' && this.direction == 'D') || (a == 'D' && this.direction == 'U'))) {
            // these conditions check for the cases, where snake cannot make 180 degree turns, such as right to left
            // up to dowm, left to right and down to up
            direction = a;
        }
    }

}
