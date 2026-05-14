public abstract class Card {
    protected int number;
    protected Color color;

    public abstract boolean isPlayable(Card topCard);

    public Color getColor(){
        return color;
    }
}


// i added methods and fields which i needed for my classes 