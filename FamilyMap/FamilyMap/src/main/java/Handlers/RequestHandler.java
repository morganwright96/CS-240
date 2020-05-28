package Handlers;

import DAO.DataAccessException;
import DAO.Database;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;


public class RequestHandler implements HttpHandler {

    private InputStream reqBody;
    private String reqData;
    private Database db = new Database();
    private Connection conn;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            conn = db.openConnection();
            reqBody = exchange.getRequestBody();
            reqData = readString(reqBody);
        } catch (DataAccessException e) {
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

    public InputStream getReqBody() {
        return reqBody;
    }

    public String getReqData() {
        return reqData;
    }

    public Database getDb() {
        return db;
    }

    public Connection getConn() {
        return conn;
    }
}
