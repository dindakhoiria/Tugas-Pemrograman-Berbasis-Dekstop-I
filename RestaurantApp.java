import java.util.ArrayList;
import java.util.Scanner;

class MenuItem {
    String name;
    double price;
    String category;

    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }
}

class OrderItem {
    MenuItem menuItem;
    int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return menuItem.price * quantity;
    }
}

public class RestaurantApp {
    static ArrayList<MenuItem> menu = new ArrayList<>();
    static ArrayList<OrderItem> order = new ArrayList<>();
    static final double TAX_RATE = 0.1;
    static final double SERVICE_FEE = 20000;

    public static void main(String[] args) {
        initMenu();
        System.out.println("Selamat Datang di Resto Pelipur Lapar");
        takeOrder();
        printReceipt();
    }

    public static void initMenu() {
        // Menambahkan beberapa item makanan dan minuman ke dalam menu
        menu.add(new MenuItem("Nasi Ayam", 30000, "Makanan"));
        menu.add(new MenuItem("Nasi Goreng", 20000, "Makanan"));
        menu.add(new MenuItem("Iga", 30000, "Makanan"));
        menu.add(new MenuItem("Ikan Bakar", 25000, "Makanan"));
        menu.add(new MenuItem("Es Teh", 5000, "Minuman"));
        menu.add(new MenuItem("Es Jeruk", 7000, "Minuman"));
    }

    public static void displayMenuByCategory(String category) {
        System.out.println("\nPilih Nomor Kategori (1/2), 'Selesai' untuk KELUAR:");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            if (item.category.equalsIgnoreCase(category)) {
                System.out.printf("%d. %s = Rp%.1f%n", i + 1, item.name, item.price);
            }
        }
    }

    public static void takeOrder() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("Pilih Kategori Menu:");
            System.out.println("1. Makanan");
            System.out.println("2. Minuman");
            System.out.print("Pilih Nomor Kategori (1/2), 'Selesai' untuk KELUAR: ");
            String categoryChoice = scanner.next();

            String category = "";
            if (categoryChoice.equals("1")) {
                category = "Makanan";
            } else if (categoryChoice.equals("2")) {
                category = "Minuman";
            } else if (categoryChoice.equalsIgnoreCase("Selesai")) {
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
                continue;
            }

            displayMenuByCategory(category);

            System.out.print("Pesan menu dengan memilih nomor pada daftar menu atau 'Selesai' untuk KELUAR: ");
            int menuNumber = scanner.nextInt();
            if (menuNumber <= 0 || menuNumber > menu.size()) {
                System.out.println("Nomor menu tidak valid atau keluar dari kategori.");
                continue;
            }

            System.out.print("Jumlah yang dipesan: ");
            int quantity = scanner.nextInt();
            MenuItem item = menu.get(menuNumber - 1);
            order.add(new OrderItem(item, quantity));
        }
    }

    public static void printReceipt() {
        double subtotal = 0;
        boolean hasDrink = false;

        System.out.println("\n=== Struk Pesanan ===");
        for (OrderItem orderItem : order) {
            double totalPrice = orderItem.getTotalPrice();
            subtotal += totalPrice;
            System.out.printf("%s x%d - Rp%.2f%n", orderItem.menuItem.name, orderItem.quantity, totalPrice);
            if (orderItem.menuItem.category.equalsIgnoreCase("Minuman")) {
                hasDrink = true;
            }
        }

        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax + SERVICE_FEE;

        if (subtotal > 100000) {
            double discount = subtotal * 0.1;
            total -= discount;
            System.out.printf("Diskon 10%%: -Rp%.2f%n", discount);
        } else if (subtotal > 50000 && hasDrink) {
            System.out.println("Penawaran: Beli 1 Gratis 1 untuk kategori minuman.");
        }

        System.out.printf("Subtotal: Rp%.2f%n", subtotal);
        System.out.printf("Pajak (10%%): Rp%.2f%n", tax);
        System.out.printf("Biaya Pelayanan: Rp%.2f%n", SERVICE_FEE);
        System.out.printf("Total: Rp%.2f%n", total);
    }
}