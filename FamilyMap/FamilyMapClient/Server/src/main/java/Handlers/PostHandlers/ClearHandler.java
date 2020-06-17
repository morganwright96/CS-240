package Handlers.PostHandlers;


import DAO.DataAccessException;
import Handlers.JsonEncoder;
import com.example.shared.Result.ClearResult;
import Service.Services;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import java.net.HttpURLConnection;

public class ClearHandler extends PostRequestHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            super.handle(exchange);
            Services services = new Services(getConn());
            ClearResult clearResult = services.clear();
            String respData = JsonEncoder.serialize(clearResult, ClearResult.class);
            if(clearResult.isSuccess()){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                responseBodyWriter(exchange, respData);
                getDb().closeConnection(true);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                responseBodyWriter(exchange, respData);
                getDb().closeConnection(false);
            }
        } catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        } catch (DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            e.printStackTrace();
        }
    }
}
