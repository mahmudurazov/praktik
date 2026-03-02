import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class BankAccount {
    private String ownerName;
    private int balance;
    private LocalDateTime openDate;
    private boolean isBlocked;
    private String number;

    public BankAccount(String ownerName) {
        this.ownerName = ownerName;
        this.balance = 0;
        this.openDate = LocalDateTime.now();
        this.isBlocked = false;
        this.number = generateAccountNumber();
    }

    private String generateAccountNumber() {
        Random random = new Random();
        return String.format("%08d", random.nextInt(100000000));
    }

    public boolean deposit(int amount) {
        if (amount > 0 && !isBlocked) {
            this.balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(int amount) {
        if (isBlocked) return false;
        if (amount <= 0) return false;
        if (amount > this.balance) return false;
        this.balance -= amount;
        return true;
    }

    public boolean transfer(BankAccount otherAccount, int amount) {
        if (isBlocked) return false;
        if (otherAccount == null) return false;
        if (amount <= 0) return false;
        if (amount > this.balance) return false;
        this.balance -= amount;
        otherAccount.balance += amount;
        return true;
    }

    public int getBalance() { return balance; }
    public boolean isBlocked() { return isBlocked; }
    public String getNumber() { return number; }
    public String getOwnerName() { return ownerName; }
    public LocalDateTime getOpenDate() { return openDate; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return String.format("Счёт: %s\nВладелец: %s\nБаланс: %d руб.\nОткрыт: %s\nСтатус: %s",
                number, ownerName, balance, openDate.format(formatter),
                isBlocked ? "ЗАБЛОКИРОВАН" : "АКТИВЕН");
    }
}