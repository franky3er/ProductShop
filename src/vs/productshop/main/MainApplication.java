package vs.productshop.main;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;

import org.apache.thrift.transport.TTransportException;
import vs.shopservice.ShopHandler;
import vs.shopservice.ShopService;

import java.io.File;
import java.io.FileNotFoundException;
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
        }
    }

    private static void loadConfig() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(PROJECT_CONFIG));
        port = Integer.parseInt(properties.getProperty(PRODUCTSHOP_LISTENING_PORT));
        sqLiteFileSource = properties.getProperty(PRODUCTSHOP_PRODUCTSQLITE_FILESOURCE);
        sqLiteDriver = properties.getProperty(PRODUCTSHOP_PRODUCTSQLITE_DRIVER);
    }

    private static void initialize() throws ClassNotFoundException, SQLException, TTransportException {
        shopHandler = new ShopHandler();
        processor = new ShopService.Processor(shopHandler);
        initializeSQLiteConnection();
        initializeServer();

    }

    private static void initializeSQLiteConnection() throws ClassNotFoundException, SQLException {
        Class.forName(sqLiteDriver);
        connection = DriverManager.getConnection(sqLiteFileSource);
    }

    private static void initializeServer() throws TTransportException {
        serverTransport = new TServerSocket(port);
        server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
    }

    private static void run() {
        server.serve();
    }

}