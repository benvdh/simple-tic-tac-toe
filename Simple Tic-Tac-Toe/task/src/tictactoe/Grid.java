package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid {
    private String fieldsString;
    public static final String EMPTY_GRID = "_________";
    private final char[][] grid;
    private final List<List<FieldState>> fieldStateGrid;

    Grid(String fieldsString) {
        this.fieldsString = fieldsString;
        this.grid = initGridArray();
        this.fieldStateGrid = initFieldStateGrid();
    }

    public String[] getFormattedGrid() {
        String[] formattedGrid = new String[5];
        final String horizontalBoundary = "---------";

        formattedGrid[0] = horizontalBoundary;

        for (int i = 0, j = 1; i < fieldsString.length(); i+=3, j++) {
            // replaceAll trick based on: https://stackoverflow.com/a/4470112/10243474
            String row = new String(grid[j - 1]).replaceAll("\\B", " ");
            formattedGrid[j] = String.format("| %s |", row);
        }

        formattedGrid[4] = horizontalBoundary;

        return formattedGrid;
    }

    private char[][] initGridArray() {
        char[][] gridArray = new char[3][3];

        for (int i = 0, j = 0; i < fieldsString.length(); i+=3, j++) {
            gridArray[j] = fieldsString.substring(i, i + 3).toCharArray();
        }

        return gridArray;
    }

    private List<List<FieldState>> initFieldStateGrid() {
        List<List<FieldState>> fieldStateGrid = new ArrayList<>();

        for (int j = 0; j < grid.length; j++) {
            char[] row = grid[j];
            fieldStateGrid.add(new ArrayList<>());

            for (int i = 0; i < row.length; i++) {
                char fieldStateSymbol = row[i];

                fieldStateGrid
                        .get(j)
                        .add(i, FieldState.findBySymbol(fieldStateSymbol));
            }
        }

        return fieldStateGrid;
    }

    public List<List<FieldState>> getTransposedFieldStateGrid() {
        List<List<FieldState>> transposed = new ArrayList<>(fieldStateGrid.get(0).size());

        // ensure we have all rows initialized beforehand, to prevent IndexOutOfBounds
        for (int i = 0; i < fieldStateGrid.size(); i++) {
            transposed.add(new ArrayList<>(fieldStateGrid.size()));
        }

        for (int i = 0; i < fieldStateGrid.size(); i++) {
            List<FieldState> row = fieldStateGrid.get(i);

            for (int j = 0; j < row.size(); j++) {
                transposed.get(j).add(i, row.get(j));
            }
        }

        return transposed;
    }

    public boolean areInvalidCoordinates(int[] coordinates) {
        return coordinates[0] < 1 ||
                coordinates[0] > grid.length ||
                coordinates[1] < 1 ||
                coordinates[1] > grid.length;
    }

    public boolean isCellOccupied(int[] coordinates) {
        return getCellValue(coordinates) != FieldState.EMPTY.symbol;
    }

    private int[] translateCoordinatesToIndices(int[] coordinates) {
        int[] indices = Arrays.copyOf(coordinates, coordinates.length);
        indices[0] -= 1;
        indices[1] -= 1;

        return indices;
    }

    private char getCellValue(int[] coordinates) {
        int[] indices = translateCoordinatesToIndices(coordinates);
        return grid[indices[0]][indices[1]];
    }

    private void setCellValue(int[] coordinates, FieldState newFieldState) {
        int[] indices = translateCoordinatesToIndices(coordinates);

        // update grid
        this.grid[indices[0]][indices[1]] = newFieldState.symbol;
        // update fieldStateGrid
        this.fieldStateGrid.get(indices[0]).set(indices[1], newFieldState);
    }

    private String getFieldsStringFromGrid() {
        StringBuilder builder = new StringBuilder();

        for (char[] row : grid) {
            builder.append(row);
        }

        return builder.toString();
    }

    public void updateFieldState(int[] coordinates, FieldState newFieldState) {
        setCellValue(coordinates, newFieldState);
        this.fieldsString = getFieldsStringFromGrid();
    }

    public String getFieldsString() {
        return fieldsString;
    }

    public List<List<FieldState>> getFieldStateGrid() {
        return fieldStateGrid;
    }
}
