import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// Kelas abstrak MenuItem
abstract class MenuItem {
    private String name;
    private double price;
    private String category;

    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Metode abstrak
    public abstract void tampilMenu();
}

// Subkelas Makanan
class Makanan extends MenuItem {
    private String jenisMakanan;

    public Makanan(String name, double price, String jenisMakanan) {
        super(name, price, "Makanan");
        this.jenisMakanan = jenisMakanan;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("Makanan: %s - Rp%.2f (Jenis: %s)%n", getName(), getPrice(), jenisMakanan);
    }
}

// Subkelas Minuman
class Minuman extends MenuItem {
    private String jenisMinuman;

    public Minuman(String name, double price, String jenisMinuman) {
        super(name, price, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("Minuman: %s - Rp%.2f (Jenis: %s)%n", getName(), getPrice(), jenisMinuman);
    }
}

// Subkelas Diskon
class Diskon extends MenuItem {
    private double diskon;

    public Diskon(String name, double diskon) {
        super(name, 0, "Diskon");
        this.diskon = diskon;
    }

    public double getDiskon() {
        return diskon;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("Diskon: %s - %.0f%%%n", getName(), diskon * 100);
    }
}

// Kelas Menu untuk mengelola semua item
class Menu {
    private ArrayList<MenuItem> items;

    public Menu() {
        this.items = new ArrayList<>();
    }

    public void tambahItem(MenuItem item) {
        items.add(item);
    }

    public void tampilkanMenu() {
        System.out.println("\n=== Daftar Menu ===");
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. ", i + 1);
            items.get(i).tampilMenu();
        }
    }

    public MenuItem getItem(int index) throws IndexOutOfBoundsException {
        return items.get(index);
    }

    public void simpanKeFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (MenuItem item : items) {
                writer.write(item.getName() + "," + item.getPrice() + "," + item.getCategory() + "\n");
            }
        }
    }

    public void muatDariFile(String fileName) throws IOException {
        items.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                String category = parts[2];
                if (category.equals("Makanan")) {
                    items.add(new Makanan(name, price, "Umum"));
                } else if (category.equals("Minuman")) {
                    items.add(new Minuman(name, price, "Dingin"));
                }
            }
        }
    }
}

// Kelas Pesanan
class Pesanan {
    private ArrayList<MenuItem> items;

    public Pesanan() {
        this.items = new ArrayList<>();
    }

    public void tambahPesanan(MenuItem item) {
        items.add(item);
    }

    public double hitungTotal() {
        double total = 0;
        for (MenuItem item : items) {
            if (item instanceof Diskon) {
                total -= total * ((Diskon) item).getDiskon();
            } else {
                total += item.getPrice();
            }
        }
        return total;
    }

    public void tampilkanStruk() {
        System.out.println("\n=== Struk Pesanan ===");
        for (MenuItem item : items) {
            item.tampilMenu();
        }
        System.out.printf("Total: Rp%.2f%n", hitungTotal());
    }

    public void simpanStrukKeFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (MenuItem item : items) {
                writer.write(item.getName() + "," + item.getPrice() + "\n");
            }
            writer.write("Total: " + hitungTotal() + "\n");
        }
    }
}

// Kelas Utama
public class RestaurantApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu();
        Pesanan pesanan = new Pesanan();

        try {
            menu.muatDariFile("menu.txt");
        } catch (IOException e) {
            System.out.println("File menu tidak ditemukan. Memulai dengan menu kosong.");
        }

        while (true) {
            System.out.println("\n=== Selamat Datang di Restoran Pelipur Lapar ===");
            System.out.println("1. Tambah Item Baru ke Menu");
            System.out.println("2. Tampilkan Menu Restoran");
            System.out.println("3. Buat Pesanan");
            System.out.println("4. Tampilkan Struk Pesanan");
            System.out.println("5. Keluar");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Jenis Item (1: Makanan, 2: Minuman, 3: Diskon): ");
                    int jenis = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Nama: ");
                    String name = scanner.nextLine();
                    System.out.print("Harga: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    if (jenis == 1) {
                        menu.tambahItem(new Makanan(name, price, "Umum"));
                    } else if (jenis == 2) {
                        menu.tambahItem(new Minuman(name, price, "Dingin"));
                    } else if (jenis == 3) {
                        System.out.print("Diskon (misalnya 0.1 untuk 10%): ");
                        double discount = scanner.nextDouble();
                        menu.tambahItem(new Diskon(name, discount));
                    }
                    break;

                case 2:
                    menu.tampilkanMenu();
                    break;

                case 3:
                    menu.tampilkanMenu();
                    System.out.print("Pilih nomor item: ");
                    int itemNumber = scanner.nextInt();
                    try {
                        MenuItem item = menu.getItem(itemNumber - 1);
                        pesanan.tambahPesanan(item);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Item tidak ditemukan.");
                    }
                    break;

                case 4:
                    pesanan.tampilkanStruk();
                    try {
                        pesanan.simpanStrukKeFile("struk.txt");
                    } catch (IOException e) {
                        System.out.println("Gagal menyimpan struk.");
                    }
                    break;

                case 5:
                    try {
                        menu.simpanKeFile("menu.txt");
                    } catch (IOException e) {
                        System.out.println("Gagal menyimpan menu.");
                    }
                    System.out.println("Terima kasih telah menggunakan aplikasi!");
                    return;

                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }
}
