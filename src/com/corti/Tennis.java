package com.corti;

/**
 * This class supports the logic to keep track of a tennis match; typically 6 intervals (or games) make up a match
 * but you can run it any way you want; just remember that an interval reflects one game.  And in tennis the scoring
 * is reported as Love (zero), Fifteen (1 pt), Thirty (2 pts), Forty (3 pts) and Game (4 pts) (as happy path).  The
 * player needs to win by two points and any ties from Forty up are called Deuce, if a player scores a point after
 * Deuce then they are said to have the Advantage and other player is at Disadvantage.
 *
 * Cool thing is to support this game I just had to override methods: isIntervalOver (cause it's based on score),
 * outputGame (cause of weird scoring) and created some enums to help with calculating the score.
 */
public class Tennis extends SportsGame {

  // Constructor (we're assume a team not individual sport)
  public Tennis(int _numberOfPlayers) {
    initGame("Tennis", true, _numberOfPlayers, 6, "Game", true);
  }

  /**
   * Return boolean to indicate if interval is over, for tennis this is when
   * teams are at interval 4 (or higher) and difference between the scores is
   * more than one
   *
   * @return boolean indicating if interval is over, defaults to false
   */
  public boolean isIntervalOver(int _interval) {
    boolean allDone = true;
    int numTeams = getNumberOfTeams();
    int team1Id, team2Id, team1Score, team2Score;
    for (int i = 0; i < (numTeams/2); i++) {
      team1Id = i * 2 + 1; // Not zero offset for call getting score
      team2Id = i * 2 + 2;
      team1Score = getScore(team1Id, _interval);
      team2Score = getScore(team2Id, _interval);
      if ( !( (Math.abs(team1Score-team2Score) > 1) && (team1Score > 3 || team2Score > 3) ) ) {
        allDone = false;
      }
    }
    return allDone;
  }

  /**
   * Calculate the total wins for the team passed in
   *
   * @param _teamId team id you want to get wins for
   * @return total number of wins
   */
  public int totalWins(int _teamId) {
    int totalWins = 0;
    int otherTeam = 0;
    int score1, score2;
    if ((_teamId % 2) == 0) // 2 plays 1, 4 plays 3
      otherTeam = _teamId - 1;
    else
      otherTeam = _teamId + 1; // plays higher number
    if (otherTeam <= getNumberOfTeams()) {
      for (int interval = 1; interval <= getCurrentInterval(); interval++) {
        if (isIntervalOver(interval)) {
          // Make sure valid
          score1 = getScore(_teamId, interval);
          score2 = getScore(otherTeam, interval);
          if (score1 > score2) totalWins++;
        }
      }
    }
    return totalWins;
  }

  /**
   * Overrides the standard game output, tennis is a little different
   */
  public void outputGame() {
    int numTeams = getNumberOfTeams();
    int currInterval = getCurrentInterval();

    System.out.println("\n\n" + this.getCurrentIntervalText());
    System.out.format("%n%n");
    System.out.format("%n%15s", " ");  // Newline and 15 spaces
    for (int i = 0; i < numTeams; i++) {
      System.out.format("%1s%7s%5d%1s", " ", (isATeamSport() ? "Team" : "Person"), i + 1, " ");
    }

    System.out.format("%n%15s", " ");
    for (int i = 0; i < numTeams; i++) {
      System.out.format("%1s%12s%1s"," ","------------"," ");
    }

    for (int intervalPos = 0; intervalPos < currInterval; intervalPos++) {
      System.out.format("%n%-15s", this.getInterval(intervalPos + 1));

      // Assumed that team1 plays team2 (really teams are people our a double)

      for (int i = 0; i < (numTeams/2); i++) {
        int team1Id = i * 2 + 1; // Not zero offset for call getting score
        int team2Id = i * 2 + 2;
        int team1Score = getScore(team1Id, intervalPos+1);
        int team2Score = getScore(team2Id, intervalPos+1);
        System.out.format("%1s%12s%1s"," ",TennisGameScore.getScore(team1Score, team2Score).name()," ");
        System.out.format("%1s%12s%1s"," ",TennisGameScore.getScore(team2Score, team1Score).name()," ");
      }

      for (int teamPos = 0; teamPos < numTeams; teamPos++) {
        //System.out.format("%8d%3s", intervalScores.get(intervalPos)[teamPos], " ");
      }
    }

    // Output the final score
    System.out.format("%n%-15s", (this.isGameOver() ? "Final" : "Current") + " score:");
    for (int teamPos = 0; teamPos < numTeams; teamPos++) {
      System.out.format("%8d%3s", this.totalWins(teamPos+1), " ");
    }
    System.out.println(" ");
  }

  // Enum for 'happy path' tennis scores
  private enum TennisScore { Love, Fifteen, Thirty, Forty, Game; }

  // Enum for possible game scores, this enum calculates the value
  // for team1 based on the current score with team2
  private enum TennisGameScore {
    Love, Fifteen, Thirty, Forty, Game, Deuce, Disadvantage, Advantage;

    // Return Status of team1 in relation to team2, typically call this with team1, team2 to get team1's status
    // then call it reversing the parms so that you get team2's status in relation to team1 :)
    public static TennisGameScore getScore(int _team1, int _team2) {

      // A common return value is when team1 is at level 'Game' or below so set here; we'll override in body of
      // method below (if applicable).
      TennisGameScore rtnValue = TennisGameScore.Love;
      if (_team1 <= TennisScore.Game.ordinal()) {
        // First 5 ordinal values are same between TennisScore and TennisGameScore
        rtnValue = TennisGameScore.values()[_team1];
      }

      if ((_team1 - _team2) > 1)  {
        if (_team1 >= TennisScore.Game.ordinal())  // Team1 winner?
          rtnValue = TennisGameScore.Game;
      }
      else
        if ((_team2 - _team1) > 1) {
          if (_team1 >= TennisScore.Forty.ordinal()) // Team2 winner and team1 has status 'disadvantage'
            rtnValue = TennisGameScore.Disadvantage;
        }
        else
          if (_team1 == _team2) {
            if ( _team1 >= TennisScore.Forty.ordinal())  // Deuce at Forty or and tie above that
              rtnValue =  TennisGameScore.Deuce;
          }
          else {
            // Score different by one point, if we've gone past 40 then it's advantage or disadvantage
            if (_team1 > _team2 && _team1 >= TennisScore.Forty.ordinal())
              rtnValue = TennisGameScore.Advantage;
            else
              if (_team2 > _team1 && _team1 >= TennisScore.Forty.ordinal())
                rtnValue = TennisGameScore.Disadvantage;
          }
      return rtnValue;
    }
  }
}
