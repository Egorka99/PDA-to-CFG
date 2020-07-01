package cfg;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple triple = (Triple) o;
        return Objects.equals(state, triple.state) &&
                Objects.equals(symbol, triple.symbol) &&
                Objects.equals(stackSymbol, triple.stackSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, symbol, stackSymbol);
    }
}
