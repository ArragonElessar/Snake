import javax.swing.JFrame;

/**
 * GamePanel
 */
public class GameFrame extends JFrame {
    GameFrame(){
        this.add(new GamePanel()); //initializes the window panel in the frame this does not include the title bar 
        this.setTitle("Snakes"); //sets title of the current frame in the title bar
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //by default pressing the close Option will only hide the windoew but process still runs this line actually closes the program
        this.setResizable(false); //set resize to false
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
}