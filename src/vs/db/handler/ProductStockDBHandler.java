package vs.db.handler;

import vs.db.pojo.ProductStockRecord;

import java.sql.Connection;

/**
 * Created by franky3er on 10.05.17.
 */
public class ProductStockDBHandler {
    private Connection connection;

    public ProductStockDBHandler(Connection connection) {
        this.connection = connection;
    }

    public ProductStockRecord getProductStockRecord(String productName) {
        //TODO implement getProductStock
        return null;
    }

    public boolean reduceProducStockAmmount(String productName, String productAmount) {
        //TODO implement reduceStockAmount
        return true;
    }
}
