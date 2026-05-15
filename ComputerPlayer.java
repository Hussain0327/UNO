public class ComputerPlayer extends Player { 
    private final int difficulty;
    public ComputerPlayer(String name, int difficulty) {
        super(name);
        this.difficulty = difficulty;
    }
    public int getDifficulty() { return difficulty; }
    
    @Override
    public int chooseCard(Game g) {
        Card top = g.getDeck().topOfDiscard();
        Color active = g.getCurrentColor();

        for (int i = 0; i < handSize(); i++) {
            Card c = getHand().get(i);
            if (c.isPlayable(top, active)) {
                if (handSize() == 2) setSaidUno(true);
                return i;
            }
            
        }
        return -1;
    }

    @Override
    public Color chooseColor() {
        int red = 0, yellow = 0, green = 0, blue = 0;
        for (Card c : getHand()) {
            Color cc = c.getColor();
            if (cc == null) continue;
            switch (cc) {
                case RED:    red++;    break;
                case YELLOW: yellow++; break;
                case GREEN:  green++;  break;
                case BLUE:   blue++;   break;
                default: break;
            }
        }
        int max = Math.max(Math.max(red, yellow), Math.max(green, blue));
        if (max == red)    return Color.RED;
        if (max == yellow) return Color.YELLOW;
        if (max == green)  return Color.GREEN;
        return Color.BLUE;
    }
    
}
