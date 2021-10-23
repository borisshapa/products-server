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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            Optional<Product> productWithMaxPriceOptional = ProductDataAccess.getProductWithMaxPrice();
            response.getWriter().println(HtmlBuilder.contentWithHeader(
                    "Product with max price: ",
                    productWithMaxPriceOptional.isPresent() ? HtmlBuilder.productList(List.of(productWithMaxPriceOptional.get())) : ""
            ));
        } else if ("min".equals(command)) {
            Optional<Product> productWithMinPriceOptional = ProductDataAccess.getProductWithMinPrice();
            response.getWriter().println(HtmlBuilder.contentWithHeader(
                    "Product with min price: ",
                    productWithMinPriceOptional.isPresent() ? HtmlBuilder.productList(List.of(productWithMinPriceOptional.get())) : ""
            ));
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
