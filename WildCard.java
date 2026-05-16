public class WildCard extends Card {
    WildType type;
    
    public WildCard(WildType type){
        this.type = type;
        this.color = Color.WILD;
    }

    @Override
    public boolean isPlayable(Card topCard, Color activeColor){
        return true;
    }
    @Override
    public String toString(){
        return (type.toString());
    }
    
    public void chooseColor(Color color){
        this.color = color;
    }

    public void applyEffect(Game game){
        if (type == WildType.WILD_DRAW_FOUR) {
            game.drawCardsForNextPlayer(4);
            game.skipNextPlayer();
        }
    }

    public WildType getWildType(){
        return type;
    }
    
}


