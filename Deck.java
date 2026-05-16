import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final ArrayList<Card> cards;
    private final ArrayList<Card> discardPile;

    public Deck(){
        cards = GenerateDeck();
        discardPile = new ArrayList<>();
    }

    public ArrayList<Card> GenerateDeck(){
        ArrayList<Card> cards = new ArrayList<>();

        for (Color color: Color.values()){
            if(color == Color.WILD) continue;
            for(int i = 1; i < 10; i++){
                cards.add(new NumberCard(i, color));
                if(i!=0) cards.add(new NumberCard(i, color));
            }
        }
        for (WildCardType type : WildCardType.values()){
            for(int i = 0; i<4;i++){
                cards.add(new WildCard(type));
            }
        }
        for (Color color: Color.values()){
            if(color == Color.WILD) continue;
            for(ActionType type : ActionType.values()){
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
        cards.addAll(discardPile.reversed());
        shuffle(cards);
        discardPile.clear();
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

