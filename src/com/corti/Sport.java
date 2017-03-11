/**
 * Created by duffy_w530 on 3/6/2017.
 */
package com.corti;

public abstract class Sport {
  private String sportName;         // Name of the sport
  private int teamsInvolved;        // Number of teams involved in game (usually 2)
  private int numberOfIntervals;    // Number of intervals (i.e. 4 for football)
  private String intervalName;      // What each interval is called (i.e. period)
  private boolean overtimeAllowed;  // Does sport have overtime?
  private int currentInterval;      // Keeps track of current interval
  private int[][] intervalScores;   // Matrix of scores
  private boolean inOvertime;       // Whether in overtime or not
  private boolean gameOver;         // Boolean when game over
  private boolean quitGame;         // Force the game to be over

  public void addScore(int _teamId, int _score) {
    if (_teamId > 0 && _teamId <= teamsInvolved) {
      // Arrays are 0 offset so decrement by 1
      intervalScores[_teamId - 1][currentInterval - 1] += _score;
    }
  }

  /**
   * This method is called when we want to advance to the next interval, it will
   * call the checkGameOver method since that can only occur when an interval advances
   */
  public void advanceInterval() {
    // Advance the interval, this is the only time we need to check if game is over.
    currentInterval++;
    this.checkGameOver();
    if (this.isGameOver()) currentInterval--;  // Past the end of array revert back 1
  }

  /**
   * This method sets the gameOver and inOvertime attributes; it should be called
   * whenever the user tells us to advance to the next interval (i.e. the next
   * quarter in football)
   */
  private void checkGameOver() {
    gameOver = false;
    if (currentInterval > numberOfIntervals) {
      gameOver = true;  // We'll set it false if we're in overtime
      inOvertime = false;
      // Overtime possible if currentInterval = numberIntervals +1 (and overtime allowed)
      if ((currentInterval == (numberOfIntervals + 1)) && overtimeAllowed) {
        // To have overtime condition we have to have two or more teams that have the
        // same highest score.
        int maxTeamScore = 0;
        for (int i = 0; i < teamsInvolved; i++) {
          // Calculate team score for [i] by looping thru all the intervals
          int otherTeam = 0;
          for (int j = 0; j < numberOfIntervals; j++) {
            otherTeam += intervalScores[i][j];
          }
          if (i == 0 || otherTeam > maxTeamScore) {  // First team or new max found
            maxTeamScore = otherTeam;
            inOvertime = false;
          } else if (maxTeamScore == otherTeam) {
            inOvertime = true;  // Match maxes
          }
        }
        gameOver = !inOvertime;  // If inOvertime game over is false
      }
    }

    gameOver = (quitGame ? true : gameOver);

    return;
  }

  /**
   * This method returns the current interval in a string format, like '1st Period' etc..
   *
   * @return String representing interval
   */
  public String getCurrentInterval() {
    if (this.gameOver) {
      return "Game over";
    }
    return this.getInterval(currentInterval);
  }

  /**
   * Return the interval passed in as a 'nice' string, if we're passed
   * the number of intervals in the game then we'll return "Overtime"
   *
   * @param _interval The interval we want
   * @return String Human readable interval :)
   */
  public String getInterval(int _interval) {
    String temp;
    if (_interval > numberOfIntervals) {
      return "Overtime";
    } else {
      switch (_interval) {
        case 1:
          temp = "1st";
          break;
        case 2:
          temp = "2nd";
          break;
        case 3:
          temp = "3rd";
          break;
        default:
          temp = Integer.toString(_interval);
          break;
      }
    }
    return temp + " " + intervalName;
  }

  /**
   * Return the interval name
   *
   * @return String intervalName (i.e. period, quarter, etc..)
   */
  public String getIntervalName() {
    return intervalName;
  }

  /**
   * Return the number of teams
   *
   * @return int numberOfTeams
   */
  public int getNumberOfTeams() {
    return teamsInvolved;
  }

  /**
   * Return sport name
   *
   * @return String sportName
   */
  public String getSportName() {
    return sportName;
  }

  ;

  /**
   * Return indicator whether we're in overtime
   *
   * @return boolean true if in overtime, false otherwise
   */
  public boolean inOvertime() {
    return inOvertime;
  }

  public void initGame(String _sport,
                       int _teamsInvolved,
                       int _numberOfIntervals,
                       String _intervalName,
                       boolean _overtimeAllowed) {
    sportName = _sport;
    teamsInvolved = _teamsInvolved;
    numberOfIntervals = _numberOfIntervals;
    intervalName = _intervalName;
    overtimeAllowed = _overtimeAllowed;
    quitGame = false;

    currentInterval = 0;
    if ((teamsInvolved > 0) && (numberOfIntervals > 0)) {
      intervalScores = new int[teamsInvolved][numberOfIntervals + (overtimeAllowed ? 1 : 0)];
      currentInterval = 1;
      gameOver = false;
      inOvertime = false;
    } else {
      gameOver = true;
    }
  }

  /**
   * Return indicator if game is over
   *
   * @return boolean true of game is over, false otherwise
   */
  public boolean isGameOver() {
    return gameOver;
  }

  public void outputGame() {
    System.out.println("\n\n" + this.getCurrentInterval());
    System.out.format("%n%n");
    System.out.format("%n%17s", " ");  // Newline and 15 spaces

    for (int i = 0; i < teamsInvolved; i++) {
      System.out.format("%1s%6s%3d%1s", " ", "Team", i + 1, " ");
    }

    for (int intervalPos = 0; intervalPos < currentInterval; intervalPos++) {
      System.out.format("%n%-17s", this.getInterval(intervalPos + 1));

      for (int teamPos = 0; teamPos < teamsInvolved; teamPos++) {
        System.out.format("%8d%3s", intervalScores[teamPos][intervalPos], " ");
      }
    }

    // Output the final score
    System.out.format("%n%-17s", "Final score:");
    for (int teamPos = 0; teamPos < teamsInvolved; teamPos++) {
      System.out.format("%8d%3s", this.runTotal(teamPos), " ");
    }
  }

  public void quitGame() {
    quitGame = true;
    gameOver = true;
  }

  public int runTotal(int _teamPos) {
    int sum = 0;
    for (int i = 0; i < numberOfIntervals; i++) {
      sum += intervalScores[_teamPos][i];
    }
    if (this.inOvertime()) sum += intervalScores[_teamPos][numberOfIntervals];
    return sum;
  }
}
