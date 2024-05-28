public class Payment {
    private String webshopId;
    private String customerId;
    private String paymentMethod;
    private int amount;
    private String bankAccount;
    private String cardNumber;
    private String paymentDate;

    public String getWebshopId() {
        return webshopId;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public int getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getCustomerId() {
        return customerId;
    }


    public Payment(String webshopId, String paymentDate, String cardNumber, String bankAccount, int amount, String paymentMethod, String customerId) {
        this.webshopId = webshopId;
        this.paymentDate = paymentDate;
        this.cardNumber = cardNumber;
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.customerId = customerId;
    }
}
