import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
/**
 * Whack-a-mole Game.
 * @author Jie Sun
 */
public class Game extends JFrame implements ActionListener {
    /**
     * The total number of models displayed in the GUI.
     */
    private static final int NUM_MOLES = 48;
    /**
     * Game status.
     */
    private static boolean finished;
    /**
     * Score for each round.
     */
    private static int score = 0;
    /**
     * The start button for the game.
     */
    private static JButton startButton;
    /**
     * The mole buttons.
     */
    private JButton[] moleButtons;
    /**
     * Timer field for the countdown.
     */
    private static JTextField timerField;
    /**
     * Score field for the game.
     */
    private static JTextField scoreField;
    /**
     * Initial background color for the moles.
     */
    private Color bgColor = Color.LIGHT_GRAY;
    /**
     * Initial background color for the down after hit.
     */
    private Color downColor = Color.GREEN;
    /**
     * Constructor.
     */
    public Game() {
        setTitle("What-a-mole");
        setSize(650, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font font = new Font(Font.MONOSPACED, Font.BOLD, 14);
        JPanel mainpane = new JPanel();

        JPanel line = new JPanel();
        startButton = new JButton("start");
        startButton.addActionListener(this);
        line.add(startButton);

        JLabel timer = new JLabel("Time Left:");
        timerField = new JTextField(7);
        timerField.setEditable(false);
        line.add(timer);
        line.add(timerField);
        JLabel scoreTitle = new JLabel("Score");
        scoreField = new JTextField(7);
        scoreField.setText(String.valueOf(score));
        scoreField.setEditable(false);
        line.add(scoreTitle);
        line.add(scoreField);
        mainpane.add(line);

        JPanel molepane = new JPanel();
        molepane.setLayout(new GridLayout(6, 8));
        moleButtons = new JButton[NUM_MOLES];
        for (int i = 0; i < moleButtons.length; i++) {
            moleButtons[i] = new JButton("   ");
            moleButtons[i].setBackground(bgColor);
            moleButtons[i].setFont(font);
            moleButtons[i].setOpaque(true);
            moleButtons[i].addActionListener(this);
            molepane.add(moleButtons[i]);
        }
        mainpane.add(molepane);

        setContentPane(mainpane);
        setVisible(true);
    }
    /**
     * Method to be invoked when buttons are clicked.
     * @param e event object
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            Thread timer = new TimerThread();
            timer.start();
            score = 0;
            for (int i = 0; i < NUM_MOLES; i++) {
                Thread moleThread = new MoleThread(moleButtons[i]);
                moleThread.start();
            }
        } else {
            for (int i = 0; i < NUM_MOLES; i++) {
                if (e.getSource() == moleButtons[i] && moleButtons[i].getText().equals("Hit") && !finished) {
                    score += 1;
                    scoreField.setText(String.valueOf(score));
                    moleButtons[i].setText(":-(");
                    moleButtons[i].setBackground(downColor);
                    moleButtons[i].setOpaque(true);
                }
            }
        }
    }
    /**
     * Main method that instantiates Game Object.
     * @param args
     */
    public static void main(String[] args) {
        new Game();
    }
    /**
     * Timer Thread.
     */
    private static class TimerThread extends Thread {
        /**
         * Counter.
         */
        private int counter = 20;
        /**
         * Constructor.
         */
        TimerThread() {
            timerField.setText("");
            scoreField.setText(String.valueOf(0));
        }
        /**
         * Implement run method for the timer thread.
         */
        @Override
        public void run() {
            finished = false;
            startButton.setEnabled(false);
            try {
                while (counter >= 0) {
                    timerField.setText(String.valueOf(counter));
                    counter -= 1;
                    Thread.sleep(1000);
                }
                finished = true;
                Thread.sleep(5000);
                startButton.setEnabled(finished);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static class MoleThread extends Thread {
        /**
         * Each button represents one mole.
         */
        private JButton myButton;
        /**
         * Mole color as hitting.
         */
        private Color hitColor = Color.ORANGE;
        /**
         * Mole color as down.
         */
        private Color downColor = Color.LIGHT_GRAY;
        /**
         * Random number generator.
         */
        private Random random = new Random();
        /**
         * Time while a Mole pops up.
         */
        private int hitTime = random.nextInt(3);
        /**
         * Time while a Mole pops down.
         */
        private int downTime = random.nextInt(5);
        /**
         * Time while a Mole is quite.
         */
        private int quiteTime = random.nextInt(20);
        /**
         * Constructor.
         * @param button
         */
        MoleThread(JButton button) {
            myButton = button;
        }
        /**
         * Implement run method for each mole thread.
         */
        @Override
        public void run() {
            try {
                myButton.setText("");
                myButton.setBackground(downColor);
                Thread.sleep(quiteTime * 1000);
                while (!finished) {
                    myButton.setText("Hit");
                    myButton.setBackground(hitColor);
                    Thread.sleep(hitTime * 1000 + 500);
                    myButton.setText("");
                    myButton.setBackground(downColor);
                    Thread.sleep(downTime * 1000);
                    myButton.setText("");
                    myButton.setBackground(downColor);
                    Thread.sleep(quiteTime * 1000);
                }
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
    }
}
