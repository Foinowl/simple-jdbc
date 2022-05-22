package org.example.db.controller.console;

import org.example.db.controller.model.Params;

/**
 * Интерфейс для преобразования строки в объект параметры.
 * В данном случае имеет только одну реализацию SimpleConsoleController которая разбирает строку из консоли.
 *
 * В перспективе могут быть и другие реализации, например отвечающие за преобразования строки, полученной через
 * http запрос или из брокера сообщений
 */
public interface Controller {
  Params parseCommandLine(String line);
}
