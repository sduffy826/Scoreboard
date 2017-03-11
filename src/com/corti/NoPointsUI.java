package com.corti;

/**
 * This is a common concrete class to support a game where the user does not enter
 * the points; they just specify the team and we'll bump the points for that team
 * by one.
 * <p>
 * Created by duffy_w530 on 3/8/2017.
 */
public class NoPointsUI extends SportGameUI {

  SportsGame theGame;
  /**
   * Constructor gets a reference to the SportsGame and a flag to indicate if
   * we auto increment it.
   */
  public NoPointsUI(SportsGame _theGame, boolean _autoIncrementGame) {
    super(_theGame, _autoIncrementGame);
    theGame = _theGame;
  }

  /**
   * Return standard scoring text for a game
   *
   * @return String of text to prompt user with
   */
  public String scoringText() {
    if (theGame.isATeamSport())
      return "Enter team id and I'll bump the score by 1 point :)";
    else
      return "Enter player id and I'll bump the score by 1 point :)";
  }

  /**
   * User doesn't enter points so we override the super class
   *
   * @return boolean false
   */
  public boolean userEntersPoints() {
    return false;
  }
}
