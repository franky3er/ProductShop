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
    public void ping() throws TException {
    }

    @Override
    public long fetchProductPrice(String productName, String productAmount) throws TException {
        ProductStockRecord productStockRecord = productStockDBHandler.getProductStockRecord(productName);
        if (productStockRecord == null) {
            System.err.println(String.format("WARNING : %s does not exist in db"));
            return -1;
        }
        try {
            return calculateTotalPrice(productStockRecord.getProductPricePerUnit(), productAmount);
        } catch (NumberFormatException e) {
            System.err.println(String.format("WARNING : incorrect amount format: %s", productAmount));
            e.printStackTrace();
            return -1;
        }

    }

    @Override
    public boolean buyProduct(String productName, String productAmount) throws TException {
        return productStockDBHandler.reduceProductStockAmount(productName, productAmount);
    }

    private long calculateTotalPrice(long unitPrice, String amount) throws NumberFormatException {
        if (amount.contains(".")) {
            return calculateTotalPrice(unitPrice, Double.parseDouble(amount));
        } else {
            return calculateTotalPrice(unitPrice, Integer.parseInt(amount));
        }
    }

    private long calculateTotalPrice(long unitPrice, int amount) {
        return unitPrice * amount;
    }

    private long calculateTotalPrice(long unitPrice, double amount) {
        return (long) (unitPrice * amount);
    }
}
