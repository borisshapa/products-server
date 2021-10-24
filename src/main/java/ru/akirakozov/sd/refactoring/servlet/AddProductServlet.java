package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.data.ProductDataAccess;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class AddProductServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest request, PrintWriter printWriter) {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        ProductDataAccess.addProduct(new Product(name, price));
        printWriter.println("OK");
    }
}
