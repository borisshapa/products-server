package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.data.ProductDataAccess;
import ru.akirakozov.sd.refactoring.html.HtmlBuilder;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private static void writeOptionalProductWithHeader(String header, HttpServletResponse response, Optional<Product> product) throws IOException {
        response.getWriter().println(HtmlBuilder.contentWithHeader(
                header,
                product.isPresent() ? HtmlBuilder.product(product.get()) : ""
        ));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            writeOptionalProductWithHeader("Product with max price: ", response, ProductDataAccess.getProductWithMaxPrice());
        } else if ("min".equals(command)) {
            writeOptionalProductWithHeader("Product with min price: ", response, ProductDataAccess.getProductWithMinPrice());
        } else if ("sum".equals(command)) {
            response.getWriter().println(HtmlBuilder.contentPage("Summary price: \n" + ProductDataAccess.getPriceSum()));
        } else if ("count".equals(command)) {
            response.getWriter().println(HtmlBuilder.contentPage("Number of products: \n" + ProductDataAccess.getProductsCount()));
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
