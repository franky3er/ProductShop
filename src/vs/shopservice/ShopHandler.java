package vs.shopservice;

import org.apache.thrift.TException;

/**
 * Created by franky3er on 09.05.17.
 */
public class ShopHandler implements ShopService.Iface {

    @Override
    public void ping() throws TException {

    }

    @Override
    public long fetchProductPrice(String productName, String productAmount) throws TException {

        return 0;
    }

    @Override
    public boolean buyProduct(String productName, String productAmount) throws TException {
        return false;
    }
}
