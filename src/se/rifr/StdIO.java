package se.rifr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class StdIO
{
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Scanner scanner = new Scanner(System.in);

    public static String readLine() throws IOException{
        String txt = reader.readLine();
        return txt;
    }

    public static boolean readYesOrNo() {
        String ch = "";
        while (!ch.equals("y")&&!ch.equals("n")) {
            ch = String.valueOf(scanner.next().charAt(0)).toLowerCase();
        }
        return ch.equals("y");
    } //readYesOrNo

    public static void writeLine(String txt) {
        System.out.println(txt);
    }

    public static void write(String txt) {
        System.out.print(txt);
    }

    public static void clearScreen() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
}
