package vs.productshop.main;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import org.apache.thrift.transport.TTransportException;
import vs.products.shop.db.handler.ProductStockDBHandler;
import vs.shopservice.ShopHandler;
import vs.shopservice.ShopService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by franky3er on 09.05.17.
 */


public class MainApplication {
    private final static String PROJECT_NAME = "ProductShop";
    private final static String PROJECT_CONFIG = System.getProperty("user.dir") + File.separator +
            "config" + File.separator + PROJECT_NAME + ".properties";

    private final static String PRODUCTSHOP_PRODUCTSQLITE_FILESOURCE = "ProductShop.ProductSQLiteHandler.FileSource";
    private final static String PRODUCTSHOP_PRODUCTSQLITE_DRIVER = "ProductShop.ProductSQLiteHandler.Driver";
    private final static String PRODUCTSHOP_LISTENING_PORT = "ProductShop.Listening.Port";


    private static int port;
    private static String sqLiteFileSource;
    private static String sqLiteDriver;
    private static ShopHandler shopHandler;
    private static ShopService.Processor processor;
    private static ProductStockDBHandler productStockDBHandler;
    private static Connection connection;
    private static TServerTransport serverTransport;
    private static TServer server;

    public static void main(String[] args) {
        try {
            loadConfig();
            initialize();
            run();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("ERROR : Failed to close DB Connection");
                    e.printStackTrace();
                }
            }
        }
    }

    private static void loadConfig() throws IOException {
        System.out.println("INFO : Loading config...");
        Properties properties = new Properties();
        properties.load(new FileReader(PROJECT_CONFIG));
        port = Integer.parseInt(properties.getProperty(PRODUCTSHOP_LISTENING_PORT));
        sqLiteFileSource = properties.getProperty(PRODUCTSHOP_PRODUCTSQLITE_FILESOURCE);
        sqLiteDriver = properties.getProperty(PRODUCTSHOP_PRODUCTSQLITE_DRIVER);
    }

    private static void initialize() throws ClassNotFoundException, SQLException, TTransportException {
        System.out.println("INFO : Initializing");
        initializeSQLiteConnection();
        System.out.println("INFO : Initializing ShopHandler");
        shopHandler = new ShopHandler(new ProductStockDBHandler(connection));
        System.out.println("INFO : Initializing Processor");
        processor = new ShopService.Processor(shopHandler);
    }

    private static void initializeSQLiteConnection() throws ClassNotFoundException, SQLException {
        System.out.println("INFO : Initializing SQLite Connection");
        Class.forName(sqLiteDriver);
        connection = DriverManager.getConnection("jdbc:sqlite:" + sqLiteFileSource);
    }

    private static void run() {
        System.out.println("INFO : run application");
        try {
            System.out.println("INFO : Starting the Server on Port: " + port);
            serverTransport = new TServerSocket(port);
            server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            server.serve();
        } catch (TTransportException e) {
            System.err.println("ERROR : run failed");
            e.printStackTrace();
        }
    }

}