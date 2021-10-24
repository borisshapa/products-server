package ru.akirakozov.sd.refactoring;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.akirakozov.sd.refactoring.data.ProductDataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.akirakozov.sd.refactoring.config.Config.DB_URL;

public class ProductsTest {
    @BeforeAll
    public static void createDbTable() {
        ProductDataAccess.executeUpdate(
                "CREATE TABLE IF NOT EXISTS PRODUCT" +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " NAME           TEXT    NOT NULL, " +
                        " PRICE          INT     NOT NULL)"
        );
        ProductDataAccess.executeUpdate("DELETE FROM PRODUCT");
    }
    
    @AfterEach
    public void clearDbTable() {
        ProductDataAccess.executeUpdate("DELETE FROM PRODUCT");
    }

    @AfterAll
    public static void deleteDbTable() {
        ProductDataAccess.executeUpdate("DROP TABLE PRODUCT");
    }
}
