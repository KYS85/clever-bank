Clever-Bank (Тестовый проект - консольное приложение для Clever-Bank)

Основные сущности:
- Банк
- Счёт
- Пользователь
- Транзакция
  
Стек:
- Java 17
- Gradle
- PostgreSQL
- JDBC

Объём данных:
1. Банков - 5
2. Пользователей - 20
3. Счетов - 40
4. Также у пользователей могут быть счета в разных банках
   
Реализовано:
1. Операции пополнения и снятия средств со счета
2. Возможность перевода средств другому клиенту Clever-Bank и
клиенту другого банка (ри переводе средств блокируются ячеки таблицы базы данных для обеспечения безопасности и
сохранности данных, после операции блокировка снимается командой commit)
4. Регулярно, по расписанию (раз в полминуты), проверяется, нужно ли начислять проценты (1% - значение подставляется из конфигурационного файла) на остаток счета в конце месяца
● Проверку и начисление процентов нужно реализована асинхронно (в одтельном потоке)
5. Значения хранится в конфигурационном файле - .yml
6. После каждой операции сформируется чек 
● Чеки сохраняются в папку check, в корне проекта
7. Проект запускается из класса Main
8. PostgreSQL база данных подключается локально url: 127.0.0.1:5432/pgCleverBankDB";
            username = "postgres";
            password = "PgRoot777";
- взаимодействие с базой данных осуществляется через интерфейс приложения
  схема базы данных:
<img width="293" alt="Снимок экрана 2023-09-05 в 10 06 27" src="https://github.com/KYS85/clever-bank/assets/98476503/f380b6a7-7f4b-4ead-8be6-eba043877a94">
  скриншоты таблиц:
  <img width="478" alt="Снимок экрана 2023-09-05 в 10 08 43" src="https://github.com/KYS85/clever-bank/assets/98476503/95c91d71-e87a-4ab1-88ef-9f5e3c465035">
<img width="1440" alt="Снимок экрана 2023-09-05 в 10 08 03" src="https://github.com/KYS85/clever-bank/assets/98476503/1da6b0ba-26c9-4d43-84c1-1ebf91952092">
<img width="239" alt="Снимок экрана 2023-09-05 в 10 08 30" src="https://github.com/KYS85/clever-bank/assets/98476503/e2c48a74-7fdf-4dd8-9807-7fe17c9115da">
