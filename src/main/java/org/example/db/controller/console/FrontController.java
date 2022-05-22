package org.example.db.controller.console;

import org.example.db.command.Command;
import org.example.db.command.CommandFactory;
import org.example.db.command.impl.console.CommandFactoryImpl;
import org.example.db.controller.model.Params;
import org.example.db.view.ConsoleView;

public class FrontController {

    private static final FrontController instance = new FrontController();

    private ConsoleView consoleView;

    private Controller controller;

    private AppContext appContext = AppContext.getInstance();

    private CommandFactory commandFactory = CommandFactoryImpl.getInstance();

    public static FrontController getInstance() {
        return instance;
    }

    public void init(ConsoleView consoleView, Controller controller) {
        this.consoleView = consoleView;
        this.controller = controller;
    }

    public void process() {
        String newLine;
        while (!appContext.isExit()) {
            newLine = consoleView.nextLine();
            Params params = controller.parseCommandLine(newLine);
            Command command = commandFactory.buildCommand(params);
            command.execute();
        }
    }
}
