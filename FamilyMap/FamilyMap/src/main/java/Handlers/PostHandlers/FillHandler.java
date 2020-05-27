package Handlers.PostHandlers;

import DAO.DataAccessException;
import Handlers.JsonEncoder;
import Service.Result.FillResult;
import Service.Services;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public class FillHandler extends PostRequestHandler{
    private String respBody = null;
    private String message = null;
    Services services;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        try {
            services = new Services();
            URI uri = exchange.getRequestURI();
            String[] params = uri.getPath().split("/");
            if(params.length > 2 && params.length < 5){
                String username = params[2];
                FillResult fillResult;
                if(params.length == 4){
                    // Fill with a custom number of generations
                    fillResult = services.fill(username, Integer.parseInt(params[3]), conn);
                }
                else {
                    // 4 is the default number of generations to fill
                    fillResult = services.fill(username, 4, conn);
                }
                findSuccess(exchange, fillResult);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                message = "Sorry, there was a problem tyring to fill with the provided information";
                createResponse(message, false);
                responseBodyWriter(exchange, respBody);
                db.closeConnection(false);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            message = "Sorry, we encountered an error trying to fill the database";
            createResponse(message, false);
            responseBodyWriter(exchange, respBody);
            e.printStackTrace();
        }
    }

    private void findSuccess(HttpExchange exchange, FillResult fillResult) throws IOException, DataAccessException {
        if(fillResult.isSuccess()){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            db.closeConnection(true);
        }
        else{
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            db.closeConnection(false);
        }
        respBody = JsonEncoder.serialize(fillResult, FillResult.class);
        responseBodyWriter(exchange, respBody);
    }

    public void createResponse(String message, boolean success){
        FillResult fillResult = new FillResult();
        fillResult.setMessage(message);
        fillResult.setSuccess(success);
        respBody = JsonEncoder.serialize(fillResult, FillResult.class);
    }
}
