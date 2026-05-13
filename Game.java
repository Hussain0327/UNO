import java.util.List;

public class Game {
    private final List<Player> players;
    private final Deck deck;
    private Color currentColor;
    private int direction;
    private int currentPlayer;
    private boolean isOver;
    private Player winner;

    public Game(List<Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;
        this.direction = 1;
        this.currentPlayer = 0;
        this.isOver = false;
        this.winner = null;
        start();
    }

    public void start() {
        for (int i = 0; i < 7; i++) {
            for (Player player : players) {
                player.addCard(deck.drawCard());
            }
        }

        Card starter = deck.drawCard();
        while (isWildDrawFour(starter)) {
            deck.discard(starter);
            deck.reshuffleDiscard();
            starter = deck.drawCard();
        }
        deck.discard(starter);

        if (starter instanceof ActionCard) {
            currentColor = starter.getColor();
            ActionType action = ((ActionCard) starter).getAction();
            switch (action) {
                case SKIP:
                    currentPlayer = 1 % players.size();
                    break;
                case REVERSE:
                    if (players.size() == 2) {
                        currentPlayer = 1;
                    } else {
                        direction = -1;
                        currentPlayer = players.size() - 1;
                    }
                    break;
                case DRAW_TWO:
                    drawCards(players.get(0), 2);
                    currentPlayer = 1 % players.size();
                    break;
            }
        } else if (starter instanceof WildCard) {
            currentColor = players.get(0).chooseColor();
            currentPlayer = 0;
        } else {
            currentColor = starter.getColor();
            currentPlayer = 0;
        }

        isOver = false;
        winner = null;
    }

    public void playRound() {
        while (!isOver) {
            Player p = getCurrentPlayer();
            int choice = p.chooseCard(this);

            if (choice < 0) {
                drawCards(p, 1);
                choice = p.chooseCard(this);
                if (choice < 0) {
                    nextTurn();
                    continue;
                }
            }

            Card played = p.playCard(choice);
            deck.discard(played);

            if (played instanceof WildCard) {
                Color chosen = p.chooseColor();
                ((WildCard) played).chooseColor(chosen);
                currentColor = chosen;
            } else {
                currentColor = played.getColor();
            }

            if (p.handSize() == 1 && !p.hasSaidUno()) {
                drawCards(p, 2);
            }

            if (checkWin() != null) {
                break;
            }

            applyCardEffect(played);
            nextTurn();
        }
    }

    public void nextTurn() {
        currentPlayer = Math.floorMod(currentPlayer + direction, players.size());
    }

    public void applyCardEffect(Card c) {
        if (c instanceof ActionCard) {
            ((ActionCard) c).applyEffect(this);
        } else if (c instanceof WildCard) {
            ((WildCard) c).applyEffect(this);
        }
    }

    public Player checkWin() {
        for (Player p : players) {
            if (p.handSize() == 0) {
                isOver = true;
                winner = p;
                return p;
            }
        }
        return null;
    }

    public void callUno(Player p) {
        p.setSaidUno(true);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    public Player getNextPlayer() {
        return players.get(Math.floorMod(currentPlayer + direction, players.size()));
    }

    public void reverseDirection() {
        direction = -direction;
    }

    public void skipNextPlayer() {
        nextTurn();
    }

    public void drawCardsForNextPlayer(int n) {
        drawCards(getNextPlayer(), n);
    }

    public void setCurrentColor(Color c) {
        currentColor = c;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getDirection() {
        return direction;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isOver() {
        return isOver;
    }

    public Player getWinner() {
        return winner;
    }

    private boolean isWildDrawFour(Card c) {
        return c instanceof WildCard
            && ((WildCard) c).getWildType() == WildType.WILD_DRAW_FOUR;
    }

    private void drawCards(Player p, int n) {
        for (int i = 0; i < n; i++) {
            if (deck.size() == 0) {
                deck.reshuffleDiscard();
            }
            p.addCard(deck.drawCard());
        }
        if (p.handSize() > 1) {
            p.setSaidUno(false);
        }
    }
}
