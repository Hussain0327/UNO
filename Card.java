public abstract class Card {
    protected int number;
    protected Color color;

    public abstract boolean isPlayable(Card topCard);

    public Color getColor(){
        return color;
    }
}
