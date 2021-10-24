package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.data.ProductDataAccess;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends AbstractServlet {

    @Override
    protected void doGet(HttpServletRequest request, PrintWriter printWriter) {
        List<Product> products = ProductDataAccess.getProducts();
        printWriter.println(HtmlBuilder.contentPage(HtmlBuilder.productList(products)));
    }
}
