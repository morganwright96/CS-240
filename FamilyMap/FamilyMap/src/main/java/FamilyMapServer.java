import Handlers.PostHandlers.ClearHandler;
import Handlers.FileHandler;
import Handlers.PostHandlers.FillHandler;
import Handlers.PostHandlers.LoadHandler;
import Handlers.PostHandlers.UserLoginHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length == 0){
            return;
        }
        int port = Integer.parseInt(args[0]);
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private static void registerHandlers(HttpServer server) {
        server.createContext("/", new FileHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/user/login", new UserLoginHandler());
        server.createContext("/fill", new FillHandler());
    }

}
