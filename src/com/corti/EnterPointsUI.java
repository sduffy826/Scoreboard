package com.corti;

/**
 * Common concrete class for SportGameUI, this one doesn't override any of the
 * methods/attributes in the base (abstract) class.
 * <p>
 * Created by duffy_w530 on 3/8/2017.
 */
public class EnterPointsUI extends SportGameUI {

  /**
   * Constructor gets passed SportGame reference and auto increment indicator,
   * just invoke super(..)
   *
   * @param _theGame              Object reference to the game we're scoring
   * @param _autoIncrementingGame boolean true if we auto increment the interval
   *                              and false otherwise (auto increment is one
   *                              sports where a condition (like score) determines
   *                              when an interval is over).
   */
  public EnterPointsUI(SportsGame _theGame, boolean _autoIncrementingGame) {
    super(_theGame, _autoIncrementingGame);
  }
}
