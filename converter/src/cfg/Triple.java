package cfg;

public class Triple {
    private String state;
    private String symbol;
    private String stackSymbol;

    public Triple(String state, String symbol, String stackSymbol) {
        this.state = state;
        this.symbol = symbol;
        this.stackSymbol = stackSymbol;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getStackSymbol() {
        return stackSymbol;
    }

    public void setStackSymbol(String stackSymbol) {
        this.stackSymbol = stackSymbol;
    }

    @Override
    public String toString() {
        StringBuilder triple = new StringBuilder();
        triple.append("[")
                .append( state != null ? state + "," : "")
                .append( symbol != null ? symbol + "," : "")
                .append( stackSymbol != null ? stackSymbol: "").append("]");
        return triple.toString();
    }
}
