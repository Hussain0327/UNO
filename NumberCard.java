public class NumberCard extends Card {
    public NumberCard(int num, Color col){
        number = num;
        color = col;
    }

    @Override
    public boolean isPlayable(Card topCard, Color activeColor){
        if (activeColor == color) return true;
        if (topCard instanceof NumberCard && ((NumberCard) topCard).number == number) return true;
        return false;
    }

    @Override
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
