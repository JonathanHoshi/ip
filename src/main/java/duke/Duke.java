package duke;

import java.io.IOException;

import duke.bot.BotMessage;
import duke.bot.JjbaBotMessage;
import duke.command.BotCommand;
import duke.command.Command;
import duke.command.CommandFeedback;
import duke.exception.DukeException;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

/**
 * Represents a task bot. A <code>Duke</code> object can be created to
 * accept user input, create tasks and respond to the user.
 */
public class Duke {
    public static final String FILE_PATH = "/data/taskInfo.txt";

    private TaskList taskList;
    private Storage storage;
    private final Ui ui;

    /**
     * Creates a default instance of a Duke object.
     */
    public Duke() {
        this(new JjbaBotMessage());
    }

    /**
     * Creates an instance of a Duke object.
     *
     * @param bot type of message bot to output messages.
     */
    public Duke(BotMessage bot) {
        ui = new Ui(bot);
        taskList = new TaskList();
    }

    /**
     *
     */
    public void initializeStorageSystem() throws DukeException {
        try {
            storage = new Storage(FILE_PATH);
            taskList = storage.loadTaskList();

        } catch (IOException e) {
            storage = null;
            throw new DukeException(ui.formatError("Storage system failure"));
        } catch (DukeException e) {
            if (!e.isHidden) {
                throw new DukeException(ui.formatError(e.getMessage()));
            }
        }
    }

    public String getWelcomeMessage() {
        return ui.getWelcomeMessage();
    }

    /**
     * Returns the output message and handle the output according to the different
     * command type in the command feedback.
     *
     * @param com the type of command being executed.
     * @param comFeed the feedback of the command after being executed.
     * @return the message output generated.
     */
    public String handleCommandFeedback(Command com, CommandFeedback comFeed) {
        switch (comFeed.cType) {
        case BOT:
            ui.setBot(((BotCommand) com).getBotType());
            break;
        case EXIT:
            break;
        default:
            ui.formatError("Unexpected error in handleCommandFeedback!");
        }

        return ui.getCommandFeedbackMessage(comFeed);
    }

    /**
     * You should have your own function to generate a response to user input.
     * Replace this stub with your completed method.
     */
    public String getResponse(String input) {

        try {
            if (input.isBlank()) {
                throw new DukeException("Empty Input", true);
            }

            Command c = Parser.parseCommand(input.trim());
            CommandFeedback cf = c.execute(taskList);

            if (storage != null) {
                storage.saveTaskList(taskList);
            }

            return handleCommandFeedback(c, cf);

        } catch (DukeException e) {
            if (!e.isHidden) {
                if (e.isInvalidCommand) {
                    return ui.getInvalidCommandMessage(e.getMessage());

                } else {
                    return ui.formatError(e.getMessage());
                }
            } else {
                return null;
            }
        }
    }

    public String getBotImagePath() {
        return ui.getBotImagePath();
    }
}
