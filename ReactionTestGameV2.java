import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ReactionTestGameV2 extends JFrame {
    private JButton[] buttons;
    private int gridSize;
    private int currentTarget;
    private long startTime;
    private boolean gameStarted;
    private JLabel timerLabel;
    private Timer timer;

    public ReactionTestGameV2(int gridSize) {
        this.gridSize = gridSize;
        buttons = new JButton[gridSize * gridSize];
        currentTarget = 1;
        gameStarted = false;

        setTitle("Reaction Test Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 600));
        setLayout(new BorderLayout());

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(gridSize, gridSize));
        for (int i = 0; i < gridSize * gridSize; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 24));
            buttons[i].addActionListener(new ButtonClickListener());
            gamePanel.add(buttons[i]);
        }
        add(gamePanel, BorderLayout.CENTER);

        timerLabel = new JLabel("Used Time: 0.0 seconds");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timerLabel, BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);

        timer = new Timer(100, new TimerListener());

        startGame();
    }

    private void startGame() {
        resetButtons();
        generateTargetOrder();
        enableButtons();
        timer.stop();
        timerLabel.setText("Used Time: 0.0 seconds");

        JOptionPane.showMessageDialog(this, "Click the buttons in numerical order to start the game.");

        gameStarted = false;
        currentTarget = 1;
    }

    private void resetButtons() {
        for (JButton button : buttons) {
            button.setText("");
        }
    }

    private void generateTargetOrder() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= gridSize * gridSize; i++) {
            numbers.add(i);
        }
        java.util.Collections.shuffle(numbers);
        for (int i = 0; i < gridSize * gridSize; i++) {
            buttons[i].setText(String.valueOf(numbers.get(i)));
        }
    }

    private void enableButtons() {
        for (JButton button : buttons) {
            button.setEnabled(true);
        }
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            int clickedNumber = Integer.parseInt(clickedButton.getText());

            if (!gameStarted) {
                startTime = System.currentTimeMillis();
                timer.start();
                gameStarted = true;
            }

            if (clickedNumber == currentTarget) {
                clickedButton.setEnabled(false);
                currentTarget++;

                if (currentTarget > gridSize * gridSize) {
                    timer.stop();
                    long endTime = System.currentTimeMillis();
                    double usedTime = (endTime - startTime) / 1000.0;
                    JOptionPane.showMessageDialog(ReactionTestGameV2.this,
                            "Congratulations! You completed the game in " + usedTime + " seconds.");

                    for (JButton button : buttons) {
                        button.setEnabled(false);
                    }

                    int option = JOptionPane.showConfirmDialog(ReactionTestGameV2.this,
                            "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        startGame();
                    } else {
                        dispose();
                    }
                }
            }
        }
    }

    private class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            long currentTime = System.currentTimeMillis();
            double elapsedTime = (currentTime - startTime) / 1000.0;
            timerLabel.setText("Used Time: " + String.format("%.1f", elapsedTime) + " seconds");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int gridSize = selectDifficulty();
            ReactionTestGameV2 game = new ReactionTestGameV2(gridSize);
            game.setVisible(true);
        });
    }

    private static int selectDifficulty() {
        String[] options = {"Easy (3x3)", "Medium (4x4)", "Hard (5x5)"};
        int choice = JOptionPane.showOptionDialog(null,
                "Select difficulty level:", "Reaction Test Game", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                return 3;
            case 1:
                return 4;
            case 2:
                return 5;
            default:
                return 3; // Default to easy difficulty
        }
    }
}
