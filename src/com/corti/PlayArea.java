package com.corti;

/**
 * Created by duffy_w530 on 3/7/2017.
 */
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class PlayArea {

  private List<Integer[]> someList;
  private final int NUMTEAMS = 5;
  private final int NUMINTERVALS = 4;
  private int currentInterval = 0;
  private int numberOfIntervals = 0;
  public PlayArea() {
    someList = new ArrayList<Integer[]>();
    addInterval();
  }

  public void addInterval() {
    Integer[] interval = new Integer[NUMTEAMS];
    for (int i = 0; i < NUMTEAMS; i++) {
      interval[i] = 0;
    }
    someList.add(interval);
    numberOfIntervals++;
    currentInterval++;
  }

  public void addScore(int _teamId, int _score) {
    if (_teamId > 0 && _teamId <= NUMTEAMS) {
      someList.get(currentInterval-1)[_teamId-1] += _score;
    }
  }

  public enum TennisScore {
    Love, Fifteen, Thirty, Forty, Game }
    /*
    Fifteen(1),
    Thirty(2),
    Forty(3),
    Game(4);
  }
*/

  public void dumpScores() {
    Iterator it = someList.iterator();
    int intCount = 0;
    while (it.hasNext()) {
      Integer[] intervalScores = (Integer[])it.next();
      System.out.format("%n%s%3d","Interval",++intCount);
      for (int i = 0; i < intervalScores.length; i++) {
        System.out.format("%4d",intervalScores[i]);
      }
    }
  }

  public static void main(String[] args) {
    // write your code here
    PlayArea myPlayArea = new PlayArea();
    myPlayArea.addScore(1, 7);
    myPlayArea.addScore(3,2);
    myPlayArea.addInterval();
    myPlayArea.addScore(2, 1);
    myPlayArea.addScore(2, 3);
    myPlayArea.dumpScores();

    System.out.println("\n\n ");
    for (int i = 0; i < 5; i++) {
      System.out.println(TennisScore.values()[i]);
    }
    System.out.println("-------");
    System.out.println(TennisScore.Game.name());
    System.out.println(TennisScore.Forty.ordinal());

    TennisScore tg = TennisScore.values()[3]; //.ordinal();
    System.out.println(TennisScore.values()[3]);
    System.out.println(TennisScore.valueOf("Love").ordinal());


  }


}
