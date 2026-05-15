public class ActionCard extends Card{
    ActionType actionType;

    public ActionCard(ActionType type, Color col){
        color = col;
        actionType = type;
    }
    public boolean isPlayable(Card topCard, Color activeColor){
        if (activeColor == color) return true;
        if (topCard instanceof ActionCard && ((ActionCard) topCard).actionType == actionType) return true;
        return false;
    }
    public String toString(){
        return (color.name()+" "+actionType.name());
    }

    public void setColor(Color color){
        this.color = color;
    }

    public ActionType getAction(){
        return actionType;
    }

    public void applyEffect(Game game){
        switch (actionType) {
            case DRAW_TWO:
                game.drawCardsForNextPlayer(2);
                game.skipNextPlayer();
                break;
            case REVERSE:
                game.reverseDirection();
                break;
            case SKIP:
                game.skipNextPlayer();
                break;
            default:
                break;
        }
    }
}
