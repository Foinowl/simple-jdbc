package org.example.db.view;

import java.util.Scanner;

public class SimpleConsoleView implements ConsoleView {
  private static final ConsoleView instance = new SimpleConsoleView();
  public static ConsoleView getInstance() {
    return instance;
  }

  private Scanner in;

  private SimpleConsoleView() {
    in = new Scanner(System.in);
  }
  @Override
  public String nextLine() {
    System.out.print(">> ");
    return in.nextLine();
  }
}
