package cfg;

import java.util.List;

public class CFGRule {
    private Triple leftPart;
    private String terminal;
    private List<Triple> rightPart;

    public CFGRule(Triple leftPart, String terminal, List<Triple> rightPart) {
        this.leftPart = leftPart;
        this.terminal = terminal;
        this.rightPart = rightPart;
    }

    public Triple getLeftPart() {
        return leftPart;
    }

    public void setLeftPart(Triple leftPart) {
        this.leftPart = leftPart;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public List<Triple> getRightPart() {
        return rightPart;
    }

    public void setRightPart(List<Triple> rightPart) {
        this.rightPart = rightPart;
    }

    @Override
    public String toString() {
        StringBuilder cfgRule = new StringBuilder();
        cfgRule.append(leftPart != null ? leftPart : "").append(" -> ").append(terminal != null ? terminal : "");
        rightPart.forEach(r -> cfgRule.append(r != null ? r : ""));

        return cfgRule.toString();

    }
}
