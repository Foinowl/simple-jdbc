package org.example.db.command;

import org.example.db.controller.Params;

public class BasedCommand {
    protected Params params;

    public BasedCommand(Params params) {
        this.params = params;
    }
}
