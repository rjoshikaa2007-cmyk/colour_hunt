import javax.swing.*;

/**
 * Main.java
 * Entry point of the application.
 * Phase 1: asks for a name, then opens the game directly.
 */
public class Main {

    public static void main(String[] args) {
        // The cross-platform look and feel makes sure button colours
        // show correctly on every operating system (especially macOS).
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Swing programs should be started on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String name = JOptionPane.showInputDialog(null, "Enter your name:",
                        "Colour Hunt", JOptionPane.QUESTION_MESSAGE);

                // If the user cancels or leaves it empty, use "Guest"
                if (name == null || name.trim().isEmpty()) {
                    name = "Guest";
                }

                Player player = new Player(name.trim());
                new GameFrame(player).setVisible(true);
            }
        });
    }
} 