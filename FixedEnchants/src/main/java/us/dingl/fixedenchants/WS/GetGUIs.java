package us.dingl.fixedenchants.WS;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetGUIs extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        String html = """
                <html>
                    <head>
                        <title>GUIs</title>
                    </head>
                    <body>
                        <h1>GUIs</h1>
                        <p>This is a list of all active GUIs:</p>
                        <ul>
                        </ul>
                    </body>
                </html>
                """;

        resp.getWriter().println(html);
    }
}