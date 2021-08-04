package tictactoe;

public enum FieldState {
    O('O'),
    X('X'),
    EMPTY('_');

    char symbol;

    FieldState(char symbol) {
        this.symbol = symbol;
    }

    public static FieldState findBySymbol(char symbol) {
        for (FieldState state : values()) {
            if (state.symbol == symbol) {
                return state;
            }
        }
        return null;
    }
}
