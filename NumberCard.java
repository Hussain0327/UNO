public class NumberCard extends Card {
    public NumberCard(int num, Color col){
        number = num;
        color = col;
    }
    public boolean isPlayable(Card topCard, Color activeColor){
        if(topCard.number == number || activeColor == color) return true;
        else return false;
    }
    public String toString(){
        return (color.name()+ " "+ number);
    }
}

enum Color {
    RED,
    GREEN,
    BLUE,
    YELLOW,
    WILD
}
