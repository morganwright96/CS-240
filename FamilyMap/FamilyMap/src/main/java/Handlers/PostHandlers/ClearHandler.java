package Handlers.PostHandlers;


import DAO.DataAccessException;
import Handlers.JsonEncoder;
import Service.Result.ClearResult;
import Service.Services;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

import java.net.HttpURLConnection;

public class ClearHandler extends PostRequestHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            super.handle(exchange);

            Services services = new Services();
            ClearResult clearResult = services.clear(conn);
            if(clearResult.isSuccess()){
                String respData = JsonEncoder.serialize(clearResult, ClearResult.class);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                respBody.write(respData.getBytes());
                respBody.close();
                db.closeConnection(true);
            }
            else {
                db.closeConnection(false);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            exchange.getResponseBody().close();
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
