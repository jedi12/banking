package ru.demo.banking.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;
import ru.demo.banking.model.User;
import ru.demo.banking.services.UserService;

import java.util.List;
import java.util.Scanner;

@Component
public class ShowAllUsersCommand implements OperationCommand {
    private final UserService userService;

    @Autowired
    public ShowAllUsersCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Scanner scanner) {
        List<User> users = userService.getAllUsers();

        System.out.printf("%n%sСписок пользователей: %s%n", AnsiEscapeCode.GREEN, AnsiEscapeCode.RESET);
        for (User user: users) {
            System.out.printf("%s%s%s%n", AnsiEscapeCode.GREEN, user, AnsiEscapeCode.RESET);
        }
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.SHOW_ALL_USERS;
    }
}
