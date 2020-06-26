import cfg.CFGRule;
import cfg.Triple;
import com.rits.cloning.Cloner;
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

    private List<CFGRule> getInitialGrammar() {
        List<CFGRule> grammar = new ArrayList<>();
        grammar.add(new CFGRule(new Triple("S", null, null), null, Arrays.asList(new Triple("q0", "z0", "q"))));

        for (Transition transition : pdaTransitions) {
            CFGRule cfgRule;
            if (!transition.getRightPart().getStackSymbols().contains("e")) {
                cfgRule = new CFGRule(
                        new Triple(transition.getLeftPart().getState(), transition.getLeftPart().getStackSymbol(), "q"),
                        transition.getLeftPart().getSymbol(),
                        Arrays.asList(
                                new Triple(transition.getRightPart().getState(), transition.getRightPart().getStackSymbols().get(0), "p"),
                                new Triple("p", transition.getRightPart().getStackSymbols().get(1), "q")
                        )
                );
            } else {
                cfgRule = new CFGRule(
                        new Triple(transition.getLeftPart().getState(), transition.getLeftPart().getStackSymbol(), transition.getRightPart().getState()),
                        transition.getLeftPart().getSymbol(),
                        null
                );
            }
            grammar.add(cfgRule);
        }
        return grammar;
    }

    public List<CFGRule> getFullGrammar(List<CFGRule> initialGrammar) throws CloneNotSupportedException {
        List<CFGRule> grammarWithoutP = new ArrayList<>();
        List<CFGRule> grammarWithoutPQ = new ArrayList<>();
        List<String> statesList = new ArrayList<>(states);

        for (CFGRule rule : initialGrammar) {
            for (String state : statesList) {

                Cloner cloner = new Cloner();
                CFGRule initialRule = cloner.deepClone(rule);

                if (initialRule.getLeftPart().getStackSymbol() != null) {
                    if (initialRule.getLeftPart().getStackSymbol().equals("p")) {
                        initialRule.getLeftPart().setStackSymbol(state);
                    }
                }
                if (initialRule.getLeftPart().getStackSymbol() != null) {
                    if (initialRule.getLeftPart().getState().equals("p")) {
                        initialRule.getLeftPart().setState(state);
                    }
                }

                if (initialRule.getRightPart() != null) {
                    for (Triple triple : initialRule.getRightPart()) {
                        if (triple.getStackSymbol() != null) {
                            if (triple.getStackSymbol().equals("p")) {
                                triple.setStackSymbol(state);
                            }
                        }
                        if (triple.getSymbol() != null) {
                            if (triple.getState().equals("p")) {
                                triple.setState(state);
                            }
                        }
                    }
                }
                grammarWithoutP.add(initialRule);
            }
        }

//        for (CFGRule rule : grammarWithoutP) {
//            for (String state : statesList) {
//                CFGRule oldRule = new CFGRule(rule.getLeftPart(), rule.getNonTerminal(), rule.getRightPart());
//                if (rule.getLeftPart().getStackSymbol() != null) {
//                    if (rule.getLeftPart().getStackSymbol().equals("q")) {
//                        rule.getLeftPart().setStackSymbol(state);
//                    }
//                }
//                if (rule.getLeftPart().getStackSymbol() != null) {
//                    if (rule.getLeftPart().getSymbol().equals("q")) {
//                        rule.getLeftPart().setSymbol(state);
//                    }
//                }
//
//                if (rule.getRightPart() != null) {
//                    for (Triple triple : rule.getRightPart()) {
//                        if (triple.getStackSymbol() != null) {
//                            if (triple.getStackSymbol().equals("q")) {
//                                triple.setStackSymbol(state);
//                            }
//                        }
//                        if (triple.getSymbol() != null) {
//                            if (triple.getSymbol().equals("q")) {
//                                triple.setSymbol(state);
//                            }
//                        }
//                    }
//                }
//                if (!oldRule.equals(rule)) grammarWithoutPQ.add(rule);
//            }
//        }

        return grammarWithoutP;


    }

    public static void main(String[] args) throws CloneNotSupportedException {
        Transition transition = new Transition(new LeftPart("q0", "a", "z0"), new RightPart("q0", Arrays.asList("a", "z0")));
        Transition transition1 = new Transition(new LeftPart("q0", "a", "a"), new RightPart("q0", Arrays.asList("a", "a")));
        Transition transition2 = new Transition(new LeftPart("q0", "b", "a"), new RightPart("q1", Collections.singletonList("e")));
        Transition transition3 = new Transition(new LeftPart("q1", "b", "a"), new RightPart("q1", Collections.singletonList("e")));
        Transition transition4 = new Transition(new LeftPart("q1", "e", "a"), new RightPart("q2", Collections.singletonList("e")));
        Transition transition5 = new Transition(new LeftPart("q2", "e", "a"), new RightPart("q2", Collections.singletonList("e")));
        Transition transition6 = new Transition(new LeftPart("q2", "e", "z0"), new RightPart("qf", Collections.singletonList("e")));
        PDAtoCFG pdAtoCFG = new PDAtoCFG(Arrays.asList(transition, transition1, transition2, transition3, transition4, transition5, transition6));

        for (CFGRule s : pdAtoCFG.getFullGrammar(pdAtoCFG.getInitialGrammar())) {
            System.out.println(s);
        }
    }


}
