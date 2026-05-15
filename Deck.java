import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private final ArrayList<Card> cards;
    private final ArrayList<Card> discardPile;

    public Deck(){
        cards = GenerateDeck();
        discardPile = new ArrayList<>();
    }

    public ArrayList<Card> GenerateDeck(){
        ArrayList<Card> cards = new ArrayList<>();

        for (Color color : Color.values()) {
            if (color == Color.WILD) continue;
            cards.add(new NumberCard(0, color));
            for (int i = 1; i < 10; i++) {
                cards.add(new NumberCard(i, color));
                cards.add(new NumberCard(i, color));
            }
        }
        for (WildType type : WildType.values()) {
            for (int i = 0; i < 4; i++) {
                cards.add(new WildCard(type));
            }
        }
        for (Color color : Color.values()) {
            if (color == Color.WILD) continue;
            for (ActionType type : ActionType.values()) {
                cards.add(new ActionCard(type, color));
                cards.add(new ActionCard(type, color));
            }
        }
        shuffle(cards);
        return cards;
    }

    public void shuffle(ArrayList<Card> cards){
        Collections.shuffle(cards);
    }

    public void reshuffleDiscard(){
        if (discardPile.isEmpty()) return;
        Card top = discardPile.removeLast();
        cards.addAll(discardPile);
        shuffle(cards);
        discardPile.clear();
        discardPile.add(top);
    }
    
    public Card drawCard(){
        if(cards.isEmpty()){
            reshuffleDiscard();
        }
        Card card = cards.removeFirst();
        return card;
    }

    public void discard(Card card){
        discardPile.add(card);
    }

    public Card topOfDiscard() {
        return discardPile.getLast();
    }
}

