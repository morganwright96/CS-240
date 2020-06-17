package Handlers.PostHandlers;

import DAO.DataAccessException;
import Handlers.RequestHandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;


public class PostRequestHandler extends RequestHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            super.handle(exchange);
            if(!exchange.getRequestMethod().toUpperCase().equals("POST")){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                getDb().closeConnection(false);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            e.printStackTrace();
        }
    }
}
