package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.data.ProductDataAccess;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends AbstractServlet {
    private static String writeOptionalProductWithHeader(String header, Optional<Product> product) {
        return HtmlBuilder.contentWithHeader(
                header,
                product.isPresent() ? HtmlBuilder.product(product.get()) : ""
        );
    }

    @Override
    protected void doGet(HttpServletRequest request, PrintWriter printWriter) {
        String command = request.getParameter("command");
        String response;
        switch (command) {
            case "max" -> response = writeOptionalProductWithHeader("Product with max price: ", ProductDataAccess.getProductWithMaxPrice());
            case "min" -> response = writeOptionalProductWithHeader("Product with min price: ", ProductDataAccess.getProductWithMinPrice());
            case "sum" -> response = HtmlBuilder.contentPage("Summary price: \n" + ProductDataAccess.getPriceSum());
            case "count" -> response = HtmlBuilder.contentPage("Number of products: \n" + ProductDataAccess.getProductsCount());
            default -> response = "Unknown command: " + command;
        }
        printWriter.println(response);
    }
}
