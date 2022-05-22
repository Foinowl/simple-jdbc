package org.example.db.command.impl.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.db.command.BasedCommand;
import org.example.db.command.Command;
import org.example.db.command.CommandBuilder;
import org.example.db.command.CommandFactory;
import org.example.db.command.impl.BasedCommandFactory;
import org.example.db.controller.model.Params;
import org.example.db.controller.model.ProxyParams;
import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.service.EmployeeService;
import org.example.db.service.JsonService;
import org.example.db.service.OrganizationService;
import org.example.db.service.ServiceFactory;
import org.example.db.service.UtilsService;

public class ServletCommandFactory extends BasedCommandFactory implements CommandFactory {

    private static final CommandFactory instance = new ServletCommandFactory();

    private static final EmployeeService<Employee> employeeService = ServiceFactory.getInstance().getEmployeeService();

    private static final JsonService jsonService = ServiceFactory.getInstance().getJsonService();

    private static final OrganizationService<Organization> organizationService =
        ServiceFactory.getInstance().getOrganizationService();


    public ServletCommandFactory() {
        register(GetEmployeeByNameCommand.NAME, new GetEmployeeByNameCommand());
        register(GetEmployeeByIdCommand.NAME, new GetEmployeeByIdCommand());
        register(GetOrganizationAllCommand.NAME, new GetOrganizationAllCommand());
        register(GetOrganizationCommand.NAME, new GetOrganizationCommand());
        register(GetEmployeeAllCommand.NAME, new GetEmployeeAllCommand());
        register(DeleteEmployeeByNameCommand.NAME, new DeleteEmployeeByNameCommand());
        register(AddOrganizationCommand.NAME, new AddOrganizationCommand());
        register(AddEmployeeCommand.NAME, new AddEmployeeCommand());
        register(UpdateEmployeeCommand.NAME, new UpdateEmployeeCommand());
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    @Override
    public Command buildCommand(Params params) {
        ServletCommandBuilder builder = new ServletCommandBuilder();
        return builder.createCommand(params);
    }

    public static class AddEmployeeCommand extends BasedCommand implements Command {
        public static String NAME = "post_employees";

        @Override
        public void execute() {
            try {
                Employee employee =
                    (Employee) jsonService.readObjectJson(((ProxyParams) params).getRequest().getInputStream(),
                        Employee.class);
                long savedOrg = employeeService.create(employee);
                ((ProxyParams) params).getResponse().getWriter().println("{\"status\":" + savedOrg + "}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class UpdateEmployeeCommand extends BasedCommand implements Command {
        public static String NAME = "put_employees";

        @Override
        public void execute() {
            try {
                Employee employee =
                    (Employee) jsonService.readObjectJson(((ProxyParams) params).getRequest().getInputStream(),
                        Employee.class);
                employeeService.update(employee);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class AddOrganizationCommand extends BasedCommand implements Command {
        public static String NAME = "post_organizations";

        @Override
        public void execute() {
            try {
                Organization organization =
                    (Organization) jsonService.readObjectJson(((ProxyParams) params).getRequest().getInputStream(),
                        Organization.class);
                organizationService.create(organization);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetOrganizationCommand extends BasedCommand implements Command {
        public static String NAME = "get_organizations_id";

        @Override
        public void execute() {
            long id =
                Long.parseLong(UtilsService.extractValueFromUrl(((ProxyParams) params).getRequest().getPathInfo()));
            Organization organization =
                organizationService.findById(id);
            organizationService.addEmployeeToModel(organization);
            try {
                Writer writer = ((ProxyParams) params).getResponse().getWriter();
                jsonService.writeToJsonObjects(organization, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetOrganizationAllCommand extends BasedCommand implements Command {
        public static String NAME = "get_organizations_all";

        @Override
        public void execute() {


            List<Organization> organizations =
                organizationService.findAll().stream().map(organizationService::addEmployeeToModel)
                    .collect(Collectors.toList());

            try {
                Writer writer = ((ProxyParams) params).getResponse().getWriter();
                jsonService.writeToJsonObjects(organizations, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetEmployeeByIdCommand extends BasedCommand implements Command {

        public static String NAME = "get_employees_id";


        @Override
        public void execute() {
            long id =
                Long.parseLong(UtilsService.extractValueFromUrl(((ProxyParams) params).getRequest().getPathInfo()));

            try {
                Writer writer = ((ProxyParams) params).getResponse().getWriter();
                jsonService.writeToJsonObjects(employeeService.findById(id), writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetEmployeeByNameCommand extends BasedCommand implements Command {

        public static String NAME = "get_employees_name";

        @Override
        public void execute() {
            try {
                String name =
                    UtilsService.extractValueFromUrl(((ProxyParams) params).getRequest().getPathInfo());
                Writer writer = ((ProxyParams) params).getResponse().getWriter();
                jsonService.writeToJsonObjects(employeeService.findByName(name), writer);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetEmployeeAllCommand extends BasedCommand implements Command {
        public static String NAME = "get_employees_all";

        @Override
        public void execute() {

            List<Employee> employees =
                employeeService.findAll();

            try {
                Writer writer = ((ProxyParams) params).getResponse().getWriter();
                jsonService.writeToJsonObjects(employees, writer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class DeleteEmployeeByNameCommand extends BasedCommand implements Command {

        public static String NAME = "delete_employees_name";

        @Override
        public void execute() {
            String name =
                UtilsService.extractValueFromUrl(((ProxyParams) params).getRequest().getPathInfo());

            boolean status = employeeService.delete(name);
            if (!status) {
                ((ProxyParams) params).getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                ((ProxyParams) params).getResponse().setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    public static class UnknownCommand extends BasedCommand implements Command {

        @Override
        public void execute() {
            try {
                Writer writer = ((ProxyParams) params).getResponse().getWriter();
                jsonService.writeToJsonObjects("\"status\":" + "unknown command", writer);
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
