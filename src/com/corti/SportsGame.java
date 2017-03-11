/**
 * Created by duffy_w530 on 3/6/2017.
 */
package com.corti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This abstract class supports common code for scoring sporting games, every sport
 * you want to score should extend this class and override the methods that are unique
 * to it.
 *
 * Note: if wondering why this has outputGame instead of having that in the ui class; it's
 * because that's not related to user input (although can see an argument for it :))
 */

public abstract class SportsGame {
  private String sportName;               // Name of the sport
  private boolean teamSport;              // Boolean for team or individual sport
  private int numberOfTeams;              // Number of teams involved in game (usually 2)
  private int numberOfIntervals;          // Number of intervals (i.e. 4 for football)
  private String intervalName;            // What each interval is called (i.e. period)
  private boolean overtimeAllowed;        // Does sport have overtime/extended time?
  private int currentInterval;            // Keeps track of current interval
  private boolean gameOver;               // Boolean when game over
  private List<Integer[]> intervalScores; // List containing overtime scores
  private boolean quitGame;               // Quit the game

  /**
   * This adds an interval (i.e. a quarter in a football game, it also initializes
   * all the scores for that interval to 0.  It also updates the currentInterval by 1
   */
  private void addInterval() {
    Integer[] interval = new Integer[numberOfTeams];
    for (int i = 0; i < numberOfTeams; i++) {
      interval[i] = 0;
    }
    intervalScores.add(interval);
    currentInterval++;
  }

  /**
   * Add the score passed in for the associated team
   *
   * @param _teamId teamId (integer from 1->numberOfTeams)
   * @param _score  score
   */
  public void addScore(int _teamId, int _score) {
    if (_teamId > 0 && _teamId <= numberOfTeams) {
      // Arrays are 0 offset so decrement by 1
      intervalScores.get(currentInterval - 1)[_teamId - 1] += _score;
    }
  }

  /**
   * This method is called when we want to advance to the next interval, it will
   * call the checkGameOver method since that can only occur when an interval advances
   */
  public void advanceInterval() {
    this.checkGameOver();
    if (this.isGameOver()) {
      outputGame();
    } else addInterval();
  }

  /**
   * This method sets the gameOver and inOvertime attributes; it should be called
   * whenever the user tells us to advance to the next interval (i.e. the next
   * quarter in football)  It should be called before we bump the current interval
   * (i.e. currentInterval should be the interval that we just completed).  Also
   * added that gameOver is set if we're told to quit the game
   */
  private void checkGameOver() {
    gameOver = false;
    if (currentInterval >= numberOfIntervals) {
      gameOver = true;
      if (inATie() && overtimeAllowed) {
        gameOver = false;
      }
    }
    gameOver = (quitGame ? true : gameOver);
    return;
  }

  /**
   * This method returns the current interval (as int)
   *
   * @return currentInterval
   */
  public int getCurrentInterval() {
    return currentInterval;
  }


  /**
   * This method returns the current interval in a string format, like '1st Period' etc..
   *
   * @return String representing interval
   */
  public String getCurrentIntervalText() {
    if (this.gameOver) {
      return "Game over";
    }
    return this.getInterval(currentInterval);
  }

