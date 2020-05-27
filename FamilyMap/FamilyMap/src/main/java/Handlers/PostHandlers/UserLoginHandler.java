package Handlers.PostHandlers;

import DAO.DataAccessException;
import Handlers.JsonEncoder;
import Service.Request.LoginRequest;
import Service.Result.LoginResult;
import Service.Services;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class UserLoginHandler extends PostRequestHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        try {
            Services services = new Services();
            LoginRequest loginRequest = JsonEncoder.deserialize(reqData, LoginRequest.class);
            LoginResult loginResult = services.login(loginRequest, conn);
            String respData = JsonEncoder.serialize(loginResult, LoginResult.class);
            if(loginResult.isSuccess()){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                respBody.write(respData.getBytes());
                respBody.close();
                db.closeConnection(true);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                OutputStream respBody = exchange.getResponseBody();
                respBody.write(respData.getBytes());
                respBody.close();
                db.closeConnection(false);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            e.printStackTrace();
        }
    }
}
