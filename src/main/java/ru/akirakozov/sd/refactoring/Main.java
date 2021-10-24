package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.data.ProductDataAccess;
import ru.akirakozov.sd.refactoring.servlet.AbstractServlet;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

import static ru.akirakozov.sd.refactoring.config.Config.DB_URL;
import static ru.akirakozov.sd.refactoring.config.Config.PORT;

/**
 * @author akirakozov
 */
public class Main {
    private static final Map<String, AbstractServlet> pathToServlet = Map.of(
        "/add-product", new AddProductServlet(),
        "/get-products", new GetProductsServlet(),
        "/query", new QueryServlet()
    );

    public static Server startServer() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        pathToServlet.forEach((String path, AbstractServlet servlet) -> context.addServlet(new ServletHolder(servlet), path));
        server.start();
        return server;
    }

    public static void main(String[] args) throws Exception {
        ProductDataAccess.executeUpdate(
                "CREATE TABLE IF NOT EXISTS PRODUCT" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " NAME           TEXT    NOT NULL, " +
                        " PRICE          INT     NOT NULL)"
        );

        Server server = startServer();
        server.join();
    }
}
