import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
public class Deck {
    private final List<Card> cards;
    private final Stack<Card> discardPile;

    public Deck() {
        this.cards = new ArrayList<>();
        this.discardPile = new Stack<>();
        // build();
        // shuffle(); implement these both
    }

    public Card topOfDiscard() {
        return discardPile.peek();
    }
}

