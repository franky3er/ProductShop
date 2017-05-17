package vs.products.shop.delivery;

/**
 * Interface to deliver the amount of a product after payment
 */
public interface DeliveryOption {
    void deliver(String productName, String productAmount, String deliveryAddress);
}
