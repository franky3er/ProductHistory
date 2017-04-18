package vs.producthistory.main;

import vs.products.show.history.ScannedProductHistory;
import vs.products.show.history.sql.SQLScannedProductHistory;
import vs.products.iohandler.database.ProductDatabaseHandler;
import vs.products.iohandler.database.sqlite.ProductSQLiteHandler;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by franky3er on 18.04.17.
 */
public class MainApplication {

    private final static String PRODUCTHISTORY_PROJECT_NAME = "ProductHistory";

    private static String OS = System.getProperty("os.name").toLowerCase();

    private final static String PRODUCTRECEIVER_PRODUCTSQLITE_FILESOURCE = "ProductHistory.ProductSQLiteHandler.FileSource";
    private final static String PRODUCTRECEIVER_PRODUCTSQLITE_DRIVER = "ProductHistory.ProductSQLiteHandler.Driver";


    public static void main(String[] args) {
        Properties properties = null;
        try {
            properties = loadProperties();
            ScannedProductHistory scannedProductHistory = initializeScannedProductHistory(properties);
            run(scannedProductHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void run(ScannedProductHistory scannedProductHistory) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a product name: ");
        scannedProductHistory.showTimeStampSortedScannedProducts(scanner.nextLine());
    }

    private static ScannedProductHistory initializeScannedProductHistory(Properties properties) {
        return new SQLScannedProductHistory(new ProductSQLiteHandler(properties.getProperty(PRODUCTRECEIVER_PRODUCTSQLITE_DRIVER),
                properties.getProperty(PRODUCTRECEIVER_PRODUCTSQLITE_FILESOURCE)));
    }

    private static Properties loadProperties() throws IOException {
        System.out.println("INFO : Load config");
        String sourceProperties = System.getProperty("user.dir") + getPathSeparator() + "config"
                + getPathSeparator() + PRODUCTHISTORY_PROJECT_NAME + ".properties";
        Properties properties = new Properties();
        properties.load(new FileReader(sourceProperties));
        return properties;
    }


    private static String getPathSeparator() {
        if (OS.indexOf("win") >= 0) {
            return "\\";
        }
        return "/";
    }

}
