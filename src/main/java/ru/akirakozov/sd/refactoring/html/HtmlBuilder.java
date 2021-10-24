package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlBuilder {
    private static String preprocessContent(String content) {
        return content.isEmpty() ? "" : "\n" + content;
    }

    public static String contentPage(String content) {
        return String.format("<html><body>%s\n</body></html>", preprocessContent(content));
    }

    public static String contentWithHeader(String header, String content) {
        return contentPage(String.format("<h1>%s</h1>%s", header, preprocessContent(content)));
    }

    public static String product(Product product) {
        return String.format("%s\t%s</br>", product.name(), product.price());
    }

    public static String productList(List<Product> products) {
        return products.stream()
                .map(HtmlBuilder::product)
                .collect(Collectors.joining("\n"));
    }
}
