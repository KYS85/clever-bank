package org.cBank;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Scanner;

/**
 * Главный класс для запуска и упарвления приложением:
 * - запуск таймера для проверки является ли текущая дата последним днём месяца,
 * в этом случает начисляется процет по балансу (размер процента записан в файле config.yml)
 * - через объек Bank подключается к базе данных с информацией о клиентах и управленией операциями
 */
// connecting to the postgresDataBase
//export CLASSPATH=/Users/spesu/Java/Clever-Bank/src/main/resourcesp/ostgresql-42.5.4.jar:$CLASSPATH
public class Main {
    private static int configValue; // переменная для значеня из конфигурационного файла
    private static boolean running = true; // для остановки таймера при перезагрузке приложения
    public static void main(String[] args)  {
        Scanner in = new Scanner(System.in);
        Bank bank = new Bank();
        //начисление % на послений день месяца
        try (FileInputStream fis = new FileInputStream("/Users/spesu/Java/Clever-Bank/src/main/java/org/cBank/config.yml")) {
            configValue = Integer.parseInt((char) fis.readAllBytes()[0] + "");
            System.out.println("config = "+ configValue + "% для начисления на конец месяца");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("Проверка такущей даты для начисления процентов \n");
        var lastDateOfCurrentMonth = TemporalAdjusters.lastDayOfMonth()
                .adjustInto(LocalDate.now()); // выравниваем текущую дату на последний день месяца
        Thread timer = new Thread(() -> { // таймер в отдельном потоке
            while(running){
                try {
                    Thread.sleep(30000); // проверка через 30 сек
                    System.out.println("Проверяю не конец ли месяца...");
                    System.out.println(LocalDate.now());
                    if (LocalDate.now().equals(lastDateOfCurrentMonth)) {
                        bank.addPercentSQL(configValue);
                        System.out.println("Начисляем");
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        });
        timer.start();
        // уравление приложением
        System.out.println("Список клиентов:");
        bank.getUserList();
        int command = 0;
        while (command != 4) {
            System.out.println("Выберите транзакию: \n 1 - Пополнение счёта, 2 - Снятие средств со счета, \n" +
                    " 3 - перевода средств другому клиенту, 4 - выход: ");
            command = in.nextInt();
            switch (command) {
                case 1 -> {
                    System.out.println("Введите номер счёта для пополнения: ");
                    int num = in.nextInt();
                    System.out.println("Введите сумму для пополнения: ");
                    int sum = in.nextInt();
                    bank.doTransactionAdd(bank.getUserFromBase(num), sum);
                    System.out.println(bank.getTransactions().get(bank.getTransactions().size() - 1).toString());
                }
                case 2 -> {
                    System.out.println("Введите номер счёта для снятия средств: ");
                    int num = in.nextInt();
                    System.out.println("Введите сумму для снятия средств со счета: ");
                    int sum = in.nextInt();
                    bank.doTransactionSub(bank.getUserFromBase(num), sum);
                    System.out.println(bank.getTransactions().get(bank.getTransactions().size() - 1).toString());
                    System.out.println(bank.getTransactions());
                }
                case 3 -> {
                    System.out.println("Введите номер счёта отправителя: ");
                    int sender = in.nextInt();
                    System.out.println("Введите номер счёта получателя: ");
                    int reviver = in.nextInt();
                    System.out.println("Введите сумму перевода: ");
                    int sum = in.nextInt();
                    bank.doTransfer(bank.getUserFromBase(sender), bank.getUserFromBase(reviver), sum);
                    System.out.println(bank.getTransactions().get(bank.getTransactions().size() - 1).toString());
                    System.out.println(bank.getTransactions());
                }
            }
        }
        System.out.println("Окончено!");
    }
}

