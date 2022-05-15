package org.example.db.controller;

public class Params {
  private String commandName;
  private String[] args;

  public Params(String commandName, String[] args) {
    this.commandName = commandName;
    this.args = args;
  }

  public String getCommandName() {
    return commandName;
  }

  public String[] getArgs() {
    return args;
  }
}
