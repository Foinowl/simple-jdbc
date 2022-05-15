package org.example.db.controller;

import org.example.db.command.Command;
import org.example.db.command.factory.CommandFactory;
import org.example.db.command.factory.CommandFactoryImpl;
import org.example.db.view.ConsoleView;

public class FrontController {

  private static final FrontController instance = new FrontController();

  public static FrontController getInstance() {
    return instance;
  }

  private ConsoleView consoleView;
  private Controller controller;

  private AppContext appContext = AppContext.getInstance();

  private CommandFactory commandFactory = CommandFactoryImpl.getInstance();

  public void init(ConsoleView consoleView, Controller controller) {
    this.consoleView = consoleView;
    this.controller = controller;
  }

  public void process() {
    String newLine = null;
    while (!appContext.isExit()) {
      newLine = consoleView.nextLine();
      Params params = controller.parseCommandLine(newLine);
      Command command = commandFactory.buildCommand(params);
      command.execute();
    }
  }

}
