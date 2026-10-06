import javax.swing.*;

/**
 * GameFrame.java
 * The window for the game. It simply holds a GamePanel.
 */
public class GameFrame extends JFrame {

    public GameFrame(Player player) {
        setTitle("Colour Hunt");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 720);
        setLocationRelativeTo(null);   // centre on screen

        add(new GamePanel(player));
    }
}