package iohandler;
import java.util.Scanner;
import java.io.PrintStream;

public class Input {
    public Scanner scn;
     
    // define one scanner to pass to other methods
    public Input() {
        scn = new Scanner(System.in);
    }
  
    public String nextLine(String prompt) {
        System.out.print(prompt);
        return scn.nextLine();
    }

    public int nextInt(String txt, int max) {
        while (true) {
            System.out.print(txt);

            String rawVal = scn.nextLine().trim();
            // quit action
            if (rawVal.equals("q")) {
                return -1;
            }
            try {
                int val = Integer.parseInt(rawVal);
                if (val <= 0) {
                    System.out.println("Invalid input. Enter a positive number");
                    continue;
                } else if (val > max) {
                    System.out.printf("Invalid input. Maximum is %d%n", max);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("e: "+ e);
                System.out.println("Invalid input. Enter a number or q to quit the game.");
            }
        }
    }

    public void close() {
        scn.close();
    }
}