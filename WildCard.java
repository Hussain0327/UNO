public class WildCard extends Card {
    WildCardType type;
    public WildCard(WildCardType type){
        this.type = type;
        this.color = Color.WILD;
    }
    public boolean isPlayable(Card topCard, Color activeColor){
        return true;
    }
    public String toString(){
        return (type.toString());
    }

    public void chooseColor(Color color){
        this.color = color;
    }

    public void applyEffect(Game game){
        game.setCurrentColor(color);
    }

    public WildCardType getWildType(){
        return type;
    }
    
}

enum WildCardType{
    WILD_DRAW_FOUR,
    WILD
}
