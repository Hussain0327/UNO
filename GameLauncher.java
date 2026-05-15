import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class GameLauncher {
    private static GameGUI gui;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            gui = new GameGUI();
            gui.setNewGameAction(GameLauncher::startNewGame);
            startNewGame();
        });
    }

    private static void startNewGame() {
        List<Player> players = new ArrayList<>();
        players.add(new HumanPlayer("You", gui));
        players.add(new ComputerPlayer("Bot 1", 1));
        players.add(new ComputerPlayer("Bot 2", 1));
        gui.setPlayers(players);
        Deck deck = new Deck();

        new SwingWorker<Player, Void>() {
            @Override
            protected Player doInBackground() {
                Game game = new Game(players, deck);
                game.playRound();
                return game.getWinner();
            }

            @Override
            protected void done() {
                try {
                    Player winner = get();
                    if (winner != null) {
                        gui.recordWin(winner);
                        gui.showWinner(winner);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
