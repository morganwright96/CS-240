package Handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonEncoder {

    public static <T> T deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }

    public static <T> String serialize(Object o, Class<T> returnType) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(o, returnType);
    }
}
