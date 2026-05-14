public class NumberCard extends Card {
    public NumberCard(int num, Color col){
        number = num;
        color = col;
    }
    public boolean isPlayable(Card topCard){
        if(topCard.number == number || topCard.color == color) return true;
        else return false;
    }
    public String toString(){
        return ("Number Card: " + color.name()+ " "+ number);
    }
}

enum Color {
    RED,
    GREEN,
    BLUE,
    YELLOW
}
