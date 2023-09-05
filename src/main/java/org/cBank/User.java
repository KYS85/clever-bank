package org.cBank;

/**
 * пользовательский класс которы содержит класс счёта (аккаунт) и нужен для передачи объекта пользователя в банк
 */
public class User {

    private final String userName;
    private final Account account;


    public User(String userName, Account account) {
        this.userName = userName;
        this.account = account;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", account=" + account +
                '}';
    }

    public Account getAccount() {
        return account;
    }

}
