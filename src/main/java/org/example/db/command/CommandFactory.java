package org.example.db.command.factory;


import org.example.db.command.Command;
import org.example.db.controller.Params;

/**
 * Интерфейс фабрики команд.
 * Отвечает за получение конкретной команды на основе данных из командной строки (Params)
 *
 * Для получения конкретной команды используется CommandBuilder
 */
public interface CommandFactory {

Command buildCommand(Params params);
}
