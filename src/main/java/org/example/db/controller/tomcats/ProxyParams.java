package org.example.db.controller.tomcats;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.example.db.controller.Params;

public class ProxyParams extends Params {


    private HttpServletRequest request;

    private HttpServletResponse response;


    public ProxyParams(String commandName, HttpServletRequest request, HttpServletResponse response) {
        super(commandName);
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
