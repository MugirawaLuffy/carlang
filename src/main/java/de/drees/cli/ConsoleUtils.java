package de.drees.cli;

public class ConsoleUtils {
    //** SINGLETON PATTERN **//
    private static ConsoleUtils instance;
    private ConsoleUtils() {}
    public static ConsoleUtils getInstance() {
        if(instance == null)
            instance = new ConsoleUtils();

        return instance;
    }

    String carlangTitle = """
                  _..._                                                          \s
                .-'_..._''.                   .---.                               \s
              .' .'      '.\\                  |   |             _..._             \s
             / .'                             |   |           .'     '.   .--./)  \s
            . '                       .-,.--. |   |          .   .-.   . /.''\\\\   \s
            | |                 __    |  .-. ||   |    __    |  '   '  || |  | |  \s
            | |              .:--.'.  | |  | ||   | .:--.'.  |  |   |  | \\`-' /   \s
            . '             / |   \\ | | |  | ||   |/ |   \\ | |  |   |  | /("'`    \s
             \\ '.          .`" __ | | | |  '- |   |`" __ | | |  |   |  | \\ '---.  \s
              '. `._____.-'/ .'.''| | | |     |   | .'.''| | |  |   |  |  /'""'.\\ \s
                `-.______ / / /   | |_| |     '---'/ /   | |_|  |   |  | ||     ||\s
                         `  \\ \\._,\\ '/|_|          \\ \\._,\\ '/|  |   |  | \\'. __// \s
                             `--'  `"               `--'  `" '--'   '--'  `'---'  \n""";

    public void printScreenContents() {
        System.out.print(carlangTitle);
        System.out.println("Welcome to Carlang. Type \":help:\" for commands and usage.");
    }
}