  /**
   * Return the interval passed in as a 'nice' string, if we're in overtime
   * we'll put (OT) after the interval number (and before the interval name)
   *
   * @param _interval The interval we want
   * @return String Human readable interval :)
   */
  public String getInterval(int _interval) {
    String temp;
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
        temp = Integer.toString(_interval) + "th";
        break;
    }
    if (_interval > numberOfIntervals) temp += " (OT)";

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
    return numberOfTeams;
  }

  /**
   * Get score for Team and Interval
   *
   * @param _teamId teamId (integer from 1->numberOfTeams)
   * @param _interval interval you want score for (1->currentInterval)
   * @return The score
   */
  public int getScore(int _teamId, int _interval) {
    if (_teamId > 0 && _teamId <= numberOfTeams && _interval > 0 && _interval <= currentInterval) {
      return intervalScores.get(_interval - 1)[_teamId - 1];
    }
    return Integer.MIN_VALUE;  // Error return smallest int
  }

  /**
   * Return sport name
   *
   * @return String sportName
   */
  public String getSportName() {
    return sportName;
  }

  /**
   * Return flag to indicate if a tie; basically where the highest scores
   * are the same (we use another method to determine it for unique sports
   * like tennis where an interval keeps going to win by 2 strokes)
   *
   * @return boolean Indicating
   */
  private boolean inATie() {
    Iterator it = intervalScores.iterator();
    int maxScore = Integer.MIN_VALUE;
    boolean rtnFlag = false;
    int[] sumOfScores = new int[numberOfTeams];
    while (it.hasNext()) {
      Integer[] intervalScores = (Integer[]) it.next();
      for (int i = 0; i < intervalScores.length; i++) {
        sumOfScores[i] += intervalScores[i];
      }
    }

    // Now iterate over team scores to see if have tie or not
    for (int i = 0; i < numberOfTeams; i++) {
      if (teamTie(maxScore, sumOfScores[i])) {
        rtnFlag = true;
      } else {
        if (sumOfScores[i] > maxScore) {
          maxScore = sumOfScores[i];
          rtnFlag = false;
        }
      }
    }
    return rtnFlag;
  }

  /**
   * Return indicator whether we're in overtime
   *
   * @return boolean true if in overtime, false otherwise
   */
  public boolean inOvertime() {
    return (currentInterval > numberOfIntervals);
  }

  /** Initialize a game
   *
   * @param _sport A String representing name of the sport (i.e. football)
   * @param _teamSport boolean true if this is a team sport (false if individual)
   * @param _numberOfTeams Number of teams in a game
   * @param _numberOfIntervals The number of intervals (i.e. 4 for football)
   * @param _intervalName The interval name (i.e. quarter for football)
   * @param _overtimeAllowed Boolean indicating if game goes into overtime on a tie, a tie
   *                         is considered true if there are two or more teams with a matching
   *                         high score (if needs to be overriden then override method inATie
   *                         in that sports concrete class).
   */
  public void initGame(String _sport,
                       boolean _teamSport,
                       int _numberOfTeams,
                       int _numberOfIntervals,
                       String _intervalName,
                       boolean _overtimeAllowed) {
    sportName = _sport;
    teamSport = _teamSport;
    numberOfTeams = _numberOfTeams;
    numberOfIntervals = _numberOfIntervals;
    intervalName = _intervalName;
    overtimeAllowed = _overtimeAllowed;
    quitGame = false;

    currentInterval = 0;
    if ((numberOfTeams > 0) && (numberOfIntervals > 0)) {
      intervalScores = new ArrayList<Integer[]>();
      addInterval();

      gameOver = false;
    } else {
      gameOver = true;
    }
  }

  /**
   * Return flag to identify if this is a team sport or not.
   *
   * @return boolean flag to identify if this is a team sport
   */
  public boolean isATeamSport() { return teamSport; }

  /**
   * Return indicator if game is over
   *
   * @return boolean true of game is over, false otherwise
   */
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * Return boolean to indicate if interval is over, some sports may end
   * when a condition is met, those sports should override this method.
   *
   * @return boolean indicating if interval is over, defaults to false
   */
  public boolean isIntervalOver(int _interval) { return false; }

  /**
   * Output game shows the teams horizontally across the top with the intervals
   * down vertically.
   */
  public void outputGame() {
    System.out.println("\n\n" + this.getCurrentIntervalText());
    System.out.format("%n%n");

    System.out.format("%n%17s", " ");  // Newline and 15 spaces
    for (int i = 0; i < numberOfTeams; i++) {
      System.out.format("%1s%6s%3d%1s", " ", (teamSport ? "Team" : "Player") , i + 1, " ");
    }

    System.out.format("%n%17s", " ");
    for (int i = 0; i < numberOfTeams; i++) {
      System.out.format("%1s%9s%1s", " ", "---------", " ");
    }

    for (int intervalPos = 0; intervalPos < currentInterval; intervalPos++) {
      System.out.format("%n%-17s", this.getInterval(intervalPos + 1));

      for (int teamPos = 0; teamPos < numberOfTeams; teamPos++) {
        System.out.format("%8d%3s", intervalScores.get(intervalPos)[teamPos], " ");
      }
    }

    // Output the final score
    System.out.format("%n%-17s", (this.isGameOver() ? "Final" : "Current") + " score:");
    for (int teamPos = 0; teamPos < numberOfTeams; teamPos++) {
      System.out.format("%8d%3s", this.runTotal(teamPos), " ");
    }
    System.out.println(" ");
  }

  /**
   * Quit the game
   */
  public void quitGame() {
    quitGame = true;
    gameOver = true;
  }

  /**
   * Return total for the team passed in (use 0 offset i.e. 0 is team 1)
   *
   * @param _teamPosMinusOne
   * @return totalScore for that team
   */
  public int runTotal(int _teamPosMinusOne) {
    int totalScore = 0;
    for (int i = 0; i < currentInterval; i++) {
      totalScore += intervalScores.get(i)[_teamPosMinusOne];
    }

    return totalScore;
  }

  /**
   * Return boolean if team scores are the same, have method so that can override
   * it for specific sports, should pass in the maximum score and then the team you
   * want to compare it to
   *
   * @param _maxScoreSoFar
   * @param _teamScore
   * @return boolean true if tie score, false otherwise
   */
  protected boolean teamTie(int _maxScoreSoFar, int _teamScore) {
    return (_maxScoreSoFar == _teamScore);
  }
}