package vs.products.shop.delivery.kalmund;

import vs.products.shop.delivery.DeliveryOption;

/**
 * KalmundDelivery is a simple testing delivery
 */
public class KalmundDelivery implements DeliveryOption {

    @Override
    public void deliver(String productName, String productAmount) {
        //TODO send the delivery to SilviDelivery application via RPC
    }
}
