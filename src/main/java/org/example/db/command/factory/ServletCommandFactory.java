package org.example.db.command.factory;

import java.io.IOException;
import org.example.db.command.BasedCommand;
import org.example.db.command.Command;
import org.example.db.controller.Params;
import org.example.db.controller.tomcats.ProxyParams;
import org.example.db.model.Employee;
import org.example.db.service.EmployeeService;
import org.example.db.service.ServiceFactory;
import org.example.db.service.UtilsService;
import org.example.db.service.impl.EmployeeServiceImpl;


// Отдельные команды с логикой сервелетов вынести в один пакет
public class ServletCommandFactory extends BasedCommandFactory implements CommandFactory {

    private static final CommandFactory instance = new ServletCommandFactory();

    private static final EmployeeService<Employee> employeeService = ServiceFactory.getInstance().getEmployeeService();


    public ServletCommandFactory() {
        register(GetEmployeeByNameCommand.NAME, new GetEmployeeByNameCommand());
        register(GetEmployeeByIdCommand.NAME, new GetEmployeeByIdCommand());

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

    @Override
    public Command buildCommand(Params params) {
        ServletCommandBuilder builder = new ServletCommandBuilder();
        return builder.createCommand(params);
    }

    public static class GetEmployeeByIdCommand extends BasedCommand implements Command {

        public static String NAME = "get_employee_id";


        @Override
        public void execute() {
            try {

                long id =
                    Long.parseLong(UtilsService.extractValueFromUrl(((ProxyParams) params).getRequest().getPathInfo()));
                ((ProxyParams) params).getResponse().getWriter().println("GetEmployeeByIdCommand");
                ((ProxyParams) params).getResponse().getWriter().println("ID ->" + id);
                ((ProxyParams) params).getResponse().getWriter().println(employeeService.findById(id));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetEmployeeByNameCommand extends BasedCommand implements Command {

        public static String NAME = "get_employee_name";

        @Override
        public void execute() {
            try {
                String name =
                    UtilsService.extractValueFromUrl(((ProxyParams) params).getRequest().getPathInfo());
                ((ProxyParams) params).getResponse().getWriter().println("GetEmployeeByNameCommand");
                ((ProxyParams) params).getResponse().getWriter().println(employeeService.findByName(name));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class UnknownCommand extends BasedCommand implements Command {

        @Override
        public void execute() {
            try {
                ((ProxyParams) params).getResponse().getWriter().println("UnknownCommand");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public class ServletCommandBuilder implements CommandBuilder {

        @Override
        public Command createCommand(Params params) {
            ProxyParams proxyParams = (ProxyParams) params;

            BasedCommand command =
                (BasedCommand) cachedCommand.getOrDefault(params.getCommandName(),
                    new UnknownCommand());
            command.setParams(proxyParams);
            return (Command) command;
        }
    }
}
