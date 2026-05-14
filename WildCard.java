public class WildCard extends Card {
    WildCardType type;
    public WildCard(WildCardType type){
        this.type = type;
    }
    public boolean isPlayable(Card topCard, Color activeColor){
        return true;
    }
    public String toString(){
        return (type+ " "+(color.name() == null ? "not specified" : color.name()));
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
