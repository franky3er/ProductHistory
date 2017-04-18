package vs.producthistory.historyutils;

import vs.products.ScannedProduct;

import java.util.List;

/**
 * Created by franky3er on 18.04.17.
 */
public interface ScannedProductHistory {
    List<ScannedProduct> getTimeStampSortedScannedProducts(String productName);
    void showTimeStampSortedScannedProducts(String productName);
}
