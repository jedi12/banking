package ru.demo.banking.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.demo.banking.configuration.AnsiEscapeCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class OperationsConsoleListener {
    private final Map<ConsoleOperationType, OperationCommand> commandMap = new HashMap<>();

    @Autowired
    public OperationsConsoleListener(List<OperationCommand> commands) {
        commands.forEach(command -> commandMap.put(command.getOperationType(), command));
    }

    public void startProcessing() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\nВыберите тип операции:");
                for (Enum<?> type: ConsoleOperationType.values()) {
                    System.out.println(type.name());
                }
                System.out.println();

                String choice = scanner.nextLine();

                ConsoleOperationType consoleOperationType = ConsoleOperationType.getByName(choice);
                if (consoleOperationType == null) {
                    System.out.printf("%n%sВведено неверное название операции. Попробуйте еще.%s%n%n", AnsiEscapeCode.RED, AnsiEscapeCode.RESET);
                    continue;
                }

                commandMap.get(consoleOperationType).execute(scanner);
            }
        }
    }
}
