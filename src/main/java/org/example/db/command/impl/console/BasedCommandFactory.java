package org.example.db.command.impl.console;

import java.util.HashMap;
import java.util.Map;
import org.example.db.command.Command;
import org.example.db.command.CommandBuilder;

public class BasedCommandFactory  {

    protected final Map<String, CommandBuilder> registar = new HashMap<>();

    protected final Map<String, Command> cachedCommand = new HashMap<>();


    protected void register(String commandName, CommandBuilder commandBuilder) {
        registar.put(commandName, commandBuilder);
    }

    protected void register(String commandName, Command command) {
        cachedCommand.put(commandName, command);
    }
}
