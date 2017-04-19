package vs.products.show.history.sql;

import vs.products.HumanReadableScannedProduct;
import vs.products.ScannedProduct;
import vs.products.iohandler.database.ProductDatabaseHandler;
import vs.products.show.history.ScannedProductHistory;

import java.util.List;

/**
 * Created by franky3er on 18.04.17.
 */
public class SQLScannedProductHistory implements ScannedProductHistory {
    private ProductDatabaseHandler databaseHandler;

    public SQLScannedProductHistory(ProductDatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    @Override
    public List<ScannedProduct> getTimeStampSortedScannedProducts(String productName) {
        String sql = String.format("SELECT %s, %s, %s, %s " +
                "FROM %s INNER JOIN %S ON %s=%s " +
                "WHERE %s='%s' " +
                "ORDER BY %s ASC; ",
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_AMMOUNT_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE_PRODUCTUNIT_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_TIMESTAMP_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE,
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME_COMPLETE,
                ProductDatabaseHandler.PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE, productName,
                ProductDatabaseHandler.SCANNEDPRODUCT_TABLE_PK_TIMESTAMP_COMPLETE);
        return databaseHandler.read(sql);
    }

    @Override
    public void printTimeStampSortedScannedProducts(String productName) {
        List<ScannedProduct> scannedProducts = this.getTimeStampSortedScannedProducts(productName);
        if(scannedProducts.isEmpty()) {
            System.out.println(String.format("Not entries for product name: %s", productName));
            return;
        }

        for(ScannedProduct scannedProduct : scannedProducts) {
            HumanReadableScannedProduct humanReadableScannedProduct =
                    new HumanReadableScannedProduct(scannedProduct);
            System.out.println(humanReadableScannedProduct.getHumanReadableProductState());
        }
    }
}
