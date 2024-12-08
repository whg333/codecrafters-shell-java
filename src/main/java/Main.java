import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Path> PATH = new ArrayList<>();

    private static final Map<String, CmdHandler> cmdMap = new HashMap<>(){
        {
            for(CmdType cmdType: CmdType.values()){
                put(cmdType.name(), cmdType.handler);
            }
        }
    };

    public static void main(String[] args) {
        parseSysPath(args);
        String input = readInput();
        do {
            eval(input);
            input = readInput();
        } while(input != null);
    }

    private static void parseSysPath(String[] args){
        String path = System.getenv("PATH");
        List<Path> pathList = Arrays.stream(path.split(File.pathSeparator)).map(Path::of).collect(Collectors.toList());
        // PATH.add(Path.of("/usr/bin"));
        // PATH.add(Path.of("/usr/local/bin"));
        PATH.addAll(pathList);
        // println(String.valueOf(PATH));
    }

    private static void eval(String input){
        Cmd cmd = parseCmd(input);
        CmdHandler handler = cmdMap.getOrDefault(cmd.cmd, UnknownCmd.INSTANCE);
        handler.eval(cmd);
    }

    private static Cmd parseCmd(String input) {
        String[] strArr = input.split(" ");
        return new Cmd(strArr[0], Arrays.copyOfRange(strArr, 1, strArr.length));
    }

    private static class Cmd{
        final String cmd;
        final String[] args;
        public Cmd(String cmd, String[] args) {
            this.cmd = cmd;
            this.args = args;
        }
    }

    private enum CmdType{

        exit(ExitCmd.INSTANCE),
        echo(EchoCmd.INSTANCE),
        type(TypeCmd.INSTANCE),
        ;

        final CmdHandler handler;
        CmdType(CmdHandler handler){
            this.handler = handler;
        }
        public static CmdType typeOf(String cmd){
            for(CmdType cmdType: values()){
                if(cmd.equals(cmdType.name())){
                    return cmdType;
                }
            }
            throw new IllegalArgumentException("Unknown command: "+cmd);
        }
    }

    private interface CmdHandler{
        void eval(Cmd cmd);
    }
    private static class ExitCmd implements CmdHandler{
        static CmdHandler INSTANCE = new ExitCmd();
        private ExitCmd(){}
        @Override
        public void eval(Cmd cmd) {
            System.exit(0);
        }
    }
    private static class EchoCmd implements CmdHandler{
        static CmdHandler INSTANCE = new EchoCmd();
        private EchoCmd(){}
        @Override
        public void eval(Cmd cmd) {
            println(String.join(" ", cmd.args));
        }
    }
    private static class TypeCmd implements CmdHandler{
        static CmdHandler INSTANCE = new TypeCmd();
        private TypeCmd(){}
        @Override
        public void eval(Cmd cmd) {
            String firstArg = cmd.args[0];
            if(cmdMap.containsKey(firstArg)){
                println("%s is a shell builtin", firstArg);
            }else{
                boolean found = false;
                for(Path path: PATH){
                    if(found){
                        break;
                    }
                    try (Stream<Path> paths = Files.walk(path)) {
                        List<Path> filePaths = paths.filter(Files::isRegularFile).collect(Collectors.toList());
                        for(Path filePath: filePaths){
                            String fileName = filePath.getFileName().toString();
                            // System.out.println(fileName+", "+firstArg);
                            if(fileName.equals(firstArg)){
                                println("%s is %s", firstArg, filePath);
                                found = true;
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!found){
                    println("%s: not found", firstArg);
                }
            }
        }
    }
    private static class UnknownCmd implements CmdHandler{
        static CmdHandler INSTANCE = new UnknownCmd();
        private UnknownCmd(){}
        @Override
        public void eval(Cmd cmd) {
            println("%s: command not found", cmd.cmd);
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
