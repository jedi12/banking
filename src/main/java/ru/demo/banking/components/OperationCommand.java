package ru.demo.banking.components;

import java.util.Scanner;

public interface OperationCommand {

    void execute(Scanner scanner);

    ConsoleOperationType getOperationType();
}
