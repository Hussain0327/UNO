import java.util.ArrayList;
import java.util.Collections;

public class Deck2 {
    ArrayList<Card> deck;
    ArrayList<Card> discard;

    public Deck(){
        deck = GenerateDeck();
        discard = new ArrayList<>();
    }

    public ArrayList<Card> GenerateDeck(){
        ArrayList<Card> cards = new ArrayList<>();

        for (Color color: Color.values()){
            for(int i = 1; i < 10; i++){
                cards.add(new NumberCard(i, color));
                if(i!=0) cards.add(new NumberCard(i, color));
            }
        }
        shuffle(cards);
        return cards;
    }

    public void shuffle(ArrayList<Card> cards){
        Collections.shuffle(cards);
    }

    public void reshuffleDiscard(){
        deck.addAll((ArrayList<Card>)discard.reversed());
        shuffle(deck);
        discard.clear();
    }
    
    public Card drawCard(){
        if(deck.isEmpty()){
            
        }
        Card card = deck.removeFirst();
        return card;
    }

    public void discard(Card card){
        discard.add(card);
    }
}