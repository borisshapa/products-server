package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class QueryServletTest extends ServletTest {
    private void testCommand(String command, String expectedResponse) throws IOException {
        when(request.getParameter("command")).thenReturn(command);
        new QueryServlet().doGet(request, response);
        verify(request, atLeast(1)).getParameter("command");
        compareWithResponse(expectedResponse);
    }

    @Test
    public void max() throws IOException {
        testCommand(
                "max",
                """
                        <html><body>
                        <h1>Product with max price: </h1>
                        </body></html>
                        """
        );
    }

    @Test
    public void min() throws IOException {
        testCommand(
                "min",
                """
                        <html><body>
                        <h1>Product with min price: </h1>
                        </body></html>
                        """
        );
    }

    @Test
    public void sum() throws IOException {
        testCommand(
                "sum",
                """
                        <html><body>
                        Summary price:\s
                        0
                        </body></html>
                        """
        );
    }

    @Test
    public void count() throws IOException {
        testCommand(
                "count",
                """
                        <html><body>
                        Number of products:\s
                        0
                        </body></html>
                        """
        );
    }
    
    @Test
    public void unknownCommand() throws IOException {
        testCommand("avg", "Unknown command: avg\n");
    }
}
