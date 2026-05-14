public class HumanPlayer extends Player {
    private final GameGUI gui;

    public HumanPlayer(String name, GameGUI gui) {
        super(name);
        this.gui = gui;
    }

    @Override
    public int chooseCard(Game g) {
        gui.updateState(g);
        try {
            int choice = gui.awaitCardChoice(this, g);
            if (choice >= 0 && handSize() == 2 && gui.consumeUnoCall()) {
                setSaidUno(true);
            }
            return choice;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1;
        }
    }

    @Override
    public Color chooseColor() {
        try {
            return gui.awaitColorChoice();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Color.RED;
        }
    }
}