package org.example.db.command;


import org.example.db.controller.model.Params;

/**
 * Интерфейс для создания экземпляра объекта команда (класс, реализующий интерфейс Command).
 *
 * Конкретная реализация CommandBuilder связана с конкретной командой и отвечает за то, чтобы преобразовать
 * параметры введенные в командной строке в параметры (поля) класса команды
 *
 * CommandBuilder регистрируется в CommandFactory
 */
public interface CommandBuilder {

    Command createCommand(Params params);
}
