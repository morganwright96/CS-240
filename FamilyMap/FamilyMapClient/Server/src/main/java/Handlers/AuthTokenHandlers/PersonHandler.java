package Handlers.AuthTokenHandlers;

import DAO.DataAccessException;
import Handlers.JsonEncoder;
import com.example.shared.Result.PeopleResult;
import com.example.shared.Result.PersonResult;
import com.example.shared.Result.Result;
import Service.Services;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;


public class PersonHandler extends AuthorizingRequestHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        try{
            Services services = new Services(getConn());
            URI uri = exchange.getRequestURI();
            String[] params = uri.getPath().split("/");
            String resData;
            if(params.length == 2){
                PeopleResult peopleResult = services.people(getPerson().getUserName());
                resData = JsonEncoder.serialize(peopleResult, PeopleResult.class);
                if(peopleResult.isSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    getDb().closeConnection(true);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    getDb().closeConnection(false);
                }
                responseBodyWriter(exchange, resData);
            }
            else if(params.length == 3){
                String personID = params[2];
                PersonResult personResult = services.person(personID, getPerson());
                resData = JsonEncoder.serialize(personResult, PersonResult.class);
                if(personResult.isSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    getDb().closeConnection(true);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    getDb().closeConnection(false);
                }
                responseBodyWriter(exchange, resData);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                getDb().closeConnection(false);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
