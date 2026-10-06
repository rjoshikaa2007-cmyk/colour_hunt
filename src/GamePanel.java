import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * GamePanel.java
 * The main game screen. It shows the score, level, lives and timer,
 * builds the grid of coloured buttons and handles every click.
 */
public class GamePanel extends JPanel {

    // ----- Game settings (easy to change) -----
    private static final int GAME_SECONDS = 60;   // total time for one game
    private static final int START_LIVES = 3;     // lives at the start
    private static final int SCORE_PER_LEVEL = 5; // score needed to go up a level

    // ----- Game data -----
    private Player player;
    private int score;
    private int level;
    private int lives;
    private int timeLeft;
    private int oddIndex;        // position of the different box (0 .. size*size-1)
    private boolean gameOver;

    // ----- Helpers -----
    private ColourGenerator colourGenerator = new ColourGenerator();
    private Random random = new Random();
    private Timer gameTimer;      // javax.swing.Timer, ticks every second

    // ----- Screen components -----
    private JLabel nameLabel;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JLabel livesLabel;
    private JLabel timerLabel;
    private JLabel messageLabel;
    private JPanel gridPanel;

    public GamePanel(Player player) {
        this.player = player;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(30, 30, 46));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(createTopPanel(), BorderLayout.NORTH);

        // The grid is filled with buttons later, in newRound()
        gridPanel = new JPanel();
        gridPanel.setBackground(new Color(30, 30, 46));
        add(gridPanel, BorderLayout.CENTER);

        // Small label at the bottom for "Correct!" / "Wrong!" messages
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(messageLabel, BorderLayout.SOUTH);

        startGame();
    }

    /** Builds the title and the row of Score / Level / Lives / Timer labels. */
    private JPanel createTopPanel() {
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setBackground(new Color(30, 30, 46));

        JLabel title = new JLabel("COLOUR HUNT", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 34));
        title.setForeground(new Color(250, 179, 135));
        top.add(title, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(1, 5, 8, 0));
        stats.setBackground(new Color(30, 30, 46));

        nameLabel = createStatLabel();
        scoreLabel = createStatLabel();
        levelLabel = createStatLabel();
        livesLabel = createStatLabel();
        timerLabel = createStatLabel();

        stats.add(nameLabel);
        stats.add(scoreLabel);
        stats.add(levelLabel);
        stats.add(livesLabel);
        stats.add(timerLabel);
        top.add(stats, BorderLayout.CENTER);

        return top;
    }

    /** Creates one styled label for the stats bar. */
    private JLabel createStatLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        return label;
    }

    /** Resets everything and starts a fresh game. */
    private void startGame() {
        score = 0;
        level = 1;
        lives = START_LIVES;
        timeLeft = GAME_SECONDS;
        gameOver = false;

        updateLabels();
        showMessage(" ", Color.WHITE);
        newRound();

        // Stop any old timer, then start a new one that ticks every 1000 ms
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                updateLabels();
                if (timeLeft <= 0) {
                    endGame();
                }
            }
        });
        gameTimer.start();
    }

    /** Creates a new colour challenge and rebuilds the grid of buttons. */
    private void newRound() {
        // Level goes up every SCORE_PER_LEVEL points
        int oldLevel = level;
        level = score / SCORE_PER_LEVEL + 1;
        if (level > oldLevel) {
            showMessage("Level Up! Colours are getting closer...", new Color(137, 180, 250));
        }

        int size = ColourGenerator.getGridSize(level);
        colourGenerator.generate(level);

        // Choose a random position for the different box
        oddIndex = random.nextInt(size * size);

        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(size, size, 6, 6));

        for (int i = 0; i < size * size; i++) {
            JButton box = new JButton();
            box.setOpaque(true);
            box.setBorderPainted(false);
            box.setFocusPainted(false);

            // Every box gets the base colour, except the odd one
            if (i == oddIndex) {
                box.setBackground(colourGenerator.getOddColour());
            } else {
                box.setBackground(colourGenerator.getBaseColour());
            }

            // Remember which box this button is
            final int index = i;
            box.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleClick(index);
                }
            });

            gridPanel.add(box);
        }

        updateLabels();
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /** Called whenever the player clicks a box. */
    private void handleClick(int clickedIndex) {
        if (gameOver) {
            return;
        }

        if (clickedIndex == oddIndex) {
            // Correct box
            score++;
            showMessage("Correct! +1", new Color(166, 227, 161));
            newRound();
        } else {
            // Wrong box
            lives--;
            showMessage("Wrong box! -1 life", new Color(243, 139, 168));
            updateLabels();
            if (lives <= 0) {
                endGame();
            }
        }
    }

    /** Stops the game and shows the result. */
    private void endGame() {
        gameOver = true;
        gameTimer.stop();

        int timeTaken = GAME_SECONDS - Math.max(timeLeft, 0);

        String result = "Player: " + player.getName()
                + "\nFinal Score: " + score
                + "\nLevel Reached: " + level
                + "\nTime Taken: " + timeTaken + " seconds";

        String[] options = {"Play Again", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, result, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            startGame();
        } else {
            System.exit(0);
        }
    }

    /** Refreshes the text of all stat labels. */
    private void updateLabels() {
        nameLabel.setText("Player: " + player.getName());
        scoreLabel.setText("Score: " + score);
        levelLabel.setText("Level: " + level);
        livesLabel.setText("Lives: " + lives);
        timerLabel.setText("Time: " + Math.max(timeLeft, 0));

        // Timer turns red in the last 10 seconds
        if (timeLeft <= 10) {
            timerLabel.setForeground(new Color(243, 139, 168));
        } else {
            timerLabel.setForeground(Color.WHITE);
        }
    }

    /** Shows a short message under the grid. */
    private void showMessage(String text, Color colour) {
        messageLabel.setText(text);
        messageLabel.setForeground(colour);
    }
}