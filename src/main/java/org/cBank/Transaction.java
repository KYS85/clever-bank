package org.cBank;
/**
 * Класс содержит все переменные транзакций и информацию для чека после операции
 * метод записывает чек в файл
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

public class Transaction {

    private final String transactionType;
    private final int transactionNum;
    private final LocalDate transactionDate;
    private final LocalTime transactionTime;
    private final String bankReceiver;
    private final String bankSender;
    private final Account accountReceiver;
    private final Account accountSender;
    private final double sumOfMoney;
    private static int cnt = 1;

    public Transaction(String transactionType, String bankReceiver, String bankSender, Account accountReceiver, Account accountSender, double sumOfMoney) {
        this.transactionType = transactionType;
        this.transactionNum = cnt++;
        this.transactionDate = LocalDate.now();
        this.transactionTime = LocalTime.now();
        this.bankReceiver = bankReceiver;
        this.bankSender = bankSender;
        this.accountReceiver = accountReceiver;
        this.accountSender = accountSender;
        this.sumOfMoney = sumOfMoney;
    }

//    @Override
//    public String toString() {
//        return "Transaction{" +
//                "Тип транзакции ='" + transactionType + '\'' +
//                ", транзация № = " + transactionNum +
//                ", дата = " + transactionDate +
//                ", время = " + transactionTime.toString().replaceAll("\\.\\d*", "") +
//                ", bankReceiver='" + bankReceiver + '\'' +
//                ", bankSender='" + bankSender + '\'' +
//                ", accountReceiver='" + accountReceiver + '\'' +
//                ", accountSender='" + accountSender + '\'' +
//                ", сумма = " + sumOfMoney +
//                '}'+"\n";
//    }

    public String toString() {
        return  String.format(
                "\n------------------------------------------------\n" +
                "|             Банковский Чек                   |\n" +
                "| Чек:                             %s           |\n" +
                "| Тип транзакции                   %s  |\n" +
                "| %s                       %s    |\n"+
                "| Банк отправителя:                %s |\n" +
                "| Банк получателя:                 %s |\n" +
                "| Счёт отправителя:                %s  |\n" +
                "| Счёт получателя:                 %s  |\n" +
                "| Cумма:                           %s  BYN"+" |\n" +
                "-----------------------------------------------" +"\n", transactionNum, transactionType,
                transactionDate, transactionTime.toString().replaceAll("\\.\\d*", ""),
                bankSender, bankReceiver,  accountSender.getAccountNum(), accountReceiver.getAccountNum(),
                sumOfMoney);
    }
    void printToFile(Transaction transaction)  { // запсь чека в файл
        System.out.println("Запись в файл");
        String fileName = "check" + transactionNum + ".txt";
        try {
            File file = new File("/Users/spesu/Java/Clever-Bank/src/main/java/org/cBank/check/"+fileName);;
            if (file.exists() ) { // проверка, есть ли такой файл
                while (Arrays.toString(Files.list(Path.of("/Users/spesu/Java/Clever-Bank/src/main/java/org/cBank/check/")).toArray()).contains(fileName)) {
                    fileName = transactionNum + "_" + fileName; // если в папке есть файлы с таким именем - меняем имя
                }
            }
                file = new File("/Users/spesu/Java/Clever-Bank/src/main/java/org/cBank/check/"+fileName);
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.append(transaction.toString());
                    fileWriter.flush();
                    System.out.println("запись тут!");
                }
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong!");
        }

    }
}
