package org.example.db.command;

import org.example.db.controller.model.Params;

public class BasedCommand {
    protected Params params;


    public BasedCommand() {
    }

    public void setParams(Object params) {
        this.params = (Params) params;
    }
}