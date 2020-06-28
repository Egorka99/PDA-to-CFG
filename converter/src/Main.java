import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileIO fileIO = new FileIO("input.txt", "output.txt");
        PDAtoCFG pdAtoCFG = new PDAtoCFG(fileIO.read());
        pdAtoCFG.printSteps();
    }
}
