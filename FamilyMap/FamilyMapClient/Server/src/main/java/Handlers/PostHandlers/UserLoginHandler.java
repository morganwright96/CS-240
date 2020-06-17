package Handlers.PostHandlers;

import DAO.DataAccessException;
import Handlers.JsonEncoder;
import com.example.shared.Request.LoginRequest;
import com.example.shared.Result.LoginResult;
import Service.Services;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.HttpURLConnection;

public class UserLoginHandler extends PostRequestHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        try {
            Services services = new Services(getConn());
            //System.out.println("In the Login handler");
            LoginRequest loginRequest = JsonEncoder.deserialize(getReqData(), LoginRequest.class);
            LoginResult loginResult = services.login(loginRequest);
            String respData = JsonEncoder.serialize(loginResult, LoginResult.class);
            if(loginResult.isSuccess()){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                responseBodyWriter(exchange, respData);
                getDb().closeConnection(true);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                responseBodyWriter(exchange, respData);
                getDb().closeConnection(false);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            e.printStackTrace();
        }
    }
}
