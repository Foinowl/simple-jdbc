package org.example.db.command;

/**
 * Общий интерфейс для реализации выполнения конкретных команд.
 *
 * В FrontController с помощью CommandFactory по введенной пользователем строке в консоле получает
 * конкретный объект, реализующий данный интерфейс, и вызывает у него метод execute.
 *
 * При этом на уровне FrontController благодаря полиморфизму нету никакой информации о таких реализациях Command как
 * ExitCommand, UnknownCommand, AddCommand или ListCommand.
 *
 * Благодаря такому подходу, при необходимости добавления новых команд достаточно написать еще один класс наследник от Command,
 * класс наследник от CommandBuilder и добавить его в CommandFactory. При этом в FrontController никаких изменений не нужно
 * вносить (что соответствует принципу открытости по SOLID)
 * */
public interface Command {

    void execute();
}
