package Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

public class NameGenerator {
    ArrayList<String> fNames = new ArrayList<>();
    ArrayList<String> surnames = new ArrayList<>();
    ArrayList<String> maidenNames = new ArrayList<>();

    public NameGenerator() {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("json/fnames.json"));
            JsonObject jsonObject = gson.fromJson( reader, JsonObject.class);

            JsonArray json = jsonObject.get("data").getAsJsonArray();
            for(int i = 0; i < json.size(); i++){
                fNames.add(json.get(i).toString());
            }

            reader = new JsonReader(new FileReader("json/snames.json"));
            jsonObject = gson.fromJson(reader, JsonObject.class);
            json = jsonObject.get("data").getAsJsonArray();
            for(int i = 0; i < json.size(); i++){
                surnames.add(json.get(i).toString());
            }

            reader = new JsonReader(new FileReader("json/mnames.json"));
            jsonObject = gson.fromJson(reader, JsonObject.class);
            json = jsonObject.get("data").getAsJsonArray();
            for(int i = 0; i < json.size(); i++){
                maidenNames.add(json.get(i).toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getfName() {
        Random random = new Random();
        int rand = random.nextInt(fNames.size());
        return fNames.get(rand).replace("\"", "");
    }

    public String getSurname() {
        Random random = new Random();
        int rand = random.nextInt(surnames.size());
        return surnames.get(rand).replace("\"", "");
    }

    public String getMaidenName() {
        Random random = new Random();
        int rand = random.nextInt(maidenNames.size());
        return maidenNames.get(rand).replace("\"", "");
    }
}
