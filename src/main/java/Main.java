import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Uncomment this block to pass the first stage
        print("$ ");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        println("%s: node found", input);
    }

    private static void print(String msg){
        System.out.print(msg);
    }

    private static void println(String format, Object... args){
        println(String.format(format, args));
    }

    private static void println(String msg){
        System.out.println(msg);
    }

}
