package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class GetProductsServletTest extends ServletTest {
    @Test
    public void addProductSuccessfully() throws IOException {
        new GetProductsServlet().doGet(request, response);
        compareWithResponse(
                """
                        <html><body>
                        </body></html>
                        """
        );
    }
}
