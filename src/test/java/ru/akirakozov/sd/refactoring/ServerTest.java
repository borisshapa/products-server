package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.akirakozov.sd.refactoring.config.Config.PORT;

public class ServerTest extends ProductsTest {
    private final static String ADD_PRODUCT_METHOD = "add-product";
    private final static String GET_PRODUCTS_METHOD = "get-products";
    private final static String QUERY_METHOD = "query";

    private final static Server server = new Server(PORT);
    private final static HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    public static void createServer() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet()), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet()), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet()), "/query");

        server.start();
    }

    private static String urlEncodeUTF8(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private static String sendRequest(String method, Map<String, String> parameters) throws IOException, InterruptedException {
        String parametersUrl = parameters.entrySet().stream()
                .map(p -> urlEncodeUTF8(p.getKey()) + "=" + urlEncodeUTF8(p.getValue()))
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");
        if (!parametersUrl.isEmpty()) {
            parametersUrl = "?" + parametersUrl;
        }

        HttpRequest request = HttpRequest.newBuilder(
                URI.create(String.format("http://localhost:%d/%s%s", PORT, method, parametersUrl))
        ).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private static String sendRequest(String method) throws IOException, InterruptedException {
        return sendRequest(method, Map.of());
    }

    private static Map<String, String> buildProductMap(String name, String price) {
        return Map.of("name", name, "price", price);
    }

    private static Map<String, String> buildCommandMap(String command) {
        return Map.of("command", command);
    }

    @Test
    public void addProduct() throws IOException, InterruptedException {
        String addResponse = sendRequest(ADD_PRODUCT_METHOD, buildProductMap("phone", "300"));
        assertEquals("OK\n", addResponse);

        String getResponse = sendRequest(GET_PRODUCTS_METHOD);
        assertEquals(
                """
                        <html><body>
                        phone\t300</br>
                        </body></html>
                        """,
                getResponse
        );
    }

    @Test
    public void addSeveralProducts() throws IOException, InterruptedException {
        int phoneIndexFrom = 1;
        int phoneIndexTo = 10;

        for (int i = phoneIndexFrom; i <= phoneIndexTo; i++) {
            String addResponse = sendRequest(ADD_PRODUCT_METHOD, buildProductMap("phone" + i, Integer.toString(300 * i)));
            assertEquals("OK\n", addResponse);
        }

        String getResponse = sendRequest(GET_PRODUCTS_METHOD);
        StringBuilder phoneList = new StringBuilder();
        for (int i = phoneIndexFrom; i <= phoneIndexTo; i++) {
            phoneList.append("phone").append(i).append("\t").append(300 * i).append("</br>\n");
        }

        assertEquals("<html><body>\n" + phoneList + "</body></html>\n", getResponse);
    }

    @Test
    public void max() throws IOException, InterruptedException {
        int phoneIndexFrom = 1;
        int phoneIndexTo = 10;

        for (int i = phoneIndexFrom; i <= phoneIndexTo; i++) {
            String addResponse = sendRequest(ADD_PRODUCT_METHOD, buildProductMap("phone" + i, Integer.toString(300 * i)));
            assertEquals("OK\n", addResponse);
        }

        String maxResponse = sendRequest(QUERY_METHOD, buildCommandMap("max"));
        assertEquals(
                """
                        <html><body>
                        <h1>Product with max price: </h1>
                        phone10\t3000</br>
                        </body></html>
                        """,
                maxResponse
        );
    }

    @Test
    public void maxFromEmptyList() throws IOException, InterruptedException {
        String maxResponse = sendRequest(QUERY_METHOD, buildCommandMap("max"));
        assertEquals(
                """
                        <html><body>
                        <h1>Product with max price: </h1>
                        </body></html>
                        """,
                maxResponse
        );
    }

    @Test
    public void min() throws IOException, InterruptedException {
        int phoneIndexFrom = 1;
        int phoneIndexTo = 10;

        for (int i = phoneIndexFrom; i <= phoneIndexTo; i++) {
            String addResponse = sendRequest(ADD_PRODUCT_METHOD, buildProductMap("phone" + i, Integer.toString(300 * i)));
            assertEquals("OK\n", addResponse);
        }

        String minResponse = sendRequest(QUERY_METHOD, buildCommandMap("min"));
        assertEquals(
                """
                        <html><body>
                        <h1>Product with min price: </h1>
                        phone1\t300</br>
                        </body></html>
                        """,
                minResponse
        );
    }

    @Test
    public void minFromEmptyList() throws IOException, InterruptedException {
        String minResponse = sendRequest(QUERY_METHOD, buildCommandMap("min"));
        assertEquals(
                """
                        <html><body>
                        <h1>Product with min price: </h1>
                        </body></html>
                        """,
                minResponse
        );
    }

    @Test
    public void sum() throws IOException, InterruptedException {
        int phoneIndexFrom = 1;
        int phoneIndexTo = 10;

        for (int i = phoneIndexFrom; i <= phoneIndexTo; i++) {
            String addResponse = sendRequest(ADD_PRODUCT_METHOD, buildProductMap("phone" + i, Integer.toString(300 * i)));
            assertEquals("OK\n", addResponse);
        }

        String sumResponse = sendRequest(QUERY_METHOD, buildCommandMap("sum"));
        assertEquals(
                """
                        <html><body>
                        Summary price:\s
                        16500
                        </body></html>
                        """,
                sumResponse
        );
    }

    @Test
    public void sumFromEmptyList() throws IOException, InterruptedException {
        String sumResponse = sendRequest(QUERY_METHOD, buildCommandMap("sum"));
        assertEquals(
                """
                        <html><body>
                        Summary price:\s
                        0
                        </body></html>
                        """,
                sumResponse
        );
    }

    @Test
    public void count() throws IOException, InterruptedException {
        int phoneIndexFrom = 1;
        int phoneIndexTo = 10;

        for (int i = phoneIndexFrom; i <= phoneIndexTo; i++) {
            String addResponse = sendRequest(ADD_PRODUCT_METHOD, buildProductMap("phone" + i, Integer.toString(300 * i)));
            assertEquals("OK\n", addResponse);
        }

        String countResponse = sendRequest(QUERY_METHOD, buildCommandMap("count"));
        assertEquals(
                """
                        <html><body>
                        Number of products:\s
                        10
                        </body></html>
                        """,
                countResponse
        );
    }

    @Test
    public void countFromEmptyList() throws IOException, InterruptedException {
        String countResponse = sendRequest(QUERY_METHOD, buildCommandMap("count"));
        assertEquals(
                """
                        <html><body>
                        Number of products:\s
                        0
                        </body></html>
                        """,
                countResponse
        );
    }

    @Test
    public void unknownCommand() throws IOException, InterruptedException {
        String response = sendRequest(QUERY_METHOD, buildCommandMap("unknown command"));
        assertEquals("Unknown command: unknown command\n", response);
    }
}
