import java.util.Arrays;
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
        Cmd cmd = parseCmd(input);
        String command = cmd.cmd;
        switch(command){
            case "exit":
                System.exit(0);
                break;
            case "echo":
                println(String.join(" ", cmd.args));
                break;
            default:
                println("%s: command not found", command);
        }
    }

    private static Cmd parseCmd(String input) {
        String[] strArr = input.split(" ");
        return new Cmd(strArr[0], Arrays.copyOfRange(strArr, 1, strArr.length));
    }

    private static class Cmd{
        final String cmd;
        final String args[];
        public Cmd(String cmd, String[] args) {
            this.cmd = cmd;
            this.args = args;
        }
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
