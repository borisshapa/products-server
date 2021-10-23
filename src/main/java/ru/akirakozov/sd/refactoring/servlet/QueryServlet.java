package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.data.ProductDataAccess;
import ru.akirakozov.sd.refactoring.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with max price: </h1>");
            Optional<Product> productWithMaxPriceOptional = ProductDataAccess.getProductWithMaxPrice();
            if (productWithMaxPriceOptional.isPresent()) {
                Product productWithMaxPrice = productWithMaxPriceOptional.get();
                response.getWriter().println(productWithMaxPrice.getName() + "\t" + productWithMaxPrice.getPrice() + "</br>");
            }
            response.getWriter().println("</body></html>");
        } else if ("min".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with min price: </h1>");
            Optional<Product> productWithMinPriceOptional = ProductDataAccess.getProductWithMinPrice();
            if (productWithMinPriceOptional.isPresent()) {
                Product productWithMinPrice = productWithMinPriceOptional.get();
                response.getWriter().println(productWithMinPrice.getName() + "\t" + productWithMinPrice.getPrice() + "</br>");
            }
            response.getWriter().println("</body></html>");
        } else if ("sum".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("Summary price: ");
            response.getWriter().println(ProductDataAccess.getPriceSum());
            response.getWriter().println("</body></html>");
        } else if ("count".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("Number of products: ");
            response.getWriter().println(ProductDataAccess.getProductsCount());
            response.getWriter().println("</body></html>");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
