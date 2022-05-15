package org.example.db.command.factory;

import java.util.HashMap;
import java.util.Map;
import org.example.db.command.Command;
import org.example.db.controller.Params;

public class DefaultCommandFactory implements CommandFactory{

    private static final CommandFactory instance = new DefaultCommandFactory();


    public static CommandFactory getInstance() {
        return instance;
    }

    private final Map<String, CommandBuilder> registar = new HashMap<>();

    public DefaultCommandFactory() {
//        register(ExitCommand.NAME, new ExitCommandBuilder());
//        register(AddCommand.NAME, new AddCommandBuilder());
//        register(ListCommand.NAME, new ListCommandBuilder());
    }

    @Override
    public Command buildCommand(final Params params) {
        return null;
    }
}
