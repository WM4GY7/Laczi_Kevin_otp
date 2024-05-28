public class Customer {
    private String webshopId;
    private String customerId;
    private String name;
    private String address;

    public Customer(String webshopId, String customerId, String name, String address) {
        this.webshopId = webshopId;
        this.customerId = customerId;
        this.name = name;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getWebshopId() {
        return webshopId;
    }

}
