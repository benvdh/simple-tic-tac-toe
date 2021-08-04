package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Grid grid = new Grid(Grid.EMPTY_GRID);
        FieldState currentPlayer = FieldState.X;
        GridState gameState = GridState.GAME_NOT_FINISHED;

        showGrid(grid);
        while (!gameState.finished) {
            int[] coordinates = getCoordinates(scanner);

            while (grid.areInvalidCoordinates(coordinates) || grid.isCellOccupied(coordinates)) {
                if (grid.areInvalidCoordinates(coordinates)) {
                    System.out.printf("Coordinates should be from 1 to %d!%n", grid.getFieldStateGrid().size());
                    coordinates = getCoordinates(scanner);
                    continue;
                }

                if (grid.isCellOccupied(coordinates)) {
                    System.out.println("This cell is occupied! Choose another one!");
                    coordinates = getCoordinates(scanner);
                }
            }

            grid.updateFieldState(coordinates, currentPlayer);
            showGrid(grid);
            GridAnalyzer analyzer = new GridAnalyzer(grid);
            gameState = analyzer.analyze();
            currentPlayer = switchPlayer(currentPlayer);
        }

        System.out.println(gameState.message);
    }

    private static void showGrid(Grid grid) {
        for (String line: grid.getFormattedGrid()) {
            System.out.println(line);
        }
    }

    private static int[] getCoordinates(Scanner scanner) {
        int[] coordinates = new int[2];
        boolean coordinatesSet = false;

        while(!coordinatesSet) {
            System.out.print("Enter the coordinates: ");

            try {
                int rowNumber = scanner.nextInt();
                int columnNumber = scanner.nextInt();

                coordinates[0] = rowNumber;
                coordinates[1] = columnNumber;

                coordinatesSet = true;

            } catch (InputMismatchException e) {
                // scanner.nextLine() is required to not end up in an infinite loop. See:
                // https://stackoverflow.com/a/32042447/10243474
                scanner.nextLine();
                System.out.println("You should enter numbers!");
            }
        }

        return coordinates;
    }

    private static FieldState switchPlayer(FieldState currentPlayer) {
        return currentPlayer == FieldState.X ? FieldState.O : FieldState.X;
    }
}
