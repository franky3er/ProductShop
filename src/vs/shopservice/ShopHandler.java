package vs.shopservice;

import org.apache.thrift.TException;

import java.sql.Connection;

/**
 * Created by franky3er on 09.05.17.
 */
public class ShopHandler implements ShopService.Iface {

    private Connection connection;

    public ShopHandler(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void ping() throws TException {
    }

    @Override
    public long fetchProductPrice(String productName, String productAmount) throws TException {
        //TODO get product ammount of product..

        return 0;
    }

    @Override
    public boolean buyProduct(String productName, String productAmount) throws TException {
        return false;
    }
}
