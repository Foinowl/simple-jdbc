package org.example.db.command.impl.servlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.db.command.BasedCommand;
import org.example.db.command.Command;
import org.example.db.command.CommandBuilder;
import org.example.db.command.CommandFactory;
import org.example.db.command.impl.console.BasedCommandFactory;
import org.example.db.controller.Params;
import org.example.db.controller.tomcats.ProxyParams;
import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.service.EmployeeService;
import org.example.db.service.OrganizationService;
import org.example.db.service.ServiceFactory;
import org.example.db.service.UtilsService;

public class ServletCommandFactory extends BasedCommandFactory implements CommandFactory {

    private static final CommandFactory instance = new ServletCommandFactory();

    private static final EmployeeService<Employee> employeeService = ServiceFactory.getInstance().getEmployeeService();

    private static final ObjectMapper mapper = new ObjectMapper();

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
        public static String NAME = "post_employee";

        @Override
        public void execute() {
            String json = "";
            try (InputStream ip = ((ProxyParams) params).getRequest().getInputStream()) {
                byte[] bytes = new byte[ip.available()];
                ip.read(bytes);
                json = new String(bytes);

                var employee = mapper.readValue(json, Employee.class);
                var savedOrg = employeeService.create(employee);
//                ((ProxyParams) params).getResponse().getWriter().println(employee);
                ((ProxyParams) params).getResponse().getWriter().println("{\"id\":" + savedOrg + "}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class UpdateEmployeeCommand extends BasedCommand implements Command {
        public static String NAME = "put_employee";

        @Override
        public void execute() {
            String json = "";
            try (InputStream ip = ((ProxyParams) params).getRequest().getInputStream()) {
                byte[] bytes = new byte[ip.available()];
                ip.read(bytes);
                json = new String(bytes);

                var employee = mapper.readValue(json, Employee.class);
                employeeService.update(employee);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class AddOrganizationCommand extends BasedCommand implements Command {
        public static String NAME = "post_organization";

        @Override
        public void execute() {
            String json = "";
            try (InputStream ip = ((ProxyParams) params).getRequest().getInputStream()) {
                byte[] bytes = new byte[ip.available()];
                ip.read(bytes);
                json = new String(bytes);

                var req = mapper.readValue(json, Organization.class);
                var savedOrg = organizationService.create(req);
                ((ProxyParams) params).getResponse().getWriter().println("{\"id\":" + savedOrg + "}");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetOrganizationCommand extends BasedCommand implements Command {
        public static String NAME = "get_organization_id";

        @Override
        public void execute() {

            long id =
                Long.parseLong(UtilsService.extractValueFromUrl(((ProxyParams) params).getRequest().getPathInfo()));
            Organization organization =
                organizationService.findById(id);
            organizationService.addEmployeeToModel(organization);
            String json = null;
            try {
                json = mapper.writeValueAsString(organization);
                ((ProxyParams) params).getResponse().getWriter().print(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static class GetOrganizationAllCommand extends BasedCommand implements Command {
        public static String NAME = "get_organization_all";

        @Override
        public void execute() {


            List<Organization> organizations =
                organizationService.findAll().stream().map(organizationService::addEmployeeToModel)
                    .collect(Collectors.toList());
            ;
            String json = null;
            try {
                json = mapper.writeValueAsString(organizations);
                ((ProxyParams) params).getResponse().getWriter().print(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetEmployeeByIdCommand extends BasedCommand implements Command {

        public static String NAME = "get_employee_id";


        @Override
        public void execute() {
            try {

                long id =
                    Long.parseLong(UtilsService.extractValueFromUrl(((ProxyParams) params).getRequest().getPathInfo()));
                ((ProxyParams) params).getResponse().getWriter().println("GetEmployeeByIdCommand");
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
                ((ProxyParams) params).getResponse().getWriter().println(employeeService.findByName(name));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GetEmployeeAllCommand extends BasedCommand implements Command {
        public static String NAME = "get_employee_all";

        @Override
        public void execute() {

            List<Employee> employees =
                employeeService.findAll();
            String json = null;
            try {
                json = mapper.writeValueAsString(employees);
                ((ProxyParams) params).getResponse().getWriter().print(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static class DeleteEmployeeByNameCommand extends BasedCommand implements Command {

        public static String NAME = "delete_employee_name";

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
