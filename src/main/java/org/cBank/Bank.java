package org.cBank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * - банк обрабатыввет операции пополнение, снятие, перевод (внутри этих методов работают методы из класса Account
 * - банк собирает обьекта клиента - берёт информацию из базы SQL и передаёт на обработку в main работает с базой   
 * и производит запись резултата в базу по итогу операций
 * - содержит метод для обновления баланса на конец месяца (прибавка процента из конфигурациооного файла) 
 * - после каждой операции методы класса выводят в консоль информацию (информация о клиенте и чек) и записыват
 * банковский чек в txt файл в папку check (метод записи в классе Transaction)
 */
public class Bank {
    private final String homeBankName;
    private final ArrayList<Transaction> transactions;
    public Bank() {
        this.homeBankName = "Clever-Bank";
        this.transactions = new ArrayList<>();
    }

    void getUserList() {
        try{
            String url = "jdbc:postgresql://127.0.0.1:5432/pgCleverBankDB";
            String username = "postgres";
            String password = "PgRoot777";
            Class.forName("org.postgresql.Driver");

            try (Connection con = DriverManager.getConnection(url, username, password)){
                System.out.println("Connection to Store DB successful!");
                Statement statement = con.createStatement();

                try (ResultSet userBase = statement.executeQuery("SELECT * from accounts \n" +
                        "    INNER JOIN  banks ON accounts.banks_id = banks.id\n" +
                        "    INNER JOIN clients ON accounts.clients_id = clients.id\n" +
                        "ORDER BY accounts.id")) {
                    while (userBase.next()) {
                        // Retrieve column values
                        int id = userBase.getInt("id");
                        String firstname = userBase.getString("Фамилия");
                        String lastName = userBase.getString("Имя");
                        String patronymic = userBase.getString("Отчество");
                        String bankName = userBase.getString("Банк");
                        String accountNum = userBase.getString("Счёт");
                        double accountMoneyBalance = userBase.getDouble("Сумма");
                        String currency = userBase.getString("Валюта");
                        // out to console
                        System.out.printf("Клиент %d", id);
                        System.out.printf("\t| Фамилия: %s", lastName);
                        System.out.printf("\t| Имя: %s", firstname);
                        System.out.printf("\t| Отчество: %s%n", patronymic);
                        System.out.printf("\t| Банк: %s", bankName);
                        System.out.printf("\t| Счёт: %s", accountNum);
                        System.out.printf("\t| Сумма: %.2f", accountMoneyBalance);
                        System.out.printf("\t| Валюта: %s |%n", currency);
                    }
                }
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...Uppphhh((");
            System.out.println(ex.getMessage());
        }
    }
    User getUserFromBase(int accoutNum) {  // get user from DB by account number (по номеру счёта)
        User user = null;
        try{
            String url = "jdbc:postgresql://127.0.0.1:5432/pgCleverBankDB";
            String username = "postgres";
            String password = "PgRoot777";
            Class.forName("org.postgresql.Driver");

            try (Connection con = DriverManager.getConnection(url, username, password)){
                System.out.println("Connection to Store DB successful!");
                Statement statement = con.createStatement();

                try (ResultSet account = statement.executeQuery("SELECT * from accounts \n" +
                        "    INNER JOIN  banks ON accounts.banks_id = banks.id\n" +
                        "    INNER JOIN clients ON accounts.clients_id = clients.id\n" +
                        " WHERE Счёт = "+accoutNum)) {
                    while (account.next()) {
                        // Retrieve column values
                        int id = account.getInt("id");
                        String lastName = account.getString("Фамилия");
                        String firstname = account.getString("Имя");
                        String patronymic = account.getString("Отчество");
                        String bankName = account.getString("Банк");
                        int accountNum = account.getInt("Счёт");
                        double accountMoneyBalance = account.getDouble("Сумма");
                        String currency = account.getString("Валюта");
                        String accountOpenedDate = account.getString("Дата открытия");
                        // out to console
                        System.out.printf("User %d", id);
                        System.out.printf("\t| Фамилия: %s", lastName);
                        System.out.printf("\t| Имя: %s", firstname);
                        System.out.printf("\t| Отчество: %s%n", patronymic);
                        System.out.printf("\t| Банк: %s", bankName);
                        System.out.printf("\t| Счёт: %s", accountNum);
                        System.out.printf("\t| Сумма: %s", accountMoneyBalance);
                        System.out.printf("\t| Валюта: %s |%n", currency);
                        //сборка клиента
                        user = new User(firstname, new Account(accountNum, accountMoneyBalance, accountOpenedDate, currency, bankName));
                    }
                }
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...Uppphhh((");
            System.out.println(ex.getMessage());
        }
        return user;
    }

    public void doTransactionAdd(User user, double moneyAmount)  { //пополенеие счёта
        user.getAccount().addMoney(moneyAmount); // метод логики java оперирует счетом в программе
        // запись итоговой суммы после пополнения в таблицу базы
        try{
            String url = "jdbc:postgresql://127.0.0.1:5432/pgCleverBankDB";
            String username = "postgres";
            String password = "PgRoot777";
            Class.forName("org.postgresql.Driver");

            try (Connection con = DriverManager.getConnection(url, username, password)){
                System.out.println("Connection to Store DB successful!");
                Statement statement = con.createStatement();

                int u = statement.executeUpdate("UPDATE accounts " +
                "SET Сумма = " + user.getAccount().getAccountMoneyBalance() + " " +
                "WHERE Счёт = " + user.getAccount().getAccountNum());
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...Uppphhh((");
            System.out.println(ex.getMessage());
        }
        Transaction transaction = new Transaction("Пополнение", user.getAccount().getBankName(), user.getAccount().getBankName(), user.getAccount(), user.getAccount(), moneyAmount);
        // запись транзакции в лист
        transactions.add(transaction);
        // печать в файл
        transaction.printToFile(transaction);
    }

    public void doTransactionSub(User user, double moneyAmount) { //снятие
        user.getAccount().subtractMoney(moneyAmount);
        // запись итоговой суммы снятия пополнения в таблицу базы
        try{
            String url = "jdbc:postgresql://127.0.0.1:5432/pgCleverBankDB";
            String username = "postgres";
            String password = "PgRoot777";
            Class.forName("org.postgresql.Driver");

            try (Connection con = DriverManager.getConnection(url, username, password)){
                System.out.println("Connection to Store DB successful!");
                Statement statement = con.createStatement();

                int u = statement.executeUpdate("UPDATE accounts " +
                        "SET Сумма = " + user.getAccount().getAccountMoneyBalance() + " " +
                        "WHERE Счёт = " + user.getAccount().getAccountNum());
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...Uppphhh((");
            System.out.println(ex.getMessage());
        }
        // запись транзакции в лист
        Transaction transaction = new Transaction("Снятие", user.getAccount().getBankName(), user.getAccount().getBankName(), user.getAccount(), user.getAccount(), moneyAmount);
        transactions.add(transaction);
        // печать в файл
        transaction.printToFile(transaction);
    }

    public void doTransfer(User sender, User receiver, double moneyAmount) { //перевод

// запись итоговой суммы после пополнения в таблицу базы
        try{
            String url = "jdbc:postgresql://127.0.0.1:5432/pgCleverBankDB";
            String username = "postgres";
            String password = "PgRoot777";
            Class.forName("org.postgresql.Driver");

            try (Connection con = DriverManager.getConnection(url, username, password)){
                con.setAutoCommit(false);
                System.out.println("Connection to Store DB successful!");
                Statement statement = con.createStatement();

                statement.executeQuery("SELECT * FROM accounts WHERE Счёт IN ("
                        + sender.getAccount().getAccountNum() + "," + receiver.getAccount().getAccountNum() +
                        ")  FOR UPDATE");

                sender.getAccount().subtractMoney(moneyAmount);
                receiver.getAccount().addMoney(moneyAmount);

                int u = statement.executeUpdate("UPDATE accounts " +
                        "SET Сумма = " + sender.getAccount().getAccountMoneyBalance() + " " +
                        "WHERE Счёт = " + sender.getAccount().getAccountNum() + "; " +
                        "UPDATE accounts " +
                        "SET Сумма = " + receiver.getAccount().getAccountMoneyBalance() + " " +
                        "WHERE Счёт = " + receiver.getAccount().getAccountNum() + "; COMMIT ");
                con.commit();
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...Uppphhh((");
            System.out.println(ex.getMessage());
        }
        // запись транзакции в лист
        Transaction transaction = new Transaction("Перевод", receiver.getAccount().getBankName(), sender.getAccount().getBankName(), receiver.getAccount(), sender.getAccount(), moneyAmount);
        transactions.add(transaction);
        // печать в файл
        transaction.printToFile(transaction);
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
    public String getBankName() {
        return homeBankName;
    }

    public void addPercentSQL(double percent) { // метод начисления процента по балансу 
        try{
            String url = "jdbc:postgresql://127.0.0.1:5432/pgCleverBankDB";
            String username = "postgres";
            String password = "PgRoot777";
            Class.forName("org.postgresql.Driver");

            try (Connection con = DriverManager.getConnection(url, username, password)){
                System.out.println("Connection to Store DB successful!");
                Statement statement = con.createStatement();

                try (ResultSet account = statement.executeQuery("SELECT Сумма from accounts")) {
                    while (account.next()) {
                        double accountMoneyBalance = account.getDouble("Сумма");
                        // fix in DB suumOfmoney
                        statement.executeUpdate("UPDATE accounts " +
                                "SET Сумма = "+(percent+accountMoneyBalance)/100+" + "+accountMoneyBalance );//Сумма + (Сумма * " + percent + ") / 100 ");
                    }
                }
            }
        }
        catch(Exception ex){
            System.out.println("Connection failed...Uppphhh((");
            System.out.println(ex.getMessage());
        }
    }
}
