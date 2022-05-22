package org.example.db.controller.tomcats;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.example.db.command.Command;
import org.example.db.command.impl.servlet.ServletCommandFactory;
import org.example.db.controller.model.Params;
import org.example.db.controller.model.ProxyParams;
import org.example.db.service.UtilsService;

@WebServlet("/api/*")
public class ServletController extends HttpServlet {

    private final String digitalValue = "id";
    private final String stringValue = "name";

    private final String ALL = "all";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        process(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        process(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) {
        String urlCommand = getCommandFromUrl(req);
        Params params = createParamsWithName(urlCommand, req, resp);
        Command command = ServletCommandFactory.getInstance().buildCommand(params);
        command.execute();
    }


    //    Рассчитан на более простое использование rest api.
//    можно добавить парсер для разбора сложных урлов
//    который будет отдавать шаблонный урл c готовым объектом params для конкретного CommandBuilder
//    Example: GET /api/employee/14 -> /api/employee/{id} = GetEmployeeByIdBuilder --> GetEmployeeByIdCommand
//    Example: GET /api/organization/rt/employee/5 -> /api/organization/{orgName}/employee/{empId}
    private String getCommandFromUrl(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getMethod());

        String path = req.getPathInfo();
        int start = path.indexOf(UtilsService.SLASH);
        int end = path.lastIndexOf(UtilsService.SLASH);
        if (end > start) {
            String presentUrl = path.substring(start, end);
            String lastValue = UtilsService.extractValueFromUrl(path);
            if (StringUtils.isNumeric(lastValue)) {
                lastValue = lastValue.replaceAll("\\d+", digitalValue);
            } else if (!lastValue.isEmpty() && !lastValue.equals(ALL)){
                lastValue = stringValue;
            }
            path = presentUrl + UtilsService.SLASH + lastValue;
        }
        sb.append(String.join("_", path.split(UtilsService.SLASH)));
        return sb.toString().toLowerCase();
    }

    private Params createParamsWithName(String urlCommand, HttpServletRequest req, HttpServletResponse resp) {
        return new ProxyParams(urlCommand, req, resp);
    }
}