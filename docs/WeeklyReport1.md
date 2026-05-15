# UNO - Weekly Report #1

**Team:** Raja Hussain, Simon Ames
**Date:** May 14, 2026

## Project Updates

The full design is pretty much the same as the original proposal, only difference is that we are adding a `GameGUI` class to support a Swing-based interface. UML unchanged otherwise.

Current status of the source files:

- **Working drafts done:** Game, Player, HumanPlayer, ComputerPlayer, GameLauncher, GameGUI, Card, Deck, the three enums (Color, ActionType, WildType)
- **In progress:** NumberCard, ActionCard, WildCard concrete classes, plus testing, refinement, and integration on the rest

The engine runs end-to-end with stubbed cards. We are now in the polish and integration phase, with both halves coming together for the Sunday meeting.

## Responsibilities This Week

**Hussain:** Working on the game engine (Game), Player abstract base, ComputerPlayer AI, HumanPlayer, GameGUI Swing interface, and GameLauncher. Currently iterating on GUI behavior, edge cases in the turn loop, and preparing the test suite.

**Simon:** Working on the enums, abstract Card class, and the Deck structure. Currently building out the concrete card implementations (NumberCard, ActionCard, WildCard) and finishing the remaining Deck methods.

## Outstanding Tasks

- Simon: finalize NumberCard, ActionCard, WildCard bodies and the rest of Deck (build, shuffle, draw, discard, reshuffle). Target: Sunday May 17 meeting.
- Hussain: JUnit tests for the Game module, GUI polish, edge-case handling for 2-player Reverse-as-Skip and Wild Draw Four stacking, README update.
- Both: integration test on May 17, full demo dry run on May 18.

## Team Dynamics

We have been working through GitHub. No blockers from either side, both halves on track for the Sunday integration.

## GitHub Repository

https://github.com/Hussain0327/UNO
