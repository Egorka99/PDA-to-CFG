import cfg.CFGRule;
import cfg.Triple;
import com.beatrix.ChomskyNormalForm;
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

    public String printSteps() {
        StringBuilder output = new StringBuilder();
        output.append("1.Context-Free grammar\r\n");
        this.getInitialGrammar().forEach(r -> output.append(r).append("\r\n"));
        output.append("\r\n2.Replace p,q by {q1,q2,q3..qf}\r\n");
        this.getFullGrammar(this.getInitialGrammar()).forEach(r -> output.append(r).append("\r\n"));
        output.append("\r\n3.Remove useless nonterminal\r\n");
        this.removeUselessNonterminal(this.getFullGrammar(this.getInitialGrammar())).forEach(r -> output.append(r).append("\r\n"));
        output.append("\r\n4.Remove eps rules\r\n");
        output.append(this.removeEpsRules(this.removeUselessNonterminal(this.getFullGrammar(this.getInitialGrammar()))));

        return output.toString();
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

    private List<CFGRule> getFullGrammar(List<CFGRule> initialGrammar) {
        List<CFGRule> grammarWithoutP = new ArrayList<>();
        List<CFGRule> grammarWithoutPQ = new ArrayList<>();
        List<String> statesList = new ArrayList<>(states);

        for (CFGRule rule : initialGrammar) {
            boolean hasChanges = false;
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
                if (!initialRule.equals(rule)) {
                    hasChanges = true;
                    grammarWithoutP.add(initialRule);
                }
            }
            if (!hasChanges) grammarWithoutP.add(rule);
        }

        for (CFGRule rule : grammarWithoutP) {
            boolean hasChanges = false;
            for (String state : statesList) {
                Cloner cloner = new Cloner();
                CFGRule initialRule = cloner.deepClone(rule);

                if (initialRule.getLeftPart().getStackSymbol() != null) {
                    if (initialRule.getLeftPart().getStackSymbol().equals("q")) {
                        initialRule.getLeftPart().setStackSymbol(state);
                    }
                }
                if (initialRule.getLeftPart().getStackSymbol() != null) {
                    if (initialRule.getLeftPart().getState().equals("q")) {
                        initialRule.getLeftPart().setState(state);
                    }
                }

                if (initialRule.getRightPart() != null) {
                    for (Triple triple : initialRule.getRightPart()) {
                        if (triple.getStackSymbol() != null) {
                            if (triple.getStackSymbol().equals("q")) {
                                triple.setStackSymbol(state);
                            }
                        }
                        if (triple.getSymbol() != null) {
                            if (triple.getState().equals("q")) {
                                triple.setState(state);
                            }
                        }
                    }
                }
                if (!initialRule.equals(rule)) {
                    hasChanges = true;
                    grammarWithoutPQ.add(initialRule);
                }
            }
            if (!hasChanges) grammarWithoutPQ.add(rule);
        }
        return grammarWithoutPQ;
    }

    private List<CFGRule> removeUselessNonterminal(List<CFGRule> fullGrammar) {
        List<CFGRule> userfulRules = new ArrayList<>();

        for (CFGRule cfgRule : fullGrammar) {
            if (cfgRule.getRightPart() == null) {
                userfulRules.add(cfgRule);
            }
        }

        fullGrammar.removeAll(userfulRules);

        Cloner cloner = new Cloner();
        List<CFGRule> grammarWithoutUseless = cloner.deepClone(fullGrammar);
        List<CFGRule> terminalRules = cloner.deepClone(userfulRules);

        Collections.reverse(fullGrammar);
        for (CFGRule cfgRule : fullGrammar) {
            boolean isUseful = true;
            for (Triple triple : cfgRule.getRightPart()) {
                boolean isTripleEqual = false;
                for (CFGRule usefulRule : userfulRules) {
                    if (triple.equals(usefulRule.getLeftPart())) {
                        isTripleEqual = true;
                        break;
                    }
                }
                if (!isTripleEqual) {
                    isUseful = false;
                    grammarWithoutUseless.remove(cfgRule);
                    break;
                }
            }
            if (isUseful) userfulRules.add(cfgRule);
        }

        grammarWithoutUseless.addAll(terminalRules);
        return grammarWithoutUseless;
    }

    private String removeEpsRules(List<CFGRule> grammarWithoutUseless) {
        List<String> grammar = new ArrayList<>();
        grammarWithoutUseless.forEach(r -> grammar.add(r.toString().replace("-> ", "").replace("e", "0")));

        StringBuilder grammarInString = new StringBuilder();
        grammar.forEach(r -> grammarInString.append(r).append("\r\n"));
        ChomskyNormalForm chomskyNormalForm = new ChomskyNormalForm(grammarInString.toString());
        return chomskyNormalForm.removeEpsilon();
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
        pdAtoCFG.printSteps();
    }
}
