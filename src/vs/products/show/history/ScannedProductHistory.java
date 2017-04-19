package vs.products.show.history;

import vs.products.ScannedProduct;

import java.util.List;

/**
 * Created by franky3er on 18.04.17.
 */
public interface ScannedProductHistory {
    List<ScannedProduct> getTimeStampSortedScannedProducts(String productName);
    void printTimeStampSortedScannedProducts(String productName);
}
