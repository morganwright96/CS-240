package Handlers.PostHandlers;

import DAO.DataAccessException;
import Handlers.JsonEncoder;
import com.example.shared.Request.LoginRequest;
import com.example.shared.Request.RegisterRequest;
import com.example.shared.Result.LoginResult;
import com.example.shared.Result.RegisterResult;
import Service.Services;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class UserRegisterHandler extends PostRequestHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        try {
            Services services = new Services(getConn());
            RegisterRequest registerRequest = JsonEncoder.deserialize(getReqData(), RegisterRequest.class);
            RegisterResult registerResult = services.register(registerRequest);
            String respData = JsonEncoder.serialize(registerResult, RegisterResult.class);
            if(registerResult.isSuccess()){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                responseBodyWriter(exchange, respData);
                getDb().closeConnection(true);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                responseBodyWriter(exchange, respData);
                getDb().closeConnection(false);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
