package vs.products.shop.db.pojo;

/**
 * Created by franky3er on 10.05.17.
 */
public class ProductStockRecord {
    private String productName;
    private String productUnit;
    private long productPricePerUnit;
    private String productAmmount;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public long getProductPricePerUnit() {
        return productPricePerUnit;
    }

    public void setProductPricePerUnit(long productPricePerUnit) {
        this.productPricePerUnit = productPricePerUnit;
    }

    public String getProductAmmount() {
        return productAmmount;
    }

    public void setProductAmmount(String productAmmount) {
        this.productAmmount = productAmmount;
    }
}
