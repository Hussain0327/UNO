public abstract class Card {
    protected int number;
    protected Color color;

    public abstract boolean isPlayable(Card topCard, Color activeColor);

    public Color getColor(){
        return color;
    }
}

