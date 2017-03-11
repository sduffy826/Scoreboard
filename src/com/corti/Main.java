package com.corti;

import java.util.Scanner;

/**
 * This class is the controller for game scoring; the public main method prompts
 * for the type of game to score and then invokes the scoring method to do that.
 * I tried to isolate the game from ui; the ui should prompt for the team/points
 * and then call methods in the game object to update it's attributes.
 */
public class Main {

  public static int getNumberResponse(int _low, int _high, String _msgPrefix) {
    int rtnValue = 0;
    try {
      rtnValue = Character.getNumericValue(getResponse(_msgPrefix));
      while (rtnValue < _low || rtnValue > _high) {
        rtnValue = Character.getNumericValue(getResponse(_msgPrefix + " (try again): "));
      }
    }
    catch (Exception e) { }
    return rtnValue;
  }

  public static char getResponse(String _msgPrefix) {
    System.out.println(_msgPrefix+">");
    Scanner scanner = new Scanner(System.in);
    char theChar = scanner.next().charAt(0);
    return theChar;
  }

  public static void main(String[] args) {

    /**
     * Mainline logic, show the instructions and prompt the user for what
     * they want to do, they can track the score for a game, quit or show
     * the instructions.  After a game I'll reshow instructions :)
     */
    String prompt = "Enter choice: ";
    char theChar = 'h';  // Show instructions
    int numberOfPlayers;
    boolean playedAGame = false;
    while (theChar != 'q' && theChar != 'Q') {
      prompt = "Enter choice: ";
      playedAGame = false;
      switch (theChar) {
        case 'h':case 'H':
          showInstructions();
          break;
        case 'f': case 'F':
          SportGameUI footballUI = new EnterPointsUI(new Football(), false);
          footballUI.scoreGame();
          playedAGame = true;
          break;
        case 't':case 'T':
          numberOfPlayers = getNumberResponse(2, 4, "Enter number of players (2->4): ");
          SportGameUI tennisUI = new NoPointsUI(new Tennis(numberOfPlayers), true);
          tennisUI.scoreGame();
          playedAGame = true;
          break;
        case 'g': case 'G':
          // We'll play this where user gives us the score person; could have it auto increment when
          // we have score for everyone but since user may score multiple times on same hole (if
          // they're doing incremental we're going to let user decide when hole is over
          numberOfPlayers = getNumberResponse(1, 4, "Enter number of players (1->4): ");
          SportGameUI golfUI = new EnterPointsUI(new Golf(numberOfPlayers), false);
          golfUI.scoreGame();
          playedAGame = true;
          break;
        default:
          prompt = "Enter valid choice (h for help):";
      }
      if (playedAGame) {
        // bypass prompt and show help first, then prompt will come up
        theChar = 'h';
      }
      else
        theChar = getResponse(prompt);
    }
  }

  /**
   * Show the instructions to the user for how to score a game
   */
  public static void showInstructions() {
    System.out.format("%n%n%nEnter one of the following characters below (no quotes):");
    System.out.format("%n'f' to score a football game");
    System.out.format("%n't' score a tennis match");
    System.out.format("%n'g' score a golf game");
    System.out.format("%n%n'h' help (this text)");
    System.out.format("%n'q' to quit the program%n");
  }

}
