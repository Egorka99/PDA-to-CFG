package pda;

import java.util.List;

public class RightPart {
    private String state;
    private List<String> stackSymbol;

    public RightPart(String state, List<String> stackSymbol) {
        this.state = state;
        this.stackSymbol = stackSymbol;
    }

    public String getState() {
        return state;
    }

    public List<String> getStackSymbols() {
        return stackSymbol;
    }
}
