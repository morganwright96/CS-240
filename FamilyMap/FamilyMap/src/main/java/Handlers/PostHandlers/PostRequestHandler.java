package Handlers.PostHandlers;

import DAO.DataAccessException;
import DAO.Database;
import Handlers.JsonEncoder;
import Service.Result.ClearResult;
import Service.Services;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.Connection;


public class PostRequestHandler implements HttpHandler {
    InputStream reqBody;
    String reqData;
    Database db = new Database();
    Connection conn;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            conn = db.openConnection();
            if(exchange.getRequestMethod().toUpperCase().equals("POST")){
                reqBody = exchange.getRequestBody();
                reqData = readString(reqBody);
            }
            else {
                db.closeConnection(false);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            e.printStackTrace();
        }
    }

    public void responseBodyWriter(HttpExchange exchange, String respData) throws IOException {
        OutputStream respBody = exchange.getResponseBody();
        respBody.write(respData.getBytes());
        respBody.close();
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }
}
