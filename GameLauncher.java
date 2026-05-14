import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class GameLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI gui = new GameGUI();

            List<Player> players = new ArrayList<>();
            players.add(new HumanPlayer("You", gui));
            players.add(new ComputerPlayer("Bot 1", 1));
            players.add(new ComputerPlayer("Bot 2", 1));

            Deck deck = new Deck();
            Game game = new Game(players, deck);

            new SwingWorker<Player, Void>() {
                @Override
                protected Player doInBackground() {
                    game.playRound();
                    return game.getWinner();
                }

                @Override
                protected void done() {
                    try {
                        gui.showWinner(get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        });
    }
}