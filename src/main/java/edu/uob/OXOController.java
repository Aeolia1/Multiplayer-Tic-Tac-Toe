package edu.uob;
import edu.uob.OXOMoveException.*;

class OXOController {
    OXOModel gameModel;
    private String command;
    private int rowIndex, colIndex;
    private OXOPlayer currentPlayer;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException {
        //check if game is end
        if (gameModel.isGameDrawn() || (gameModel.getWinner()!=null)){
            return;
        }
        this.command = command;
        validateCommand();
        int currentNum = gameModel.getCurrentPlayerNumber();
        //detect game states: draw, win or continue
        if (isWin()){
            gameModel.setWinner(currentPlayer);
            return;
        }
        if (isDraw()){
            gameModel.setGameDrawn();
            return;
        }
        //set number for next player
        int playerNum = gameModel.getNumberOfPlayers();
        if (currentNum == playerNum-1){
            gameModel.setCurrentPlayerNumber(0);
        }
        else{
            gameModel.setCurrentPlayerNumber(currentNum+1);
        }
    }

    public void addRow() {
        gameModel.addRow();
    }
    public void removeRow() {
        gameModel.removeRow();
    }
    public void addColumn() {
        gameModel.addColumn();
    }
    public void removeColumn() {
        gameModel.removeColumn();
    }
    public void increaseWinThreshold() {
        gameModel.setWinThreshold(gameModel.getWinThreshold()+1);
    }
    public void decreaseWinThreshold() {
        gameModel.setWinThreshold(gameModel.getWinThreshold()-1);
    }

    public boolean isDraw() {
        int row = gameModel.getNumberOfRows();
        int col = gameModel.getNumberOfColumns();
        for(int i=0; i<row; i++){
            for(int j=0; j<col; j++){
                if (gameModel.getCellOwner(i,j) == null){
                    return false; //exit the method
                }
            }
        }
        return true;
    }

    public boolean isWin(){
        boolean cnt1=checkVertical();
        boolean cnt2=checkHorizontal();
        boolean cnt3=checkDiagonals();
        return cnt1 || cnt2 || cnt3;
    }

    public boolean checkVertical () {
        int winThreshold = gameModel.getWinThreshold();
        int row= gameModel.getNumberOfRows();
        int cnt=0;
        for (int i=0; i<row; i++){
            if (gameModel.getCellOwner(i,colIndex) == currentPlayer){
                cnt++;
                if (cnt==winThreshold){
                    return true;
                }
            }
            else{
                cnt=0;
            }
        }
        return false;
    }

    public boolean checkHorizontal () {
        int winThreshold = gameModel.getWinThreshold();
        int col = gameModel.getNumberOfColumns();
        int cnt=0;
        for (int i=0; i<col; i++){
            if (gameModel.getCellOwner(rowIndex,i) == currentPlayer){
                cnt++;
                if (cnt==winThreshold){
                    return true;
                }
            }
            else{
                cnt=0;
            }
        }
        return false;
    }

    public boolean checkDiagonals(){
        int winThreshold = gameModel.getWinThreshold();
        int row = gameModel.getNumberOfRows();
        int col = gameModel.getNumberOfColumns();
        //from top-left to bottom-right
        int cnt1=0;
        for (int i=1-winThreshold; i<winThreshold; i++){
            if ((rowIndex+i>=0) && (colIndex+i>=0) && (rowIndex+i<row) && (colIndex+i<col)){
                if (gameModel.getCellOwner(rowIndex+i,colIndex+i) == currentPlayer){
                    cnt1++;
                }
                else{
                    cnt1=0;
                }
                if (cnt1==winThreshold){
                    return true;
                }
            }
        }
        //from bottom-left to top-right
        int cnt2=0;
        for (int i=1-winThreshold; i<winThreshold; i++){
            if ((rowIndex+i>=0) && (colIndex-i>=0) && (rowIndex+i<=row-1) && (colIndex-i<=col-1)){
                if (gameModel.getCellOwner(rowIndex+i,colIndex-i) == currentPlayer){
                    cnt2++;
                }
                else{
                    cnt2=0;
                }
                if (cnt2==winThreshold){
                    return true;
                }
            }
        }
        return false;
    }

    public void validateCommand() throws OXOMoveException
    {
        //check for input length
        if(command.length() != 2) {
            throw new OXOMoveException.InvalidIdentifierLengthException(command.length());
        }

        //check for input type
        if(!Character.isLetter(command.charAt(0))){
            throw new OXOMoveException.InvalidIdentifierCharacterException(RowOrColumn.ROW,command.charAt(0));
        }
        else{
            String newStr = command.toLowerCase();
            rowIndex = newStr.charAt(0)-'a';
        }

        if(!Character.isDigit(command.charAt(1))){
            throw new OXOMoveException.InvalidIdentifierCharacterException(RowOrColumn.COLUMN,command.charAt(1));
        }
        else{
            colIndex = command.charAt(1)-'1';
        }

        //check if cell is outside the range
        if ((rowIndex>=gameModel.getNumberOfRows()) || (rowIndex>=9)){
            throw new OXOMoveException.OutsideCellRangeException(RowOrColumn.ROW,rowIndex);
        }
        if ((colIndex<0) || (colIndex>=gameModel.getNumberOfColumns())){
            throw new OXOMoveException.OutsideCellRangeException(RowOrColumn.COLUMN,colIndex);
        }

        //check if cell has already been taken
        if(gameModel.getCellOwner(rowIndex,colIndex)!=null){
            throw new OXOMoveException.CellAlreadyTakenException(rowIndex,colIndex);
        }
        else{
            int currentNum = gameModel.getCurrentPlayerNumber();
            currentPlayer = gameModel.getPlayerByNumber(currentNum);
            gameModel.setCellOwner(rowIndex,colIndex,currentPlayer);
        }
    }
}
