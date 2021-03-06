package vs.products.shop.db.handler;

import vs.products.shop.db.pojo.ProductStockRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by franky3er on 10.05.17.
 */
public class ProductStockDBHandler {
    private Connection connection;

    public ProductStockDBHandler(Connection connection) {
        this.connection = connection;
    }

    public ProductStockRecord getProductStockRecord(String productName) {
        ProductStockRecord productStockRecord = null;
        String sqlStatement = "SELECT * FROM ProductStock WHERE Name = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, productName);
            System.out.println(String.format("INFO : ExecuteQuery: %s", preparedStatement));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                productStockRecord = new ProductStockRecord();
                productStockRecord.setProductName(productName);
                productStockRecord.setProductUnit(resultSet.getString("Unit"));
                productStockRecord.setProductPricePerUnit(resultSet.getInt("PricePerUnit"));
                productStockRecord.setProductAmmount(resultSet.getString("Amount"));
            }
        } catch (SQLException e) {
            System.err.println(String.format("WARNING : ExecuteQuery failed"));
            e.printStackTrace();
            return null;
        }
        return productStockRecord;
    }

    public boolean reduceProductStockAmount(String productName, String productAmount) {
        ProductStockRecord productStockRecord = getProductStockRecord(productName);
        if (productStockRecord == null) {
            System.err.println(String.format("WARNING : Product %s not found", productName));
            return false;
        }
        String newProductStockAmount = null;
        String sqlStatement = "UPDATE ProductStock SET Amount = ? WHERE Name = ?;";
        try {
            newProductStockAmount = calculateNewProductStockAmount(productAmount, productStockRecord.getProductAmmount());
            if (newProductStockAmount == null) return false;
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, newProductStockAmount);
            preparedStatement.setString(2, productName);
            System.out.println(String.format("INFO : Execute Update: %s", preparedStatement));
            preparedStatement.executeUpdate();
            return true;
        } catch (NumberFormatException e) {
            System.err.println("WARNING : Incorrect Amount Format");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            String failedSqlStatement = String.format("UPDATE ProductStock SET Amount = '%s' WHERE Name = '%s';",
                    newProductStockAmount, productName);
            System.err.println(String.format("ERROR : SQL-Statement failed %s", failedSqlStatement));
            e.printStackTrace();
            return false;
        }
    }

    private String calculateNewProductStockAmount(String productAmount, String productStockAmount) throws NumberFormatException {
        System.out.println(String.format("INFO : Calculate new ProductStuckAmount : ProductAmount: %s, ProductStockAmount: %s", productAmount, productStockAmount));
        if (productAmount.contains(".") && productStockAmount.contains(".")) { // is Double
            return calculateNewProductStockAmount(Double.parseDouble(productAmount), Double.parseDouble(productStockAmount));
        }
        return calculateNewProductStockAmount(Integer.parseInt(productAmount), Integer.parseInt(productStockAmount));
    }

    private String calculateNewProductStockAmount(double productAmount, double productStockAmount) {
        if (productStockAmount < productAmount) {
            return null;
        }
        return Double.toString(productStockAmount - productAmount);
    }

    private String calculateNewProductStockAmount(int productAmount, int productStockAmount) {
        if (productStockAmount < productAmount) {
            return null;
        }
        return Integer.toString(productStockAmount - productAmount);
    }
}
