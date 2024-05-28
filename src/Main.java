import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        Webshop service = new Webshop();
        try {
            service.loadData();
            service.generateReports();
            service.generateTopCustomers();
            service.generateWebshopReport();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}