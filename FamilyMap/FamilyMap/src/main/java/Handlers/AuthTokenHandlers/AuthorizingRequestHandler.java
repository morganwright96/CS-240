package Handlers.AuthTokenHandlers;

import Handlers.RequestHandlers.RequestHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class AuthorizingRequestHandler extends RequestHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        try {
            Headers reqHeaders = exchange.getRequestHeaders();
            if (reqHeaders.containsKey("Authorization")) {
                String authToken = reqHeaders.getFirst("Authorization");
                // FIXME add code to check for a matching AuthToken
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_UNAUTHORIZED, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            e.printStackTrace();
        }

    }
}
