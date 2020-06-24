package pda;

import pda.LeftPart;
import pda.RightPart;

public class Transition {
    private LeftPart leftPart;
    private RightPart rightPart;

    public Transition(LeftPart leftPart, RightPart rightPart) {
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }

    public LeftPart getLeftPart() {
        return leftPart;
    }

    public RightPart getRightPart() {
        return rightPart;
    }

}
