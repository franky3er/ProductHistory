package vs.producthistory.main;

import vs.products.show.history.ScannedProductHistory;
import vs.products.show.history.sql.SQLScannedProductHistory;
import vs.products.iohandler.database.ProductDatabaseHandler;
import vs.products.iohandler.database.sqlite.ProductSQLiteHandler;

/**
 * Created by franky3er on 18.04.17.
 */
public class MainApplication {
    public static void main (String []args) {
        ProductDatabaseHandler productDatabaseHandler = new ProductSQLiteHandler("org.sqlite.JDBC", "/home/franky3er/IdeaProjects/ProductReceiver/running/fridge.db");
        ScannedProductHistory history = new SQLScannedProductHistory(productDatabaseHandler);
        history.showTimeStampSortedScannedProducts("Beer");
    }
}
