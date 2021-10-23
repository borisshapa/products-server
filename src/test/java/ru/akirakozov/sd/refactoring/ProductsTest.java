package ru.akirakozov.sd.refactoring;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductsTest {
    private final static String DB_URL = "jdbc:sqlite:test.db";

    protected static void executeSqlQuery(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_URL)) {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @BeforeAll
    public static void createDbTable() throws SQLException {
        executeSqlQuery(
                "CREATE TABLE IF NOT EXISTS PRODUCT" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " NAME           TEXT    NOT NULL, " +
                        " PRICE          INT     NOT NULL)"
        );
    }
    
    @AfterEach
    public void clearDbTable() throws SQLException {
        executeSqlQuery("DELETE FROM PRODUCT");
    }

    @AfterAll
    public static void deleteDbTable() throws SQLException {
        executeSqlQuery("DROP TABLE PRODUCT");
    }
}
