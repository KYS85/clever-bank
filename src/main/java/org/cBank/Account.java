package org.cBank;

/**
 * класс конструктор счёта содержит методы для работы с балансом
 * лучше все мат расчёты вести в базе а сущности создавать только для вывода ин
 */
public class Account {
    private final int accountNum;
    private double accountMoneyBalance;
    private final String accountOpenedDate;
    private final String currency;
    private final String bankName;

    public Account(int accountNum, double accountMoneyBalance, String accountOpenedDate, String currency, String bankName) {
        this.accountNum = accountNum;
        this.accountMoneyBalance = accountMoneyBalance;
        this.accountOpenedDate = accountOpenedDate;
        this.currency = currency;
        this.bankName = bankName;
    }

    // медоты коррекции баланса у сушьностей
    public void addMoney(double sum) {
        accountMoneyBalance += sum;
    }

    public void subtractMoney(double sum) {
        accountMoneyBalance -= sum;
    }
    //метод начисления процентов у сущности - наверное не будет использоваться, лучше считать сразу в базу
    public void addPercent(double percent) {
        accountMoneyBalance += (accountMoneyBalance * percent) / 100;
    }

//    public void transferMoney(int sum) {
//        accountMoneyBalance += sum;
//    }

    public int getAccountNum() {
        return accountNum;
    }

    public double getAccountMoneyBalance() {
        return accountMoneyBalance;
    }

    public String getBankName() {
        return bankName;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNum=" + accountNum +
                ", accountMoneyBalance=" + accountMoneyBalance +
                ", accountOpenedDate=" + accountOpenedDate +
                ", currency='" + currency + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
