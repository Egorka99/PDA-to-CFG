package pda;

public class LeftPart {
    private String state;
    private String symbol;
    private String stackSymbol;

    public LeftPart(String state, String symbol, String stackSymbol) {
        this.state = state;
        this.symbol = symbol;
        this.stackSymbol = stackSymbol;
    }

    public String getState() {
        return state;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStackSymbol() {
        return stackSymbol;
    }
}


