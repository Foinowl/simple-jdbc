package org.example.db.controller.console;

public class AppContext {

  private static final AppContext instance = new AppContext();

  public static AppContext getInstance() {
    return instance;
  }

  private AppContext() {
    exit = false;
  }

  private boolean exit;

  public boolean isExit() {
    return exit;
  }

  public void setExit(boolean exit) {
    this.exit = exit;
  }
}
