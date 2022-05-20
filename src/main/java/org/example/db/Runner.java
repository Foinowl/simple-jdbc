package org.example.db;

import org.example.db.controller.console.FrontController;
import org.example.db.controller.console.SimpleConsoleController;
import org.example.db.view.SimpleConsoleView;

// Не  стал добавлять доп.настроек для валидации данных на разных уровнях
public class Runner {
    public static void main(String[] args) {
        FrontController fc = FrontController.getInstance();
        fc.init(SimpleConsoleView.getInstance(), SimpleConsoleController.getInstance());
        fc.process();
    }
}
