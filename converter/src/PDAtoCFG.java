import pda.LeftPart;
import pda.RightPart;
import pda.Transition;

import java.util.*;

public class PDAtoCFG {
    private List<Transition> pdaTransitions;
    private Set<String> states;

    public PDAtoCFG(List<Transition> pdaTransitions) {
        this.pdaTransitions = pdaTransitions;

        states = new HashSet<>();

        for (Transition transition : pdaTransitions) {
            states.add(transition.getLeftPart().getState());
            states.add("qf");
        }

    }

    private List<String> getInitialGrammar() {
        List<String> grammar = new ArrayList<>();
        grammar.add("S -> [q0,z0,q]");

        for (Transition transition : pdaTransitions) {
            StringBuilder rule = new StringBuilder();
            if (!transition.getRightPart().getStackSymbols().contains("e")) {
                rule
                        .append("[").append(transition.getLeftPart().getState()).append(",")
                        .append(transition.getLeftPart().getStackSymbol()).append(",")
                        .append("q").append("]")
                        .append(" -> ")
                        .append(transition.getLeftPart().getSymbol());

                rule.append("[").append(transition.getRightPart().getState()).append(",").append(transition.getRightPart().getStackSymbols().get(0)).append(",").append("p").append("]");
                rule.append("[").append("p").append(",").append(transition.getRightPart().getStackSymbols().get(1)).append(",").append("q").append("]");
            } else {
                rule.append("[")
                        .append(transition.getLeftPart().getState()).append(",")
                        .append(transition.getLeftPart().getStackSymbol()).append(",")
                        .append(transition.getRightPart().getState()).append("]");
                rule.append(" -> ");
                rule.append(transition.getLeftPart().getSymbol());
            }
            grammar.add(rule.toString());
        }
        return grammar;
    }

    public List<String> getFullGrammar(List<String> initialGrammar) {
        List<String> grammarWithoutP = new ArrayList<>();
        List<String> grammarWithoutPQ = new ArrayList<>();
        List<String> statesList = new ArrayList<>(states);

        for (String rule : initialGrammar) {
            if (rule.contains("p")) {
                for (int i = 0; i < states.size(); i++) {
                    grammarWithoutP.add(rule.replaceAll("p", statesList.get(i)));
                }
            } else {
                grammarWithoutP.add(rule);
            }
        }

//        for (String rule : grammarWithoutP) {
//            for (int i = 0; i < rule.length() - 1; i++) {
//                if (rule.charAt(i) == 'q') {
//                    if (!Character.isDigit(rule.charAt(i + 1)) && rule.charAt(i + 1) != 'f') {
//                        for (String state : statesList) {
//                            ruleBuilder.replace(i, i + 1, state);
//                        }
//                    }
//                }
//            }
//            grammarWithoutPQ.add(ruleBuilder.toString());
//        }

        for (String rule : grammarWithoutP) {
            StringBuilder ruleBuilder;
                for (String state : statesList) {
                    ruleBuilder = new StringBuilder(rule);
                    for (int i = 0; i < rule.length() - 1; i++) {
                        if (rule.charAt(i) == 'q') {
                            if (!Character.isDigit(rule.charAt(i + 1)) && rule.charAt(i + 1) != 'f') {
                                ruleBuilder.replace(i, i + 1, state);
                            }
                        }
                    }
                    grammarWithoutPQ.add(ruleBuilder.toString());
                }
        }


        return grammarWithoutPQ;

    }

    public static void main(String[] args) {
        Transition transition = new Transition(new LeftPart("q0", "a", "z0"), new RightPart("q0", Arrays.asList("a", "z0")));
        Transition transition1 = new Transition(new LeftPart("q0", "a", "a"), new RightPart("q0", Arrays.asList("a", "a")));
        Transition transition2 = new Transition(new LeftPart("q0", "b", "a"), new RightPart("q1", Collections.singletonList("e")));
        Transition transition3 = new Transition(new LeftPart("q1", "b", "a"), new RightPart("q1", Collections.singletonList("e")));
        Transition transition4 = new Transition(new LeftPart("q1", "e", "a"), new RightPart("q2", Collections.singletonList("e")));
        Transition transition5 = new Transition(new LeftPart("q2", "e", "a"), new RightPart("q2", Collections.singletonList("e")));
        Transition transition6 = new Transition(new LeftPart("q2", "e", "z0"), new RightPart("qf", Collections.singletonList("e")));
        PDAtoCFG pdAtoCFG = new PDAtoCFG(Arrays.asList(transition, transition1, transition2, transition3, transition4, transition5, transition6));

        for (String s : pdAtoCFG.getFullGrammar(pdAtoCFG.getInitialGrammar())) {
            System.out.println(s);
        }


    }


}
