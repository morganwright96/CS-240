package Handlers.AuthTokenHandlers;

import DAO.DataAccessException;
import Handlers.JsonEncoder;
import Handlers.RequestHandler;
import Model.Person;
import Model.User;
import Service.Result.Result;
import Service.Services;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

public class AuthorizingRequestHandler extends RequestHandler {
    private Person person = null;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        Result result = new Result();
        String resData;
        try {
            if(exchange.getRequestMethod().toUpperCase().equals("GET")){
                // Get the request headers and check for Authorization
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    Services services = new Services(getConn());
                    if(services.isAuthorized(authToken)){
                        person = services.getLoggedInUser(authToken);
                        if(person == null){
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            result.setMessage("Error: There is no person with the given authToken");
                            resData = JsonEncoder.serialize(result, Result.class);
                            responseBodyWriter(exchange, resData);
                            getDb().closeConnection(false);
                        }
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        result.setMessage("Error: You are not authorized to preform this request");
                        resData = JsonEncoder.serialize(result, Result.class);
                        responseBodyWriter(exchange, resData);
                        getDb().closeConnection(false);
                    }
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    result.setMessage("Error: There was no Authorization token passed to the sever");
                    resData = JsonEncoder.serialize(result, Result.class);
                    responseBodyWriter(exchange, resData);
                    getDb().closeConnection(false);
                }
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                result.setMessage("Error: You have tried to make an illegal request");
                resData = JsonEncoder.serialize(result, Result.class);
                responseBodyWriter(exchange, resData);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            result.setMessage("Error: There was a problem trying to access the database");
            resData = JsonEncoder.serialize(result, Result.class);
            responseBodyWriter(exchange, resData);
            e.printStackTrace();
        }
    }

    public Person getPerson() {
        return person;
    }
}
