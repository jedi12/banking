package ru.demo.banking.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;
import ru.demo.banking.model.User;
import ru.demo.banking.services.UserService;

import java.util.Scanner;

@Component
public class UserCreateCommand implements OperationCommand {
    private final UserService userService;

    @Autowired
    public UserCreateCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Укажите логин пользователя:");
        String userLogin = scanner.nextLine();

        User user = userService.createUser(userLogin);

        System.out.printf("%n%sСоздан новый пользователь: %s%s%n", AnsiEscapeCode.GREEN, user, AnsiEscapeCode.RESET);
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.USER_CREATE;
    }
}
