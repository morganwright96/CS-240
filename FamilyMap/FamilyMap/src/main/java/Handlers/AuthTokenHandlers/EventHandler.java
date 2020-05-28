package Handlers.AuthTokenHandlers;

import DAO.DataAccessException;
import Handlers.JsonEncoder;
import Service.Result.AllEventsResult;
import Service.Result.EventResult;
import Service.Result.PeopleResult;
import Service.Result.PersonResult;
import Service.Services;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

public class EventHandler extends AuthorizingRequestHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        try{
            Services services = new Services(getConn());
            URI uri = exchange.getRequestURI();
            String[] params = uri.getPath().split("/");
            String resData;
            if(params.length == 2){
                AllEventsResult allEventsResult = services.allEvents(getPerson().getUserName());
                resData = JsonEncoder.serialize(allEventsResult, AllEventsResult.class);
                if(allEventsResult.isSuccess()){
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
                String eventId = params[2];
                EventResult eventResult = services.event(eventId, getPerson());
                resData = JsonEncoder.serialize(eventResult, EventResult.class);
                if(eventResult.isSuccess()){
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
