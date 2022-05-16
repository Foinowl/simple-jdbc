package org.example.db.command.factory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.example.db.command.Command;
import org.example.db.controller.AppContext;
import org.example.db.controller.Params;
import org.example.db.dao.PersistentStorage;
import org.example.db.dao.StorageService;
import org.example.db.model.Employee;
import org.example.db.model.Organization;
import org.example.db.model.Salary;

public class CommandFactoryImpl implements CommandFactory {

    private static final CommandFactory instance = new CommandFactoryImpl();

    private final Map<String, CommandBuilder> registar = new HashMap<>();

    public CommandFactoryImpl() {
        register(ExitCommand.NAME, new ExitCommandBuilder());
        register(AddCommand.NAME, new AddCommandBuilder());
        register(ListCommand.NAME, new ListCommandBuilder());
        register(FindCommand.NAME, new FindCommandBuilder());
        register(GetCommand.NAME, new GetCommandBuilder());
        register(DeleteCommand.NAME, new DeleteCommandBuilder());
        register(UpdateCommand.NAME, new UpdateCommandBuilder());
    }

    public static CommandFactory getInstance() {
        return instance;
    }

    private void register(String commandName, CommandBuilder commandBuilder) {
        registar.put(commandName, commandBuilder);
    }

    @Override
    public Command buildCommand(Params params) {

        CommandBuilder commandBuilder = registar.getOrDefault(params.getCommandName(), new UnknownCommandBuilder());

        return commandBuilder.createCommand(params);
    }


    public static class ExitCommand implements Command {

        public static String NAME = "exit";

        @Override
        public void execute() {
            AppContext.getInstance().setExit(true);
        }
    }

    public static class UnknownCommand implements Command {

        @Override
        public void execute() {

        }
    }

    public static class UnknownCommandBuilder implements CommandBuilder {

        @Override
        public Command createCommand(Params params) {
            return new UnknownCommand();
        }
    }

    public static class AddCommand implements Command {

        public static final String NAME = "add";

        private final String orgName;

        private final String empName;

        private final BigDecimal salary;

        private final StorageService storageService;


        public AddCommand(String orgName, String empName, BigDecimal salary,
                          StorageService storageService) {
            this.orgName = orgName;
            this.empName = empName;
            this.salary = salary;
            this.storageService = storageService;
        }

        @Override
        public void execute() {
            Organization organization = new Organization(orgName);
            Employee employee = new Employee();
            employee.setName(empName);
            Salary s = new Salary();
            s.setValue(salary);
            employee.setSalary(s);
            storageService.add(organization, employee);
        }
    }

    public static class AddCommandBuilder implements CommandBuilder {


        @Override
        public Command createCommand(Params params) {

            String oName = params.getArgs()[0];
            String eName = params.getArgs()[1];
            BigDecimal salary = new BigDecimal(params.getArgs()[2]);

            return new AddCommand(oName, eName, salary,
                PersistentStorage.getInstance());
        }
    }

    public static class ExitCommandBuilder implements CommandBuilder {
        @Override
        public Command createCommand(Params params) {
            return new ExitCommand();
        }
    }

    public static class ListCommand implements Command {

        public static String NAME = "list";

        private final StorageService storageService;

        public ListCommand(StorageService storageService) {
            this.storageService = storageService;
        }

        @Override
        public void execute() {
            storageService.listAll()
                .forEach(e ->
                    System.out.printf("%s %d%n", e.getName(), e.getSalary().getValue().longValueExact()));
        }
    }

    public static class ListCommandBuilder implements CommandBuilder {

        @Override
        public Command createCommand(Params params) {
            return new ListCommand(PersistentStorage.getInstance());
        }
    }

    public static class GetCommand implements Command {
        public static String NAME = "get";

        private final String name;

        private final StorageService storageService;

        public GetCommand(final String name, final StorageService storageService) {
            this.name = name;
            this.storageService = storageService;
        }

        @Override
        public void execute() {
            System.out.println("Найден работник - " + storageService.get(name));
        }
    }

    public static class GetCommandBuilder implements CommandBuilder {

        @Override
        public Command createCommand(Params params) {
            return new GetCommand(params.getArgs()[0], PersistentStorage.getInstance());
        }
    }

    public static class DeleteCommand implements Command {
        public static String NAME = "delete";

        private final String name;

        private final StorageService storageService;

        public DeleteCommand(final String name, final StorageService storageService) {
            this.name = name;
            this.storageService = storageService;
        }

        @Override
        public void execute() {
            System.out.println("Удален работник - " + storageService.delete(name));
        }
    }

    public static class DeleteCommandBuilder implements CommandBuilder {

        @Override
        public Command createCommand(Params params) {
            return new DeleteCommand(params.getArgs()[0], PersistentStorage.getInstance());
        }
    }

    public static class UpdateCommand implements Command {
        public static String NAME = "update";

        private final Employee employee;

        private final StorageService storageService;

        public UpdateCommand(final Employee employee, final StorageService storageService) {
            this.employee = employee;
            this.storageService = storageService;
        }


        @Override
        public void execute() {
            System.out.println("Обновлен работник - " + storageService.update(employee));
        }
    }

    public static class UpdateCommandBuilder implements CommandBuilder {

        @Override
        public Command createCommand(Params params) {
            Employee employee = new Employee();
            String[] args = params.getArgs();
            employee.setId(Long.valueOf(args[0]));
            employee.setName(args[1]);
            Salary salary = new Salary();
            salary.setValue(BigDecimal.valueOf(Long.parseLong(args[2])));
            employee.setSalary(salary);
            return new UpdateCommand(employee, PersistentStorage.getInstance());
        }
    }

    public static class FindCommand implements Command {
        public static String NAME = "find";

        private final StorageService storageService;

        private final String title;

        public FindCommand(final StorageService storageService, final String title) {
            this.storageService = storageService;
            this.title = title;
        }

        @Override
        public void execute() {
            System.out.println("найдена организация - " + storageService.findOrganization(title));
        }
    }

    public static class FindCommandBuilder implements CommandBuilder {

        @Override
        public Command createCommand(Params params) {
            return new FindCommand(PersistentStorage.getInstance(), params.getArgs()[0]);
        }
    }
}
