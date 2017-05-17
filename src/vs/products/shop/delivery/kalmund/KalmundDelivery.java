package vs.products.shop.delivery.kalmund;

import vs.products.shop.delivery.DeliveryOption;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * KalmundDelivery is a simple testing delivery
 */
public class KalmundDelivery implements DeliveryOption {
    String deliveryDirectory;
    
    public KalmundDelivery(String deliveryDirectory) {
        this.deliveryDirectory = deliveryDirectory;
    }
    
    @Override
    public void deliver(String productName, String productAmount, String deliveryAddress) {
        String absolutDeliveryFilePath = createDeliveryAbsolutFilePath();
        String deliveryFileContent = createDeliveryFileContent(productName, productAmount, deliveryAddress);
        writeDeliveryFile(absolutDeliveryFilePath, deliveryFileContent);
    }

    private String createDeliveryFileContent(String productName, String productAmount, String deliveryAddress) {
        String deliveryFileContentFormat = "DeliveryAddress: %s\n" +
                "Product: %s\n" +
                "Amount: %s";
        String deliveryFileContent = String.format(
                deliveryFileContentFormat,
                deliveryAddress,
                productName,
                productAmount);
        return deliveryFileContent;
    }

    private String createDeliveryAbsolutFilePath() {
        String fileName = Long.toString(System.currentTimeMillis()) + ".delivery";
        return this.deliveryDirectory + File.separator + fileName;
    }

    private void writeDeliveryFile(String absolutDeliveryFilePath, String deliveryFileContent) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(absolutDeliveryFilePath, "UTF-8");
            writer.println(deliveryFileContent);
        } catch (FileNotFoundException e) {
            System.err.println(String.format("ERROR : Can't write delivery: %s", absolutDeliveryFilePath));
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.err.println(String.format("ERROR : Can't write delivery: %s", absolutDeliveryFilePath));
            e.printStackTrace();
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }
}
