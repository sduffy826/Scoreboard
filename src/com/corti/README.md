Scoreboard
==========
This project is a simple game scoring application.  Was done as part of an assignment for GDGNJ Android 2017.  I did a  little more than what was asked for but I wanted to learn/experiment.  Instead of scoring for a particular sport I wrote it to support three sports: football, tennis and golf.  I used abstract classes since most of the code is common in any sport; and I overrode specific methods in the concrete classes.  It should be very easy to enhance this design to support scoring other sports (at least that's my hope :) Feedback welcome.

The code is commented throughout so you can find more detail there.

The java files are as follows
* Main.java - Controller to kick off the game scoring, it asks the user what sport to score
* SportsGame.java - Abstract class with common methods/attributes for most sports
* Football.java - Implementation of a football scoring game, it extends SportsGame.  This uses the default behavior of a SportsGame so nothing is overridden.
* Tennis.java - Implementation for a tennis scoring game (also extends SportsGame).  Tennis has some unique qualities; you can see in this that several methods have been overriden.  This is also a game where the score dictates the end of a game so the user doesn't need to manually progress the game along.
* Golf.java - Implementation for golf (yea you know.. extends SportsGame)
* SportsGameUI.java - Abstract class to support the user input needed to score a game (i.e. the prompts)
* EnterPointsUI.java - Concrete implementation to support games where the user enters the score (like football) it extends SportsGameUI
* NoPointsUI.java - Implementation of a game where user doesn't supply score, they just specify the team and it increases the score for that game by 1 (also extends SportsGameUI).