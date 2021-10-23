package ru.akirakozov.sd.refactoring.servlet;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AddProductServletTest extends ServletTest {
    @Test
    public void addProductSuccessfully() throws IOException {
        when(request.getParameter("name")).thenReturn("phone");
        when(request.getParameter("price")).thenReturn("300");

        new AddProductServlet().doGet(request, response);
        verify(request, atLeast(1)).getParameter("name");
        verify(request, atLeast(1)).getParameter("price");
        compareWithResponse("OK\n");
    }

    @Test
    public void addProductInvalidPrice() {
        when(request.getParameter("name")).thenReturn("phone");
        when(request.getParameter("price")).thenReturn("Three hundred");

        assertThrows(NumberFormatException.class, () -> new AddProductServlet().doGet(request, response));
        verify(request, atLeast(1)).getParameter("name");
        verify(request, atLeast(1)).getParameter("price");
    }

    @Test
    public void addProductMissingPrice() {
        when(request.getParameter("name")).thenReturn("phone");

        assertThrows(NumberFormatException.class, () -> new AddProductServlet().doGet(request, response));
        verify(request, atLeast(1)).getParameter("name");
        verify(request, atLeast(1)).getParameter("price");
    }
}
