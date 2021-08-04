package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class GridAnalyzer {
    final private Grid grid;
    final private List<List<FieldState>> winningRowsResult;
    final private List<List<FieldState>> winningColumnsResult;
    final private List<List<FieldState>> winningDiagonalsResult;

    public GridAnalyzer(Grid grid) {
        this.grid = grid;
        this.winningRowsResult = detectWinningRows(grid.getFieldStateGrid());
        this.winningColumnsResult = detectWinningRows(grid.getTransposedFieldStateGrid());
        this.winningDiagonalsResult = detectWinningDiagonals();
    }

    public GridState analyze() {
        List<List<List<FieldState>>> analysisResults = Arrays.asList(
                winningRowsResult,
                winningColumnsResult,
                winningDiagonalsResult
        );

        if (isGameInProgress() && !hasImpossibleState()) {
            return GridState.GAME_NOT_FINISHED;
        }

        if (hasImpossibleState()) {
            return GridState.IMPOSSIBLE;
        }

        if (isDraw()) {
            return GridState.DRAW;
        }

        for (List<List<FieldState>> analysisResult : analysisResults) {
            GridState winner = detectWinner(analysisResult);

            if (winner != null) {
                return winner;
            }
        }

        return null;
    }

    private static GridState detectWinner(List<List<FieldState>> analysisResult) {
        if (!analysisResult.isEmpty()) {
            return GridState.getGridStateByFieldState(
                    analysisResult.get(0).get(0)
            );
        } else {
            return null;
        }
    }

    private static List<List<FieldState>> detectWinningRows(List<List<FieldState>> fieldStateGrid) {
        List<List<FieldState>> winningRows = new ArrayList<>();

        for (List<FieldState> row : fieldStateGrid) {
            EnumSet<FieldState> uniqueValuesInRow = EnumSet.copyOf(row);

            if (uniqueValuesInRow.size() == 1 && !uniqueValuesInRow.contains(FieldState.EMPTY)) {
                winningRows.add(row);
            }
        }
        return winningRows;
    }
    
    private List<List<FieldState>> detectWinningDiagonals() {
        List<List<FieldState>> winningDiagonals = new ArrayList<>();
        List<FieldState> forwardDiagonal = new ArrayList<>();
        List<FieldState> backwardDiagonal = new ArrayList<>();
        List<List<FieldState>> fieldStateGrid = grid.getFieldStateGrid();
        int gridSize = fieldStateGrid.size();

        // extract diagonals
        for (int i = 0, j = gridSize-1;
             i < gridSize;
             i++, j--
        ) {
            forwardDiagonal.add(fieldStateGrid.get(i).get(i));
            backwardDiagonal.add(fieldStateGrid.get(i).get(j));
        }
            EnumSet<FieldState> uniqueValuesForward = EnumSet.copyOf(forwardDiagonal);
            EnumSet<FieldState> uniqueValuesBackward = EnumSet.copyOf(backwardDiagonal);

        if (uniqueValuesForward.size() == 1 && !uniqueValuesForward.contains(FieldState.EMPTY)) {
            winningDiagonals.add(forwardDiagonal);
        }

        if (uniqueValuesBackward.size() == 1 && !uniqueValuesBackward.contains(FieldState.EMPTY)) {
            winningDiagonals.add(backwardDiagonal);
        }
        
        return winningDiagonals;
    }

    private boolean hasImpossibleState() {
        long xCount = getCountByFieldState(FieldState.X);
        long oCount = getCountByFieldState(FieldState.O);
        long diff = xCount > oCount ? xCount - oCount : oCount - xCount;

        return diff >= 2 ||
                winningRowsResult.size() > 1 ||
                winningColumnsResult.size() > 1 ||
                winningDiagonalsResult.size() > 1;
    }

    private boolean isDraw() {
        boolean hasNoEmptyCells = !hasEmptyCells();
        boolean hasNoWinners = hasNoWinners();

        return hasNoEmptyCells && hasNoWinners;

    }

    private boolean hasEmptyCells() {
        return getCountByFieldState(FieldState.EMPTY) > 0;
    }

    private boolean hasNoWinners() {
        return winningRowsResult.isEmpty() && winningColumnsResult.isEmpty() && winningDiagonalsResult.isEmpty();
    }

    private boolean isGameInProgress() {
        return hasEmptyCells() && hasNoWinners();
    }

    private long getCountByFieldState(FieldState state) {
        return grid
                .getFieldsString()
                .chars()
                .filter(ch -> ch == state.symbol)
                .count();
    }
}
