import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameGUI {
    private static final int CARD_W = 78;
    private static final int CARD_H = 116;
    private static final int BIG_CARD_W = 130;
    private static final int BIG_CARD_H = 190;

    private final JFrame frame;
    private final JPanel topCardSlot;
    private final JLabel colorChip;
    private final JLabel turnLabel;
    private final JPanel handPanel;
    private final JPanel westOpponentSlot;
    private final JPanel eastOpponentSlot;
    private final JPanel extraOpponentsPanel;
    private final JPanel scoreboardPanel;
    private final JButton drawButton;
    private final JButton unoButton;
    private final JButton newGameButton;

    private final BlockingQueue<Integer> cardChoiceQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Color> colorChoiceQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<Boolean> unoConfirmQueue = new LinkedBlockingQueue<>();
    private volatile boolean unoCalled = false;

    private final Map<String, Integer> winCounts = new LinkedHashMap<>();
    private Runnable newGameAction;

    public GameGUI() {
        frame = new JFrame("UNO");
        frame.setSize(1320, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new java.awt.Color(18, 95, 45));

        extraOpponentsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 12));
        extraOpponentsPanel.setOpaque(false);
        frame.add(extraOpponentsPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 30));
        tablePanel.setOpaque(false);
        topCardSlot = new JPanel(new BorderLayout());
        topCardSlot.setOpaque(false);
        topCardSlot.setPreferredSize(new Dimension(BIG_CARD_W, BIG_CARD_H));
        tablePanel.add(topCardSlot);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        infoPanel.setOpaque(false);
        colorChip = new JLabel("Color: ?", SwingConstants.CENTER);
        colorChip.setOpaque(true);
        colorChip.setBackground(java.awt.Color.DARK_GRAY);
        colorChip.setForeground(java.awt.Color.WHITE);
        colorChip.setPreferredSize(new Dimension(200, 50));
        colorChip.setFont(new Font("SansSerif", Font.BOLD, 18));
        colorChip.setBorder(new LineBorder(java.awt.Color.WHITE, 2));
        turnLabel = new JLabel(" ", SwingConstants.CENTER);
        turnLabel.setForeground(java.awt.Color.WHITE);
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        infoPanel.add(colorChip);
        infoPanel.add(turnLabel);
        tablePanel.add(infoPanel);
        frame.add(tablePanel, BorderLayout.CENTER);

        westOpponentSlot = new JPanel();
        westOpponentSlot.setLayout(new BoxLayout(westOpponentSlot, BoxLayout.Y_AXIS));
        westOpponentSlot.setOpaque(false);
        westOpponentSlot.setAlignmentX(Component.LEFT_ALIGNMENT);

        eastOpponentSlot = new JPanel();
        eastOpponentSlot.setLayout(new BoxLayout(eastOpponentSlot, BoxLayout.Y_AXIS));
        eastOpponentSlot.setOpaque(false);
        eastOpponentSlot.setAlignmentX(Component.LEFT_ALIGNMENT);

        scoreboardPanel = new JPanel();
        scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));
        scoreboardPanel.setOpaque(false);
        scoreboardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        frame.add(buildWestPanel(), BorderLayout.WEST);
        frame.add(buildEastPanel(), BorderLayout.EAST);

        JPanel bottomPanel = new JPanel(new BorderLayout(0, 6));
        bottomPanel.setOpaque(false);
        handPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
        handPanel.setOpaque(false);
        JScrollPane handScroll = new JScrollPane(handPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        handScroll.setBorder(null);
        handScroll.setOpaque(false);
        handScroll.getViewport().setOpaque(false);
        handScroll.setPreferredSize(new Dimension(820, CARD_H + 30));
        bottomPanel.add(handScroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        buttonPanel.setOpaque(false);
        drawButton = bigButton("Draw");
        drawButton.addActionListener(e -> cardChoiceQueue.offer(-1));
        unoButton = bigButton("UNO!");
        unoButton.setBackground(new java.awt.Color(220, 40, 40));
        unoButton.setForeground(java.awt.Color.WHITE);
        unoButton.setOpaque(true);
        unoButton.setBorderPainted(false);
        unoButton.setEnabled(false);
        unoButton.addActionListener(e -> unoCalled = true);
        newGameButton = bigButton("New Game");
        newGameButton.addActionListener(e -> {
            if (newGameAction != null) newGameAction.run();
        });
        buttonPanel.add(drawButton);
        buttonPanel.add(unoButton);
        buttonPanel.add(newGameButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel buildWestPanel() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setOpaque(false);
        side.setBorder(new EmptyBorder(20, 16, 20, 8));
        side.setPreferredSize(new Dimension(210, 0));

        side.add(westOpponentSlot);
        side.add(Box.createVerticalStrut(28));

        JLabel scoresHeader = new JLabel("Wins");
        scoresHeader.setForeground(java.awt.Color.WHITE);
        scoresHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        scoresHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(scoresHeader);
        side.add(Box.createVerticalStrut(8));
        side.add(scoreboardPanel);

        side.add(Box.createVerticalGlue());
        return side;
    }

    private JPanel buildEastPanel() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setOpaque(false);
        side.setBorder(new EmptyBorder(20, 8, 20, 16));
        side.setPreferredSize(new Dimension(210, 0));

        side.add(eastOpponentSlot);
        side.add(Box.createVerticalStrut(28));

        JLabel legendHeader = new JLabel("Legend");
        legendHeader.setForeground(java.awt.Color.WHITE);
        legendHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        legendHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(legendHeader);
        side.add(Box.createVerticalStrut(8));

        side.add(legendRow("S", "Skip"));
        side.add(legendRow("R", "Reverse"));
        side.add(legendRow("+2", "Draw Two"));
        side.add(legendRow("W", "Wild"));
        side.add(legendRow("+4", "Wild Draw Four"));
        side.add(Box.createVerticalStrut(10));

        JLabel wildNote = new JLabel("<html><i>Wilds render black</i></html>");
        wildNote.setForeground(new java.awt.Color(220, 220, 220));
        wildNote.setFont(new Font("SansSerif", Font.PLAIN, 12));
        wildNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(wildNote);

        side.add(Box.createVerticalGlue());
        return side;
    }

    private JLabel legendRow(String symbol, String meaning) {
        JLabel l = new JLabel("<html><b>" + symbol + "</b>&nbsp;&nbsp;" + meaning + "</html>");
        l.setForeground(java.awt.Color.WHITE);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(new EmptyBorder(2, 0, 2, 0));
        return l;
    }

    private JButton bigButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 18));
        b.setPreferredSize(new Dimension(130, 52));
        b.setFocusPainted(false);
        return b;
    }

    public void setNewGameAction(Runnable r) {
        this.newGameAction = r;
    }

    public void setPlayers(List<Player> players) {
        SwingUtilities.invokeLater(() -> {
            for (Player p : players) {
                winCounts.putIfAbsent(p.getName(), 0);
            }
            refreshScoreboard();
            newGameButton.setEnabled(false);
        });
    }

    public void recordWin(Player winner) {
        SwingUtilities.invokeLater(() -> {
            winCounts.merge(winner.getName(), 1, Integer::sum);
            refreshScoreboard();
            newGameButton.setEnabled(true);
        });
    }

    private void refreshScoreboard() {
        scoreboardPanel.removeAll();
        for (Map.Entry<String, Integer> e : winCounts.entrySet()) {
            JLabel row = new JLabel(e.getKey() + ":  " + e.getValue());
            row.setForeground(java.awt.Color.WHITE);
            row.setFont(new Font("SansSerif", Font.PLAIN, 15));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setBorder(new EmptyBorder(2, 0, 2, 0));
            scoreboardPanel.add(row);
        }
        scoreboardPanel.revalidate();
        scoreboardPanel.repaint();
    }

    public void updateState(Game g) {
        SwingUtilities.invokeLater(() -> {
            topCardSlot.removeAll();
            topCardSlot.add(new CardView(g.getDeck().topOfDiscard(), BIG_CARD_W, BIG_CARD_H, true), BorderLayout.CENTER);

            Color active = g.getCurrentColor();
            colorChip.setText("Color: " + (active == null ? "?" : active.name()));
            colorChip.setBackground(swingColorFor(active));
            colorChip.setForeground(active == Color.YELLOW ? java.awt.Color.BLACK : java.awt.Color.WHITE);

            String dir = g.getDirection() > 0 ? "> clockwise" : "< counter-clockwise";
            turnLabel.setText("Turn: " + g.getCurrentPlayer().getName() + "    " + dir);

            List<Player> opponents = new ArrayList<>();
            for (Player p : g.getPlayers()) {
                if (!(p instanceof HumanPlayer)) opponents.add(p);
            }

            westOpponentSlot.removeAll();
            eastOpponentSlot.removeAll();
            extraOpponentsPanel.removeAll();
            if (opponents.size() >= 1) westOpponentSlot.add(opponentTile(opponents.get(0)));
            if (opponents.size() >= 2) eastOpponentSlot.add(opponentTile(opponents.get(1)));
            for (int i = 2; i < opponents.size(); i++) {
                extraOpponentsPanel.add(opponentTile(opponents.get(i)));
            }

            frame.revalidate();
            frame.repaint();
        });
    }

    private JPanel opponentTile(Player p) {
        JPanel tile = new JPanel();
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        tile.setOpaque(false);
        tile.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComponent back = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(java.awt.Color.BLACK);
                g2.fillRoundRect(0, 0, w - 2, h - 2, 14, 14);
                g2.setColor(new java.awt.Color(220, 30, 30));
                g2.fillOval(w / 8, h / 4, w - w / 4 - 2, h / 2);
                g2.setColor(java.awt.Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(14, w / 4)));
                FontMetrics fm = g2.getFontMetrics();
                String s = "UNO";
                int sw = fm.stringWidth(s);
                g2.drawString(s, (w - 2 - sw) / 2, (h - 2 + fm.getAscent()) / 2 - 4);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(java.awt.Color.WHITE);
                g2.drawRoundRect(2, 2, w - 6, h - 6, 12, 12);
                g2.dispose();
            }
        };
        back.setPreferredSize(new Dimension(80, 120));
        back.setMaximumSize(new Dimension(80, 120));
        back.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel backWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        backWrap.setOpaque(false);
        backWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        backWrap.add(back);
        tile.add(backWrap);

        tile.add(Box.createVerticalStrut(6));
        JLabel name = new JLabel(p.getName() + "  (" + p.handSize() + ")");
        name.setForeground(java.awt.Color.WHITE);
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel nameWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nameWrap.setOpaque(false);
        nameWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameWrap.add(name);
        tile.add(nameWrap);
        return tile;
    }

    public int awaitCardChoice(HumanPlayer p, Game g) throws InterruptedException {
        cardChoiceQueue.clear();
        final Card top = g.getDeck().topOfDiscard();
        final Color active = g.getCurrentColor();
        SwingUtilities.invokeLater(() -> {
            handPanel.removeAll();
            for (int i = 0; i < p.handSize(); i++) {
                final int idx = i;
                Card c = p.getHand().get(i);
                boolean playable = c.isPlayable(top, active);
                CardView cv = new CardView(c, CARD_W, CARD_H, playable);
                if (playable) {
                    cv.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    cv.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            cardChoiceQueue.offer(idx);
                        }
                    });
                }
                handPanel.add(cv);
            }
            unoButton.setEnabled(p.handSize() == 2);
            handPanel.revalidate();
            handPanel.repaint();
        });
        return cardChoiceQueue.take();
    }

    public Color awaitColorChoice() throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            JDialog dlg = new JDialog(frame, "Pick a color", true);
            dlg.setLayout(new GridLayout(2, 2, 8, 8));
            Color[] choices = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};
            for (Color c : choices) {
                JButton b = new JButton(c.name());
                b.setFont(new Font("SansSerif", Font.BOLD, 20));
                b.setBackground(swingColorFor(c));
                b.setForeground(c == Color.YELLOW ? java.awt.Color.BLACK : java.awt.Color.WHITE);
                b.setOpaque(true);
                b.setBorderPainted(false);
                b.addActionListener(e -> {
                    colorChoiceQueue.offer(c);
                    dlg.dispose();
                });
                dlg.add(b);
            }
            dlg.setSize(340, 220);
            dlg.setLocationRelativeTo(frame);
            dlg.setVisible(true);
        });
        return colorChoiceQueue.take();
    }

    public boolean consumeUnoCall() {
        boolean called = unoCalled;
        unoCalled = false;
        return called;
    }

    public boolean askUnoCall() throws InterruptedException {
        unoConfirmQueue.clear();
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(frame,
                    "You're about to play your second-to-last card.\nCall UNO?",
                    "UNO!",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            unoConfirmQueue.offer(choice == JOptionPane.YES_OPTION);
        });
        return unoConfirmQueue.take();
    }

    public void showWinner(Player winner) {
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(frame,
                    winner.getName() + " wins!\n\nPlay again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            if (choice == JOptionPane.YES_OPTION && newGameAction != null) {
                newGameAction.run();
            }
        });
    }

    private static java.awt.Color swingColorFor(Color c) {
        if (c == null) return java.awt.Color.DARK_GRAY;
        switch (c) {
            case RED:    return new java.awt.Color(220, 40, 40);
            case YELLOW: return new java.awt.Color(245, 200, 40);
            case GREEN:  return new java.awt.Color(50, 170, 60);
            case BLUE:   return new java.awt.Color(50, 110, 220);
            default:     return java.awt.Color.DARK_GRAY;
        }
    }

    private static class CardView extends JComponent {
        private final Card card;
        private final boolean playable;

        CardView(Card card, int w, int h, boolean playable) {
            this.card = card;
            this.playable = playable;
            setPreferredSize(new Dimension(w, h));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            int arc = Math.max(12, w / 6);

            g2.setColor(new java.awt.Color(0, 0, 0, 90));
            g2.fillRoundRect(3, 5, w - 4, h - 4, arc, arc);

            java.awt.Color bg = colorFor(card);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w - 4, h - 4, arc, arc);

            int ovalX = w / 8;
            int ovalY = h / 10;
            int ovalW = w - 4 - 2 * ovalX;
            int ovalH = h - 4 - 2 * ovalY;
            g2.setColor(java.awt.Color.WHITE);
            g2.fillOval(ovalX, ovalY, ovalW, ovalH);

            String label = labelFor(card);
            int fontSize;
            if (label.length() == 1) fontSize = Math.max(28, w / 2);
            else if (label.length() <= 2) fontSize = Math.max(24, (int) (w / 2.4));
            else fontSize = Math.max(14, w / 4);
            g2.setColor(bg);
            g2.setFont(new Font("SansSerif", Font.BOLD, fontSize));
            FontMetrics fm = g2.getFontMetrics();
            int sw = fm.stringWidth(label);
            int sh = fm.getAscent();
            g2.drawString(label, (w - 4 - sw) / 2, (h - 4 + sh) / 2 - 6);

            g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(11, w / 7)));
            g2.setColor(java.awt.Color.WHITE);
            FontMetrics sfm = g2.getFontMetrics();
            String corner = cornerLabelFor(card);
            g2.drawString(corner, 7, 7 + sfm.getAscent());
            int cornerW = sfm.stringWidth(corner);
            g2.drawString(corner, w - 4 - 7 - cornerW, h - 4 - 9);

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(playable ? java.awt.Color.WHITE : new java.awt.Color(120, 120, 120));
            g2.drawRoundRect(1, 1, w - 6, h - 6, arc, arc);

            if (!playable) {
                g2.setColor(new java.awt.Color(0, 0, 0, 120));
                g2.fillRoundRect(0, 0, w - 4, h - 4, arc, arc);
            }

            g2.dispose();
        }

        private static java.awt.Color colorFor(Card c) {
            if (c instanceof WildCard) return java.awt.Color.BLACK;
            Color col = c.getColor();
            if (col == null) return java.awt.Color.BLACK;
            return swingColorFor(col);
        }

        private static String labelFor(Card c) {
            if (c instanceof NumberCard) {
                return String.valueOf(((NumberCard) c).number);
            }
            if (c instanceof ActionCard) {
                switch (((ActionCard) c).actionType) {
                    case SKIP:     return "S";
                    case REVERSE:  return "R";
                    case DRAW_TWO: return "+2";
                }
            }
            if (c instanceof WildCard) {
                return ((WildCard) c).getWildType() == WildType.WILD_DRAW_FOUR ? "+4" : "W";
            }
            return "?";
        }

        private static String cornerLabelFor(Card c) {
            return labelFor(c);
        }
    }
}
