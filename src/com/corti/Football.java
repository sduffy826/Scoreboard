package com.corti;

/**
 * This is the concrete class for scoring a football game, it uses the default
 * logic in the SportsGame (abstract) class so I didn't need to override anything
 * here.
 * Created by duffy_w530 on 3/6/2017.
 */
public class Football extends SportsGame {

  public Football() {
    initGame("Football", true, 2, 4, "Quarter", true);
  }

}
