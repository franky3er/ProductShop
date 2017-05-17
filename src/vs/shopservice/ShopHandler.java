package vs.shopservice;

import org.apache.thrift.TException;
import vs.products.shop.db.handler.ProductStockDBHandler;
import vs.products.shop.db.pojo.ProductStockRecord;
import vs.products.shop.delivery.DeliveryOption;

/**
 * Created by franky3er on 09.05.17.
 */
public class ShopHandler implements ShopService.Iface {

    private ProductStockDBHandler productStockDBHandler;
    private DeliveryOption deliveryOption;

    public ShopHandler(ProductStockDBHandler productStockDBHandler, DeliveryOption deliveryOption) {
        this.productStockDBHandler = productStockDBHandler;
        this.deliveryOption = deliveryOption;
    }

    @Override
    public void ping() throws TException {
    }

    @Override
    public long fetchProductPrice(String productName, String productAmount) throws TException {
        System.out.println(String.format("INFO : Fetch product price : ProductName: %s, Amount: %s", productName, productAmount));
        ProductStockRecord productStockRecord = productStockDBHandler.getProductStockRecord(productName);
        if (productStockRecord == null) {
            System.err.println(String.format("WARNING : %s does not exist in db", productName));
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
    public boolean buyProduct(String productName, String productAmount, String deliveryAddress) throws TException {
        System.out.println(String.format("INFO : Buy Product : ProductName: %s, Amount: %s", productName, productAmount));
        boolean buySuccessful = productStockDBHandler.reduceProductStockAmount(productName, productAmount);
        if(!buySuccessful) {
            return false;
        }
        deliveryOption.deliver(productName, productAmount, deliveryAddress);
        return true;
    }

    private long calculateTotalPrice(long unitPrice, String amount) throws NumberFormatException {
        System.out.println(String.format("INFO : Calculate Price : UnitPrice: %d, Amount: %s", unitPrice, amount));
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
