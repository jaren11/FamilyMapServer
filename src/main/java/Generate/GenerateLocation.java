package Generate;

import Model.Event;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;


//returns an event with a random location
public class GenerateLocation {
    public Event generateLocation() {
        Event theEvent = new Event(null, null, null, 0, 0, null, null, null, 0);

        GenerateLocation generateLocation = new GenerateLocation();

        try{
            List<Event> locations = generateLocation.parse(new File("D:\\School\\2021 Summer\\CS 240\\FamilyMapServerStudent-master\\json\\locations.json"));
            Random rand = new Random();
            int index = rand.nextInt(locations.size());

            theEvent = locations.get(index);

            return theEvent;

        }catch (RuntimeException e){
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new Event(null, null, null, 0, 0, null, null, null, 0);
    }

    private List<Event> parse(File file) throws IOException {
        List<Event> locations = new ArrayList<>();

        try(FileReader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            JSONTokener tokener = new JSONTokener(bufferedReader);
            JSONObject rootObj = new JSONObject(tokener);

            String jsonString = rootObj.getJSONArray("data").toString();

            Gson g = new Gson();

            locations = g.fromJson(jsonString, new TypeToken<List<Event>>() {
            }.getType());
        }
        return locations;
    }
}
