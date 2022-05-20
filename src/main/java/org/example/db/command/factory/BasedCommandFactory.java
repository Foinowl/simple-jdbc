package org.example.db.command.factory;

import java.util.HashMap;
import java.util.Map;
import org.example.db.command.Command;
import org.example.db.controller.Params;

public class BasedCommandFactory implements CommandFactory {

    protected final Map<String, CommandBuilder> registar = new HashMap<>();


    protected void register(String commandName, CommandBuilder commandBuilder) {
        registar.put(commandName, commandBuilder);
    }

    @Override
    public Command buildCommand(Params params) {
        CommandBuilder commandBuilder = registar.getOrDefault(params.getCommandName(), new CommandFactoryImpl.UnknownCommandBuilder());

        return commandBuilder.createCommand(params);
    }
}
