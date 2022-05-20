package org.example.db.controller.tomcats;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.db.command.Command;
import org.example.db.command.factory.ServletCommandFactory;
import org.example.db.controller.Params;


// TODO: Устранить повторение кода, объедить повторяемые действия в одну логику.

@WebServlet("/api/*")
public class ServletController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().println(req.getContextPath());


        String url = req.getPathInfo();
        url = url.substring(url.lastIndexOf("/") + 1);

        resp.getWriter().println(url);
        resp.getWriter().println(req.getServletPath());
        resp.getWriter().println(req.getMethod());

        resp.getWriter().println(req.getRequestURL());

        resp.getWriter().println(getCommandFromUrl(req));
        String urlCommand = getCommandFromUrl(req);
        Params params = createParamsWithName(urlCommand, req, resp);
        Command command = ServletCommandFactory.getInstance().buildCommand(params);
        command.execute();
    }

    private Params createParamsWithName(String urlCommand, HttpServletRequest req, HttpServletResponse resp) {
        return new ProxyParams(urlCommand, req, resp);
    }


//    Рассчитан на более простое использование rest api
//    можно добавить парсер для разбора сложных урлов
//    который будет отдавать шаблонный урл c готовым объектом params для конкретного CommandBuilder
//    Example: GET /api/employee/14 -> /api/employee/{id} = GetEmployeeByIdBuilder --> GetEmployeeByIdCommand
//    Example: GET /api/organization/rt/employee/5 -> /api/organization/{orgName}/employee/{empId}
    private String getCommandFromUrl(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getMethod());
        sb.append(String.join("_", req.getPathInfo().replaceAll("\\d+", "id").split("/")));
        return sb.toString().toLowerCase();
    }
}
