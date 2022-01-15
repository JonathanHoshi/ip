import java.util.Locale;

public class Duke {

    public static String exitString = "bye";

    public static void printWelcomeMsg() {
        Console.printLogo();
        Console.println("Hello, I'm JJBA" +
                "\nWhat can I do for you?");
    }

    public static void printExitMsg() {
        Console.println("Goodbye! Good luck!");
    }


    public static void main(String[] args) {

        printWelcomeMsg();

        String input = "";
        boolean exitProg = false;

        do {
            input = Console.read();

            switch (input.toLowerCase()) {
                case "bye": {
                    printExitMsg();
                    exitProg = true;
                    break;
                }
                case "dio": {
                    Console.println("WRYYYYYYYYYYYY!");
                    break;
                }
                default: {
                    Console.println(input);
                    break;
                }
            }

        } while (!exitProg);




    }
}
