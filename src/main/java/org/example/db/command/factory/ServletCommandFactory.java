package org.example.db.command.factory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.example.db.command.BasedCommand;
import org.example.db.command.Command;
import org.example.db.controller.Params;
import org.example.db.controller.tomcats.ProxyParams;


// TODO: В качестве примера реализовать 2 апи с гет запросом для работника

// Отдельные команды с логикой сервелетов вынести в один пакет
public class ServletCommandFactory extends BasedCommandFactory {

    private static final CommandFactory instance = new ServletCommandFactory();


    public ServletCommandFactory() {
        register(GetEmployeeCommandBuilder.NAME, new GetEmployeeCommandBuilder());
    }

    public static CommandFactory getInstance() {
        return instance;
    }


//    public class AddEmployeeCommand implements Command {
//
//        @Override
//        public void execute() {
//
//        }
//    }
//
//    public class AddEmployeeCommandBuilder implements CommandBuilder {
//
//        @Override
//        public Command createCommand(Params params) {
////          extract body json
//            return null;
//        }
//    }

    public static class GetEmployeeByIdCommand extends BasedCommand implements Command {

        public GetEmployeeByIdCommand(Params params) {
            super(params);
        }

        @Override
        public void execute() {
            try {
                ((ProxyParams) params).getResponse().getWriter().println("GetEmployeeByIdCommand");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetEmployeeByNameCommand extends BasedCommand implements Command {

        public GetEmployeeByNameCommand(ProxyParams params) {
            super(params);
        }

        @Override
        public void execute() {
            try {
                ((ProxyParams) params).getResponse().getWriter().println("GetEmployeeByNameCommand");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetEmployeeCommandBuilder implements CommandBuilder {

        public static String NAME = "get_employee";

//        TODO: Для различных валидаций создать отдельный класс
        public static boolean isNumeric(String str) {
            return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
        }

        @Override
        public Command createCommand(Params params) {
            ProxyParams proxyParams = (ProxyParams) params;

            HttpServletRequest request = proxyParams.getRequest();

            String url = request.getPathInfo();
            String type = url.substring(url.lastIndexOf("/") + 1);

            Command command = null;
            if (type.isEmpty()) {
//              Если тип пустой значит, пришел http-запрос с поиском пользователя, параметры в body
            } else if (isNumeric(type)) {
                command = new GetEmployeeByIdCommand(proxyParams);
            } else {
                command = new GetEmployeeByNameCommand(proxyParams);
            }
            return command;
        }
    }
}
