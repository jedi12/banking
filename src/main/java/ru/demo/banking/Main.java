package ru.demo.banking;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.demo.banking.configuration.AppConfig;
import ru.demo.banking.components.OperationsConsoleListener;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        OperationsConsoleListener operationsConsoleListener = context.getBean(OperationsConsoleListener.class);
        operationsConsoleListener.startProcessing();
    }
}