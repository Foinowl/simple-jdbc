package org.example.db.controller.console;

import java.util.Arrays;
import org.example.db.controller.model.Params;

public class SimpleConsoleController implements Controller {

  private static final Controller instance = new SimpleConsoleController();

  public static Controller getInstance() {
    return instance;
  }

  private SimpleConsoleController() {}

  @Override
  public Params parseCommandLine(String line) {
    String commandName = null;
    String[] args = null;

    String[] row_data = line.split(" ");

    commandName = row_data[0];
    args = Arrays.copyOfRange(row_data, 1, row_data.length);

    return new Params(commandName, args);
  }
}
