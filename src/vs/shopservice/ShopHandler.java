package vs.shopservice;

import org.apache.thrift.TException;
import vs.db.handler.ProductStockDBHandler;
import vs.db.pojo.ProductStockRecord;

/**
 * Created by franky3er on 09.05.17.
 */
public class ShopHandler implements ShopService.Iface {

    private ProductStockDBHandler productStockDBHandler;

    public ShopHandler(ProductStockDBHandler productStockDBHandler) {
        this.productStockDBHandler = productStockDBHandler;
    }

    @Override
    public void ping() throws TException {}

    @Override
    public long fetchProductPrice(String productName, String productAmount) throws TException {
        ProductStockRecord productStockRecord = productStockDBHandler.getProductStockRecord(productName);
        //TODO handle

        return 0;
    }

    @Override
    public boolean buyProduct(String productName, String productAmount) throws TException {
        boolean successful = productStockDBHandler.reduceProducStockAmmount(productName, productAmount);
        return successful;
    }
}
