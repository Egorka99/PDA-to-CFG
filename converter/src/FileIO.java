import pda.LeftPart;
import pda.RightPart;
import pda.Transition;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    private String inputFilePath;
    private String outputFilePath;

    public FileIO(String inputFilePath, String outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    public List<Transition> read() throws IOException {
        List<Transition> transitionsList = new ArrayList<>();

        String[] rules = readInputString().split("\r\n");
        for (String rule : rules) {
            String leftPart = rule.split(" -> ")[0];
            String rightPart = rule.split(" -> ")[1];

            String[] leftPartArray = leftPart.split(",");
            String[] rightPartArray = rightPart.split(",");

            List<String> rightTerminals = new ArrayList<>();

            for (int i = 0; i < rightPartArray[1].toCharArray().length; i++) {
               if (rightPartArray[1].toCharArray()[i] == 'a') {
                   rightTerminals.add("a");
               }
               if (rightPartArray[1].toCharArray()[i] == 'z') {
                   rightTerminals.add("z0");
               }
                if (rightPartArray[1].toCharArray()[i] == 'e') {
                    rightTerminals.add("e");
                }
            }

            transitionsList.add(new Transition(new LeftPart(leftPartArray[0], leftPartArray[1], leftPartArray[2]),
                    new RightPart(rightPartArray[0],rightTerminals)));
        }

        return transitionsList;
    }

    public void write(String output) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFilePath);
        fileWriter.write(output);
        fileWriter.flush();
        fileWriter.close();
    }


    private String readInputString() throws IOException {
        FileReader fileReader = new FileReader(inputFilePath);
        StringBuilder input = new StringBuilder();

        int c;
        while ((c = fileReader.read()) != -1) {
            input.append((char) c);
        }
        fileReader.close();

        return input.toString();
    }

}
