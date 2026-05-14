public abstract class Card {
    protected Color color;
    protected int pointValue;
    
    public Color getColor() {
        return color;
    }

    public abstract boolean isPlayable(Card top, Color activeColor);

    
}


// i added methods and fields which i needed for my classes 