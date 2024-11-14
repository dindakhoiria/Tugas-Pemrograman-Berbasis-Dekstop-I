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
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Selamat Datang di Resto Pelipur Lapar ===");
            System.out.println("1. Menu Pelanggan");
            System.out.println("2. Manajemen Menu (Pemilik Restoran)");
            System.out.println("3. Keluar");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                takeOrder();
                printReceipt();
            } else if (choice == 2) {
                manageMenu();
            } else if (choice == 3) {
                System.out.println("Terima kasih telah menggunakan aplikasi!");
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
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

    public static void manageMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Manajemen Menu ===");
            System.out.println("1. Tambah Menu Baru");
            System.out.println("2. Ubah Harga Menu");
            System.out.println("3. Hapus Menu");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                addNewMenuItem();
            } else if (choice == 2) {
                updateMenuItemPrice();
            } else if (choice == 3) {
                deleteMenuItem();
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public static void addNewMenuItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nama menu baru: ");
        String name = scanner.nextLine();
        System.out.print("Harga: ");
        double price = scanner.nextDouble();
        System.out.print("Kategori (Makanan/Minuman): ");
        scanner.nextLine();  // consume newline
        String category = scanner.nextLine();

        menu.add(new MenuItem(name, price, category));
        System.out.println("Menu baru berhasil ditambahkan.");
    }

    public static void updateMenuItemPrice() {
        Scanner scanner = new Scanner(System.in);
        displayMenu();
        System.out.print("Pilih nomor menu yang ingin diubah harganya: ");
        int menuNumber = scanner.nextInt();

        if (menuNumber <= 0 || menuNumber > menu.size()) {
            System.out.println("Nomor menu tidak valid.");
            return;
        }

        MenuItem item = menu.get(menuNumber - 1);
        System.out.print("Harga baru untuk " + item.name + ": ");
        double newPrice = scanner.nextDouble();
        System.out.print("Apakah Anda yakin ingin mengubah harga? (Ya/Tidak): ");
        scanner.nextLine();  // consume newline
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Ya")) {
            item.price = newPrice;
            System.out.println("Harga menu berhasil diubah.");
        } else {
            System.out.println("Perubahan dibatalkan.");
        }
    }
    public static void deleteMenuItem() {
        Scanner scanner = new Scanner(System.in);
        displayMenu();
        System.out.print("Pilih nomor menu yang ingin dihapus: ");
        int menuNumber = scanner.nextInt();

        if (menuNumber <= 0 || menuNumber > menu.size()) {
            System.out.println("Nomor menu tidak valid.");
            return;
        }

        MenuItem item = menu.get(menuNumber - 1);
        System.out.print("Apakah Anda yakin ingin menghapus " + item.name + "? (Ya/Tidak): ");
        scanner.nextLine();  // consume newline
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("Ya")) {
            menu.remove(menuNumber - 1);
            System.out.println("Menu berhasil dihapus.");
        } else {
            System.out.println("Penghapusan dibatalkan.");
        }
    }

    public static void displayMenu() {
        System.out.println("\n=== Daftar Menu ===");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.get(i);
            System.out.printf("%d. %s - Rp%.2f (%s)%n", i + 1, item.name, item.price, item.category);
        }
    }

    public static void takeOrder() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            System.out.print("Pilih nomor menu atau '0' untuk selesai: ");
            int menuNumber = scanner.nextInt();

            if (menuNumber == 0) {
                break;
            } else if (menuNumber < 0 || menuNumber > menu.size()) {
                System.out.println("Nomor menu tidak valid.");
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
