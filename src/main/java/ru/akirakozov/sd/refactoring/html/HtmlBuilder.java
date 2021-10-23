package ru.akirakozov.sd.refactoring.html;

import ru.akirakozov.sd.refactoring.model.Product;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlBuilder {
    public static String contentPage(String content) {
        return String.format("<html><body>\n%s\n</body></html>", content);
    }

    public static String contentWithHeader(String header, String content) {
        if (!content.isEmpty()) {
            content = "\n" + content;
        }
        return contentPage(String.format("<h1>%s</h1>%s", header, content));
    }

    public static String productList(List<Product> products) {
        return products.stream()
                .map(product -> String.format("%s\t%s</br>", product.getName(), product.getPrice()))
                .collect(Collectors.joining());
    }
}
