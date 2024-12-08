import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String input = readInput();
        do {
            eval(input);
            input = readInput();
        } while(input != null);
    }

    private static void eval(String input){
        String cmd = parseCmd(input);
        switch(cmd){
            case "exit 0":
                System.exit(0);
                break;
            default:
                println("%s: command not found", cmd);
        }
    }

    private static String parseCmd(String input) {
        return input;
    }

    private static String readInput(){
        print("$ ");
        try{
            return scanner.nextLine();
        }catch(IllegalStateException e){
            return null;
        }
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
