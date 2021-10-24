package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractServlet extends HttpServlet {

    abstract protected void doGet(HttpServletRequest request, PrintWriter printWriter);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp.getWriter());
        resp.setContentType("text/html");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
