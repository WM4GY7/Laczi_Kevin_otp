import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Webshop {
    // Ügyfelek és fizetések tárolása
    private Map<String, Customer> customers = new HashMap<>(); //Kulcs érték párok tárolása
    private List<Payment> payments = new ArrayList<>();

    // Adatok beolvasása
    public void loadData() throws IOException {
        // Ügyfelek beolvasása
        BufferedReader reader = new BufferedReader(new FileReader("customer.csv"));
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                String[] parts = line.split(",");
                String webshopId = parts[0];
                String customerId = parts[1];
                String name = parts[2];
                String address = parts[3];
                customers.put(customerId, new Customer(webshopId, customerId, name, address));
            } catch (Exception e) {
                // Hiba logolása
                logError(e, line);
            }
        }
        reader.close();

        // Fizetések beolvasása
        reader = new BufferedReader(new FileReader("payments.csv"));
        while ((line = reader.readLine()) != null) {
            try {
                String[] parts = line.split(",");
                String webshopId = parts[0];
                String customerId = parts[1];
                String paymentMethod = parts[2];
                int amount = Integer.parseInt(parts[3]);
                String bankAccount = parts[4];
                String cardNumber = parts[5];
                String paymentDate = parts[6];
                payments.add(new Payment(webshopId, paymentDate, cardNumber, bankAccount, amount, paymentMethod, customerId));
            } catch (Exception e) {
                // Hiba logolása
                logError(e, line);
            }
        }
        reader.close();
    }

    // Hiba logolása
    private void logError(Exception e, String line) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("application.log", true));
            writer.println("Hiba a sor feldolgozásakor: " + line);
            writer.println("Hibaüzenet: " + e.getMessage());
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Riport generálása
    public void generateReports() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("report01.csv"));
        writer.println("NAME,ADDRESS,TOTAL_PURCHASE");
        for (Customer customer : customers.values()) {
            List<Payment> customerPayments = payments.stream()
                    .filter(payment -> payment.getCustomerId().equals(customer.getCustomerId()))
                    .collect(Collectors.toList());
            int totalPurchase = 0;
            for (Payment payment : customerPayments) {
                totalPurchase += payment.getAmount();
            }
            writer.println(customer.getName() + "," + customer.getAddress() + "," + totalPurchase);
        }
        writer.close();
    }

    // A legtöbbet költő ügyfelek riportjának generálása
    public void generateTopCustomers() throws IOException {
        // Ügyfelek és összes vásárlásuk listájának létrehozása
        List<Map.Entry<Customer, Integer>> customerList = new ArrayList<>();
        for (Customer customer : customers.values()) {
            List<Payment> customerPayments = payments.stream()
                    .filter(payment -> payment.getCustomerId().equals(customer.getCustomerId()))
                    .collect(Collectors.toList());
            int totalPurchase = 0;
            for (Payment payment : customerPayments) {
                totalPurchase += payment.getAmount();
            }
            customerList.add(new AbstractMap.SimpleEntry<>(customer, totalPurchase));
        }

        // A lista rendezése a vásárlások összege szerint csökkenő sorrendben
        customerList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // A top 2 ügyfél kiírása a top.csv-be
        PrintWriter writer = new PrintWriter(new FileWriter("top.csv"));
        writer.println("NAME,ADDRESS,TOTAL_PURCHASE");
        for (int i = 0; i < 2; i++) {
            Map.Entry<Customer, Integer> entry = customerList.get(i); //A Map.Entry egy kulcs-érték párt reprezentál a Map adatszerkezetben.
            Customer customer = entry.getKey();
            int totalPurchase = entry.getValue();
            writer.println(customer.getName() + "," + customer.getAddress() + "," + totalPurchase);
        }
        writer.close();
    }

    // Webshopok bevételeinek riportja
    public void generateWebshopReport() throws IOException {
        // Hash létrehozása a kártyás és átutalásos fizetések összegének tárolására minden webshop számára
        Map<String, int[]> webshopPayments = new HashMap<>();
        for (Payment payment : payments) {
            String webshopId = payment.getWebshopId();
            String paymentMethod = payment.getPaymentMethod();
            int amount = payment.getAmount();

            // Ha a webshop még nincs a térképen, adjuk hozzá
            if (!webshopPayments.containsKey(webshopId)) {
                webshopPayments.put(webshopId, new int[2]);
            }

            // A fizetési összeg hozzáadása a megfelelő fizetési módhoz
            if (paymentMethod.equals("Card")) {
                webshopPayments.get(webshopId)[0] += amount;
            } else if (paymentMethod.equals("Transfer")) {
                webshopPayments.get(webshopId)[1] += amount;
            }
        }

        // A riport kiírása a report02.csv-be
        PrintWriter writer = new PrintWriter(new FileWriter("report02.csv"));
        writer.println("WEBSHOP,CARD_PAYMENTS,TOTAL_TRANSFER");
        for (Map.Entry<String, int[]> entry : webshopPayments.entrySet()) { //Minden iterációjában az entry változó a következő bejegyzést fogja tartalmazni a webshopPayments map-ból
            String webshopId = entry.getKey(); //hozzáférhetünk a kulcshoz
            int[] amounts = entry.getValue();
            writer.println(webshopId + "," + amounts[0] + "," + amounts[1]);
        }
        writer.close();
    }

}
