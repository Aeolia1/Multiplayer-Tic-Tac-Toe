package edu.uob;
import java.util.ArrayList;

class OXOModel {
  ArrayList<ArrayList<OXOPlayer>> cells = new ArrayList<ArrayList<OXOPlayer>>();
  ArrayList<OXOPlayer> players = new ArrayList<OXOPlayer>();
  private int currentPlayerNumber;
  private OXOPlayer winner;
  private boolean gameDrawn;
  private int winThreshold;

  public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
    winThreshold = winThresh;
    for (int i=0; i<numberOfRows; i++){ //row
      ArrayList<OXOPlayer> Row = new ArrayList<OXOPlayer>();
      for (int j=0; j<numberOfColumns; j++){ //col
        Row.add(null);
      }
      cells.add(Row);
    }
    setWinThreshold(winThresh);
  }

  public int getNumberOfPlayers() {
    return players.size();
  }

  public void addPlayer(OXOPlayer player) {
    players.add(player);
  }

  public OXOPlayer getPlayerByNumber(int number) {
    return players.get(number);
  }

  public OXOPlayer getWinner() {
    return winner;
  }

  public void setWinner(OXOPlayer player) {
    winner = player;
  }

  public int getCurrentPlayerNumber() {
    return currentPlayerNumber;
  }

  public void setCurrentPlayerNumber(int playerNumber) {
    currentPlayerNumber = playerNumber;
  }

  public int getNumberOfRows() {
    return cells.size();
  }

  public int getNumberOfColumns() {
    return cells.get(0).size();
  }

  public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
    return cells.get(rowNumber).get(colNumber);
  }

  public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
    cells.get(rowNumber).set(colNumber,player);
  }

  public void setWinThreshold(int winThresh) {
    winThreshold = winThresh;
  }

  public int getWinThreshold() {
    return winThreshold;
  }

  public void setGameDrawn() {
    gameDrawn = true;
  }

  public boolean isGameDrawn() {
    return gameDrawn;
  }

  public void addRow() {
    ArrayList<OXOPlayer> Row = new ArrayList<>();
    for (int j=0; j<cells.get(0).size(); j++){ //col
      Row.add(null);
    }
    cells.add(Row);
  }

  public void removeRow() {
    cells.remove(cells.size()-1);

  }

  public void addColumn() {
    for (ArrayList<OXOPlayer> i : cells){
      i.add(null);
    }
  }

  public void removeColumn() {
    for (ArrayList<OXOPlayer> i : cells){
      i.remove(cells.get(0).size()-1);
    }
  }

}
