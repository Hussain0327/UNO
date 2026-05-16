# UNO - May 14 Update

I finished my whole half today. Here is what I built:

- `Game.java` - turn loop, direction, color tracking, effect routing, win check, UNO penalty
- `Player.java` - abstract base (name, hand, saidUno, all the basic methods, plus abstract `chooseCard` and `chooseColor`)
- `ComputerPlayer.java` - simple AI (first valid card, most-held color), auto-calls UNO at handSize 2
- `HumanPlayer.java` - GUI input, uses BlockingQueues to talk to the GUI thread
- `GameGUI.java` - Swing JFrame with the hand panel, Draw button, UNO button, color picker, winner dialog
- `GameLauncher.java` - main entry, runs the game on a SwingWorker so the UI does not freeze

## GUI

GUI instead of command line. Swing window felt way better, you can click cards, pick colors from a dialog, see a winner popup, all of it.

## What you still need to write

Your enums and the Card abstract are good already, in those classes I added what I needed for mine.

### NumberCard.java

Right now it is just `public class NumberCard { }`. Needs:

- `extends Card`
- A `private final int number` field
- Constructor `NumberCard(Color color, int number)` that sets color, number, and pointValue = number
- `getNumber()`
- Override `isPlayable(Card top, Color activeColor)` - true if same color OR same number as the top NumberCard
- Override `toString()`

### ActionCard.java

It extends Card but the body is empty so it does not compile yet. Needs:

- `private final ActionType action` field
- Constructor `ActionCard(Color color, ActionType action)` setting color, action, pointValue = 20
- `getAction()`
- `applyEffect(Game g)` - switch on the action:
  - SKIP: `g.skipNextPlayer()`
  - REVERSE: `g.reverseDirection()`
  - DRAW_TWO: `g.drawCardsForNextPlayer(2)` then `g.skipNextPlayer()`
- Override `isPlayable(Card top, Color activeColor)` - true if same color OR same action as the top ActionCard
- Override `toString()`

### WildCard.java

Same situation as ActionCard, extends Card but empty body. Needs:

- `private final WildType wildType` field
- `private Color chosenColor` field
- Constructor `WildCard(WildType wildType)` setting wildType, color = `Color.WILD`, pointValue = 50
- `getWildType()`
- `chooseColor(Color c)` that just sets chosenColor
- `applyEffect(Game g)` - if WILD_DRAW_FOUR, call `g.drawCardsForNextPlayer(4)` then `g.skipNextPlayer()`. Plain WILD does nothing here.
- Override `isPlayable(Card top, Color activeColor)` - just return true, wilds are always playable
- Override `toString()`

### Deck.java

You have the fields and `topOfDiscard()`. Still need:

- Constructor that calls `build()` and `shuffle()`
- `build()` - the full 108 card deck:
  - 76 number cards: one 0 per color, two of 1-9 per color, four colors
  - 24 action cards: two of each SKIP/REVERSE/DRAW_TWO per color
  - 8 wild cards: 4 plain WILD, 4 WILD_DRAW_FOUR
- `shuffle()` - just `Collections.shuffle(cards)`
- `drawCard()` - remove and return the last element of cards
- `discard(Card c)` - push onto discardPile
- `reshuffleDiscard()` - pop top of discard, move the rest into cards, shuffle, push the top back
- `size()` - return cards.size()

## Stuff my Game calls on your classes

Make sure these signatures match exactly or my code breaks:

- `deck.drawCard()` returns Card
- `deck.discard(Card)` returns void
- `deck.topOfDiscard()` returns Card
- `deck.reshuffleDiscard()` returns void
- `deck.size()` returns int
- `((ActionCard) c).getAction()` returns ActionType
- `((ActionCard) c).applyEffect(Game)` returns void
- `((WildCard) c).getWildType()` returns WildType
- `((WildCard) c).chooseColor(Color)` returns void
- `((WildCard) c).applyEffect(Game)` returns void
- `card.isPlayable(Card top, Color activeColor)` returns boolean
- `card.getColor()` returns Color
