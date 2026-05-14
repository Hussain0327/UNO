public class WildCard extends Card {
    public WildCard(){
    }
    public boolean isPlayable(Card topCard){
        return true;
    }
    public String toString(){
        return ("Wild Card: " + (color.name() == null ? "not specified" : color.name()));
    }

    public void chooseColor(Color color){
        this.color = color;
    }

    public void applyEffect(Game game){
        game.setCurrentColor(color);
    }
}
