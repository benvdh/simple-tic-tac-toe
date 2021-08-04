package tictactoe;

public enum GridState {
    X_WINS("X wins", true, FieldState.X),
    O_WINS("O wins", true, FieldState.O),
    DRAW("Draw", true, null),
    IMPOSSIBLE("Impossible", true, null),
    GAME_NOT_FINISHED("Game not finished", false, FieldState.EMPTY);

    String message;
    boolean finished;
    FieldState relatedFieldState;

    GridState(String message, boolean finished, FieldState relatedFieldState) {
        this.message = message;
        this.finished = finished;
        this.relatedFieldState = relatedFieldState;
    }

    public static GridState getGridStateByFieldState(FieldState fieldState) {
        for (GridState state : values()) {
            if (state.relatedFieldState == fieldState) {
                return state;
            }
        }
        return null;
    }
}
