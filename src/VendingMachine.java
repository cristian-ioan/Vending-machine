import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class VendingMachine {
    private VMType vmType;
    private CoinType coinType;
    private Map<Product, Integer> productStock;
    private Map<Currency, Integer> coinStock;

    public VendingMachine(String filePath) {
        initialize(filePath);
    }

    public void displayMenu(){
        System.out.println("This is a " + vmType + " vending machine.");
        System.out.println("Cod\tProdus\tPret\tGramaj\tCantitate");
        for (Product product : productStock.keySet()){
            System.out.println(product.getCod() + "\t" + product.getName() + "\t" + product.getPrice() + " " +
                    coinType + "\t" + product.getSize() + "\t\t" + productStock.get(product));
        }
        System.out.println("0 - Iesire");
    }


    public void displayCoin(){
        System.out.println("Monede in stock:");
        System.out.println("NumeMoneda\tValoareMoneda\tStockMoneda");
        for(Currency coin : coinStock.keySet()){
            System.out.println(coin.getCoinCod() + "\t" + coin.getCoinName() + "\t\t" + coin.getCoinValue() + "\t\t" + coinStock.get(coin));
        }
    }

    public void displayLoading(){
        System.out.print("Loading:");
        for (int i = 0; i < 15; i++) {
            System.out.print(".");
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public int enterCoin(){
        System.out.println("Introduceti monezile(VM-ul accepta doar monede 1 / 5 / 10 " + coinType + "). " +
                "Tastati 0 cand ati terminat de introdus monezi.");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        boolean type = true;
        int sum = 0;

        while (option != 0 && type == true) {
            for (Currency c : coinStock.keySet()) {
                if (c.getCoinValue() == option) {
                    int coinQuantity = coinStock.get(c);
                    if (c.getCoinValue() == 1) {
                        coinStock.put(c, coinQuantity + 1);
                        sum = sum + option;
                    }
                    if (c.getCoinValue() == 5) {
                        coinStock.put(c, coinQuantity + 1);
                        sum = sum + option;
                    }
                    if (c.getCoinValue() == 10) {
                        coinStock.put(c, coinQuantity + 1);
                        sum = sum + option;
                    }
                    option = scanner.nextInt();
                    if (option == 0){
                        type = false;
                    }
                }
            }
        }
        return sum;
    }

    public void decreaseCoin(int n){
        if (n < 5){
            for (Currency c : coinStock.keySet()) {
                if (c.getCoinValue() == 1) {
                    int coinQuantity = coinStock.get(c);
                    coinStock.put(c, coinQuantity - n);
                }
            }
        }
        if (n >= 5 && n < 10) {
            int m1 = n - 5; // tipul monezii m1
            int m2 = 1;
            for (Currency c : coinStock.keySet()) {
                if (c.getCoinValue() == 1) {
                    int coinQuantity = coinStock.get(c);
                    coinStock.put(c, coinQuantity - m1);
                }
                if (c.getCoinValue() == 5){
                    int coinQuantity = coinStock.get(c);
                    coinStock.put(c, coinQuantity - m2);
                 }
            }
        }
    }

    public void buyProduct(){
        System.out.println("Alege un produs:");
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        if (option != 0) {
            for (Product p : productStock.keySet()) {
                if (p.getCod() == option) {
                    int quantity = productStock.get(p);
                    int price = p.getPrice(); // valoarea produsului selectat
                    int sum = enterCoin(); // suma introdusa de client
                    int rest = 0;
                    if (price > sum){
                        System.out.println("Nu ati introdus suficiente monede!");
                    } else {
                        if (quantity > 0) {
                            productStock.put(p, quantity - 1);
                            displayLoading();
                            System.out.println("Ati cumparat produsul: " + p.getName());
                            rest = sum - price;
                            if (rest > 0){
                                System.out.println("Restul dumneavoastra: " + rest +  " " + coinType);
                                decreaseCoin(rest);
                            }
                        } else {
                            System.out.println("Nu sunt produse suficiente.");
                        }
                    }
                    displayCoin();
                }
            }
        } else{
            System.out.println("Va multumim pentru alegerea facuta!");
            System.exit(0);
        }
    }

    public void start(){
        while (true){
            displayMenu();
            buyProduct();
        }
    }

    public void initialize(String filePath){
        Path path = Paths.get(filePath);
        List<String> lines = null;

        try{
            lines = Files.readAllLines(path);

        } catch(IOException e){
            e.printStackTrace();
        }

        vmType = VMType.valueOf(lines.get(0));
        coinType = CoinType.valueOf(lines.get(1));
        int productNumber = Integer.valueOf(lines.get(2)); // nr de produse din VM
        int lineCurrency = productNumber + 3; // pozitia din fisier la care se gaseste "cate tipuri de monezi"
        productStock = new LinkedHashMap<>();
        coinStock = new LinkedHashMap<>();

        for (int i = 3; i < productNumber + 3; i++) {
            String line = lines.get(i);
            String[] parts = line.split(" ");
            Product product = new Product(Integer.valueOf(parts[0]), parts[1], Integer.valueOf(parts[2]),
                    Integer.valueOf(parts[3]));
            productStock.put(product, Integer.valueOf(parts[4]));
        }

        for(int i = lineCurrency + 1; i < lines.size(); i++){
            String coinLine = lines.get(i);
            String[] coinParts = coinLine.split(" ");
            Currency coin = new Currency(Integer.valueOf(coinParts[0]), coinParts[1], Integer.valueOf(coinParts[2]));
            coinStock.put(coin, Integer.valueOf(coinParts[3]));
        }
    }

}
