package vs.products.iohandler.database.sqlite;

import vs.products.SQLHandableScannedProduct;
import vs.products.ScannedProduct;
import vs.products.factory.ScannedProductFactory;
import vs.products.iohandler.database.ProductDatabaseHandler;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class implements the ProductIOHandler to read and write ScannedProduct's
 * to the concrete implemented SQLite DB.
 */
public class ProductSQLiteHandler extends ProductDatabaseHandler {

    private String dbFileSource;

    public ProductSQLiteHandler(String driver, String dbFileSource) {
        super(driver);
        this.dbFileSource = dbFileSource;
        this.connect();
        if( this.isConnected() ){
            this.createTablesIfNotExist();
        }
    }

    @Override
    public void write(ScannedProduct scannedProduct) {
        if( !this.isConnected() ){
            System.err.println("ERROR : Can't insert scanned product. Not connected to DB");
            return;
        }

        SQLHandableScannedProduct sqlHandableScannedProduct = new SQLHandableScannedProduct(scannedProduct);
        String sql = sqlHandableScannedProduct.getSQLInsertStatement();

        try {
            System.out.println("INFO : Write ScannedProduct to SQLite DB");
            executeStatements(sql);
        } catch (SQLException e) {
            System.err.println("ERROR : Insert scanned product Failed.");
            e.printStackTrace();
        }
    }

    @Override
    public List<ScannedProduct> read(String statement) {
        List<ScannedProduct> scannedProducts = new ArrayList<>();
        try {
            System.out.println("INFO : Read List<ScannedProduct> from SQLite DB");
            System.out.println(String.format("INFO : ExecuteQuery: %s", statement));
            Statement statement1 = super.connection.createStatement();
            ResultSet resultSet = statement1.executeQuery(statement);
            while(resultSet.next()){
                scannedProducts.add(ScannedProductFactory.build(resultSet));
            }
        } catch (SQLException e) {
            System.err.println(String.format("ERROR : Read scanned vs.products failed: %s", statement));
            e.printStackTrace();
        }
        return scannedProducts;

    }

    @Override
    public void connect() {
        System.out.println(String.format("INFO : Connect to SQLite DB: %s", this.dbFileSource));
        try {
            Class.forName(super.driver);
            super.connection = DriverManager.getConnection("jdbc:sqlite:" + this.dbFileSource);
        } catch (ClassNotFoundException e) {
            System.err.println(String.format("ERROR : Driver Problems: %s", super.driver));
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println(String.format(String.format("ERROR : Connect to DB-File failed: %s", this.dbFileSource)));
            e.printStackTrace();
        }
    }

    public String getDbFileSource() {
        return dbFileSource;
    }

    public void setDbFileSource(String dbFileSource) {
        this.dbFileSource = dbFileSource;
    }
}
