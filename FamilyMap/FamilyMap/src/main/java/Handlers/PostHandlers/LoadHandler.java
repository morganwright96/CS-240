package Handlers.PostHandlers;

import DAO.DataAccessException;
import Handlers.JsonEncoder;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Request.LoadRequest;
import Service.Result.ClearResult;
import Service.Result.LoadResult;
import Service.Services;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class LoadHandler extends PostRequestHandler{
    ArrayList<Person> people = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Event> events = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            super.handle(exchange);
            Services services = new Services(getConn());
            OutputStream respBody = exchange.getResponseBody();
            ClearResult clearResult = services.clear();
            if(clearResult.isSuccess()){
                if(getReqData().contains("users") && getReqData().contains("persons") && getReqData().contains("events")){
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson( getReqData(), JsonObject.class);
                    JsonArray usersJson = jsonObject.get("users").getAsJsonArray(); // returns a JsonElement for that name
                    JsonArray peopleJson = jsonObject.get("persons").getAsJsonArray();
                    JsonArray eventsJson = jsonObject.get("events").getAsJsonArray();

                    for(int i = 0; i < usersJson.size(); i++){
                        User newUser = JsonEncoder.deserialize(usersJson.get(i).toString(), User.class);
                        users.add(newUser);
                    }

                    for(int i = 0; i < peopleJson.size(); i++){
                        Person newPerson = JsonEncoder.deserialize(peopleJson.get(i).toString(), Person.class);
                        people.add(newPerson);
                    }

                    for(int i = 0; i < eventsJson.size(); i++) {
                        Event newEvent = JsonEncoder.deserialize(eventsJson.get(i).toString(), Event.class);
                        events.add(newEvent);
                    }
                    LoadRequest loadRequest = new LoadRequest(users, people, events);
                    LoadResult loadResult = services.load(loadRequest);
                    String respData = JsonEncoder.serialize(loadResult, LoadResult.class);

                    if(loadResult.isSuccess()){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        responseBodyWriter(exchange, respData);
                        getDb().closeConnection(true);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
                        responseBodyWriter(exchange, respData);
                        getDb().closeConnection(false);
                    }
                }
                else{
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    getDb().closeConnection(false);
                }
            }
            else {
                String respData = JsonEncoder.serialize(clearResult, ClearResult.class);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                responseBodyWriter(exchange, respData);
                getDb().closeConnection(false);
            }
            users.clear();
            people.clear();
            events.clear();
        } catch (IOException | DataAccessException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            e.printStackTrace();
        }
    }
}
