package com.corti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This an abstract class, it has the attributes/methods associated with the user interface
 * for scoring a game.  The constructor is passed the sport we're playing and a boolean
 * identifying if the game automatically increments to the next interval or whether the
 * user specifies it... to clarify... in a game like football the user has to tell us when
 * an interval (quarter) is over, but in a game like tennis we know when the interval (game)
 * is over based on the score.
 * <p>
 * Note: common methods to override is 'scoringText()' and 'userEntersPoints()', these control
 * the prompt to the user and boolean that identifies if the user enters the points when
 * scoring the game (if they don't enter points then the points increment by one each time
 * they give us a team).
 * <p>
 * Created by duffy_w530 on 3/8/2017.
 */
public abstract class SportGameUI {
  private SportsGame theSport;
  private boolean autoIncrement;

  /**
   * Disable default constructor
   */
  private SportGameUI() {
  }

  /**
   * Constructor, we get a reference to the game we're scoring and also a flag to
   * identify if we automatically increment or not, it's not part of the sport since
   * we have some sports where we may want both ways.
   *
   * @param _sportsGame                     Reference to the game we're playing
   * @param _gameIsAutomaticallyIncremented Boolean to identify if we should auto increment intervals
   */
  public SportGameUI(SportsGame _sportsGame,
                     boolean _gameIsAutomaticallyIncremented) {
    theSport = _sportsGame;
    autoIncrement = _gameIsAutomaticallyIncremented;
  }

  /**
   * This method identifies whether the game automatically has the interval increment on
   * or is controlled from the users input; games like tennis auto increment because we
   * know when the game is over (based on score), games like football are controlled by
   * the user.  If the game does autoIntervalIncrement then we override this method in
   * those concrete classes.
   *
   * @return boolean true if autoIncrement is on, false otherwise
   */
  public boolean autoIntervalIncrement() {
    return autoIncrement;
  }

  /**
   * Show the help for the game to the console
   */
  public void help() {
    System.out.println("\n\n\n\n");
    System.out.format("%n%-18s%s", "Sport", theSport.getSportName());
    if (theSport.isATeamSport())
      System.out.format("%n%-18s%d", "Teams available", theSport.getNumberOfTeams());
    else
      System.out.format("%n%-18s%d", "Players", theSport.getNumberOfTeams());

    System.out.format("%n%-18s%s%n%n", "Currently", theSport.getCurrentInterval());
    System.out.println(scoringText());
    if (autoIntervalIncrement() == false) {
      System.out.println("Enter + to advance to the next " + theSport.getIntervalName());
    }
    System.out.println("Enter s to see current score");
    System.out.println("Enter h to see this help");
    System.out.println("Enter q to quit early :(");
    System.out.println("\nEnter your request and hit enter >");
  }

  /**
   * This method is called to process the team/points passed in; it's passed in as an
   * array of string where pos 0 is the team identifier (an int), if user enters the
   * score for this sport then pos 1 has the score.  If there's a problem with the data
   * passed in we return a boolean false meaning we didn't process the data given
   * to us.
   *
   * @param requestArray Has the team and points (if game specifies that user enters points)
   * @return boolean indicating true if we processed the points
   */
  private boolean processPoints(String[] requestArray) {
    int team = 0, score = 0;

    int arrayLen = requestArray.length;
    boolean validProcess;
    if (userEntersPoints()) validProcess = (arrayLen == 2);
    else validProcess = (arrayLen == 1);

    if (validProcess) {
      try {
        team = Integer.parseInt(requestArray[0].trim());
        if (arrayLen == 2) {
          score = Integer.parseInt(requestArray[1].trim());
        } else score = 1;
      } catch (NumberFormatException ne) {
        validProcess = false;
      }

      if (validProcess) {
        if (team > 0 && team <= theSport.getNumberOfTeams()) {
          theSport.addScore(team, score);
        } else {
          validProcess = false;
        }
      }
    }
    return validProcess;
  }

  /**
   * This handles the scoring of a game; we show help and then continually
   * prompt the user for input until the game is over.
   */
  public void scoreGame() {
    help();  // Start out and show help
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    boolean processed;
    while (theSport.isGameOver() == false) {

      try {
        System.out.print(theSport.getCurrentIntervalText() + ">");
        String str = br.readLine();
        String[] requestSplit = str.trim().split("\\s+");
        processed = true;
        if (requestSplit.length == 1) {
          if (requestSplit[0].equalsIgnoreCase("h")) help();
          else if (requestSplit[0].equalsIgnoreCase("q")) theSport.quitGame();
          else if (requestSplit[0].equals("+")) {  // Only advance if auto increment is off
            if (autoIntervalIncrement() == false) {
              theSport.advanceInterval();
              if (theSport.isGameOver()) theSport.outputGame();
            }
          } else if (requestSplit[0].equalsIgnoreCase("s")) theSport.outputGame();
          else {
            processed = false;
          }
        } else processed = false;

        if (processed == false) { // Didn't process above see if it's points
          if (processPoints(requestSplit) == false) {
            System.out.println("Invalid request, hit letter h for help");
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      // See if some condition caused the interval to end; if so we'll advance to next one
      // or it'll trigger end of game
      if (theSport.isIntervalOver(theSport.getCurrentInterval())) {
        theSport.advanceInterval();
      }
    }
  }

  /**
   * Return standard scoring text for a game
   *
   * @return
   */
  public String scoringText() {
    if (theSport.isATeamSport())
      return "Enter team id followed by score (i.e. 1 7 meaning team 1 got 7 points)";
    else
      return "Enter player id followed by score (i.e. 1 3 means person 1 got 3 points)";
  }

  /**
   * User enters the points from the command line, otherwise they just give the team
   * number and we increment the points by 1 (for sports like tennis, baseball), for
   * sports like football the user tells us the number of points recieved
   */
  public boolean userEntersPoints() {
    return true;
  }
}
