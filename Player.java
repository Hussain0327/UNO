
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final String name;
    private final List<Card> hand;
    private boolean saidUno;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public void addCard(Card c) {
        hand.add(c);
        if (hand.size() > 1) saidUno = false;
    }
    public Card playCard(int i) {
        return hand.remove(i);
    }
    public int handSize() { return hand.size(); }
    public boolean hasSaidUno() { return saidUno; }
    public void setSaidUno(boolean said) { saidUno = said; }
    public String getName() { return name; }

    public List<Card> getHand() { return hand; }
    public abstract int chooseCard(Game g);
    public abstract Color chooseColor();


}
