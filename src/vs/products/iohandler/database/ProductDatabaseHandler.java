package vs.products.iohandler.database;

import vs.products.ScannedProduct;
import vs.products.iohandler.ProductIOHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * This abstract class implements the ProductIOHandler to read and write ScannedProduct's
 * to the concrete implemented database.
 */
public abstract class ProductDatabaseHandler implements ProductIOHandler{
    public final static String PRODUCT_TABLE = "Product";
    public final static String PRODUCT_TABLE_PK_PRODUCTNAME = "productName";
    public final static String PRODUCT_TABLE_PK_PRODUCTNAME_COMPLETE = PRODUCT_TABLE + "." + PRODUCT_TABLE_PK_PRODUCTNAME;
    public final static String PRODUCT_TABLE_PRODUCTUNIT = "productUnit";
    public final static String PRODUCT_TABLE_PRODUCTUNIT_COMPLETE = PRODUCT_TABLE + "." + PRODUCT_TABLE_PRODUCTUNIT;


    public final static String SCANNEDPRODUCT_TABLE = "ScannedProduct";
    public final static String SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME = PRODUCT_TABLE_PK_PRODUCTNAME;
    public final static String SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME_COMPLETE = SCANNEDPRODUCT_TABLE + "." + SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME;
    public final static String SCANNEDPRODUCT_TABLE_PK_TIMESTAMP = "timeStamp";
    public final static String SCANNEDPRODUCT_TABLE_PK_TIMESTAMP_COMPLETE = SCANNEDPRODUCT_TABLE + "." + SCANNEDPRODUCT_TABLE_PK_TIMESTAMP;
    public final static String SCANNEDPRODUCT_TABLE_AMMOUNT = "ammount";
    public final static String SCANNEDPRODUCT_TABLE_AMMOUNT_COMPLETE = SCANNEDPRODUCT_TABLE + "." + SCANNEDPRODUCT_TABLE_AMMOUNT;

    protected String driver = null;
    protected Connection connection = null;

    public ProductDatabaseHandler(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public abstract void write(ScannedProduct scannedProduct);
    public abstract List<ScannedProduct> read(String statement);
    public abstract void connect();

    /**
     * Checks if the there is a running connection to the db
     *
     * @return boolean
     *      true --> connected
     *      false --> not connected
     */
    public boolean isConnected() {
        try {
            return !(this.connection == null || !this.connection.isValid(10));
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Creates the necessary tables in the db if they don't exist
     */
    public void createTablesIfNotExist() {
        System.out.println("INFO : Create necessary tables if not existing");
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "%s VARCHAR(100), " +
                "%s VARCHAR(100), " +
                "PRIMARY KEY (%s)); ", PRODUCT_TABLE,
                PRODUCT_TABLE_PK_PRODUCTNAME,
                PRODUCT_TABLE_PRODUCTUNIT,
                PRODUCT_TABLE_PK_PRODUCTNAME);
        sql += String.format("CREATE TABLE IF NOT EXISTS %s (" +
                "%s VARCHAR(100), " +
                "%s DATETIME, " +
                "%s VARCHAR(100), " +
                "PRIMARY KEY (%s, %s), " +
                "FOREIGN KEY (%s) REFERENCES %s(%s)); ", SCANNEDPRODUCT_TABLE,
                SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME,
                SCANNEDPRODUCT_TABLE_PK_TIMESTAMP,
                SCANNEDPRODUCT_TABLE_AMMOUNT,
                SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME, SCANNEDPRODUCT_TABLE_PK_TIMESTAMP,
                SCANNEDPRODUCT_TABLE_PK_FK_PRODUCTNAME, PRODUCT_TABLE, PRODUCT_TABLE_PK_PRODUCTNAME);

        try {
            executeStatements(sql);
        } catch (SQLException e) {
            System.err.println("ERROR : Create tables if not exist failed");
            e.printStackTrace();
        }
    }

    protected void executeStatements(String statements) throws SQLException {
        Statement statement = this.connection.createStatement();
        String []sqlStatements = statements.split(";");
        for(String sqlStatement : sqlStatements) {
            if( !sqlStatement.trim().isEmpty() ) {
                sqlStatement = sqlStatement.trim();
                sqlStatement += ";";
                System.out.println(String.format("INFO : Execute Statement: %s", sqlStatement));
                statement.execute(sqlStatement);
            }
        }
    }
}
