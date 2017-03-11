package com.corti;

/**
 * This is the concrete class for scoring a golf game, it uses the default
 * logic in the SportsGame (abstract) class so I didn't need to override anything
 * here.
 * Created by duffy_w530 on 3/10/2017.
 */
public class Golf extends SportsGame {

  public Golf(int _numPlayers) {
    initGame("Golf", false, _numPlayers, 18, "Hole", false);
  }
}
