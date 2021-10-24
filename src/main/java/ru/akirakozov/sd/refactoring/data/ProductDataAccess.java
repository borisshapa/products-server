package ru.akirakozov.sd.refactoring.data;

import ru.akirakozov.sd.refactoring.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDataAccess {
    public static final String DB_URL = "jdbc:sqlite:test.db";
    private static final String CONNECTION_ERROR_PREFIX = "Сould not establish a connection to the database: ";

    private static boolean executeUpdate(String sqlQuery) {
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            PreparedStatement stmt = c.prepareStatement(sqlQuery);
            boolean success = stmt.executeUpdate() > 0;
            stmt.close();
            return success;
        } catch (SQLException e) {
            throw new RuntimeException(CONNECTION_ERROR_PREFIX + e.getMessage());
        }
    }

    private static List<Product> getListOfProducts(String sql) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            List<Product> result = new ArrayList<>();

            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                long price = resultSet.getLong("price");
                result.add(new Product(name, price));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(CONNECTION_ERROR_PREFIX + e.getMessage());
        }
    }

    private static long getAggregateValue(String sql) {
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            Statement stmt = c.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(CONNECTION_ERROR_PREFIX + e.getMessage());
        }
    }

    public static boolean addProduct(Product product) {
        return executeUpdate("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"" + product.name() + "\", " + product.price() + ")");
    }

    public static List<Product> getProducts() {
        return getListOfProducts("SELECT * FROM PRODUCT");
    }

    private static Optional<Product> getOptionalProduct(String sql) {
        List<Product> productsList = getListOfProducts(sql);
        return productsList.isEmpty() ? Optional.empty() : Optional.of(productsList.get(0));
    }

    public static Optional<Product> getProductWithMaxPrice() {
        return getOptionalProduct("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
    }

    public static Optional<Product> getProductWithMinPrice() {
        return getOptionalProduct("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
    }

    public static long getPriceSum() {
        return getAggregateValue("SELECT SUM(price) FROM PRODUCT");
    }

    public static long getProductsCount() {
        return getAggregateValue("SELECT COUNT(*) FROM PRODUCT");
    }
}
