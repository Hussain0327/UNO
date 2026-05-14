import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameGUI {
    private final JFrame frame;
    private final JLabel topCardLabel;
    private final JLabel currentColorLabel;
    private final JLabel currentPlayerLabel;
    private final JPanel handPanel;
    private final JPanel opponentsPanel;
    private final JButton drawButton;
    private final JButton unoButton;

    private final BlockingQueue<Integer> cardChoiceQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Color> colorChoiceQueue = new LinkedBlockingQueue<>();
    private volatile boolean unoCalled = false;

    public GameGUI() {
        frame = new JFrame("UNO");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topCardLabel = new JLabel("Top: ", SwingConstants.CENTER);
        currentColorLabel = new JLabel("Color: ", SwingConstants.CENTER);
        currentPlayerLabel = new JLabel("Turn: ", SwingConstants.CENTER);
        topPanel.add(topCardLabel);
        topPanel.add(currentColorLabel);
        topPanel.add(currentPlayerLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        opponentsPanel = new JPanel();
        frame.add(opponentsPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        handPanel = new JPanel(new FlowLayout());
        bottomPanel.add(new JScrollPane(handPanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        drawButton = new JButton("Draw");
        drawButton.addActionListener(e -> cardChoiceQueue.offer(-1));
        unoButton = new JButton("UNO!");
        unoButton.addActionListener(e -> unoCalled = true);
        buttonPanel.add(drawButton);
        buttonPanel.add(unoButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void updateState(Game g) {
        SwingUtilities.invokeLater(() -> {
            topCardLabel.setText("Top: " + g.getDeck().topOfDiscard());
            currentColorLabel.setText("Color: " + g.getCurrentColor());
            currentPlayerLabel.setText("Turn: " + g.getCurrentPlayer().getName()
                + "  Direction: " + (g.getDirection() > 0 ? "clockwise" : "counter-clockwise"));
            opponentsPanel.removeAll();
            for (Player p : g.getPlayers()) {
                opponentsPanel.add(new JLabel(p.getName() + ": " + p.handSize() + " cards     "));
            }
            opponentsPanel.revalidate();
            opponentsPanel.repaint();
        });
    }

    public int awaitCardChoice(HumanPlayer p, Game g) throws InterruptedException {
        final Card top = g.getDeck().topOfDiscard();
        final Color active = g.getCurrentColor();
        SwingUtilities.invokeLater(() -> {
            handPanel.removeAll();
            for (int i = 0; i < p.handSize(); i++) {
                final int idx = i;
                Card c = p.getHand().get(i);
                JButton btn = new JButton(c.toString());
                btn.setEnabled(c.isPlayable(top, active));
                btn.addActionListener(e -> cardChoiceQueue.offer(idx));
                handPanel.add(btn);
            }
            handPanel.revalidate();
            handPanel.repaint();
        });
        return cardChoiceQueue.take();
    }

    public Color awaitColorChoice() throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"RED", "YELLOW", "GREEN", "BLUE"};
            int choice = JOptionPane.showOptionDialog(frame, "Pick a color", "Wild!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            Color c;
            switch (choice) {
                case 0:  c = Color.RED;    break;
                case 1:  c = Color.YELLOW; break;
                case 2:  c = Color.GREEN;  break;
                case 3:  c = Color.BLUE;   break;
                default: c = Color.RED;
            }
            colorChoiceQueue.offer(c);
        });
        return colorChoiceQueue.take();
    }

    public boolean consumeUnoCall() {
        boolean called = unoCalled;
        unoCalled = false;
        return called;
    }

    public void showWinner(Player winner) {
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(frame, winner.getName() + " wins!", "Game Over",
                JOptionPane.INFORMATION_MESSAGE));
    }
}