package Generate;

import Model.Event;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateName {
    //make a male name
    public String maleFirstName() {
        GenerateName generateName = new GenerateName();

        try{
            List<String> mNames = generateName.parse(new File("D:\\School\\2021 Summer\\CS 240\\FamilyMapServerStudent-master\\json\\mnames.json"));
            Random rand = new Random();
            int index = rand.nextInt(mNames.size());

            String name = mNames.get(index);

            return name;

        }catch (RuntimeException e){
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error: something went wrong";
    }

    //make a female name
    public String femaleFirstName() {
        GenerateName generateName = new GenerateName();

        try{
            List<String> mNames = generateName.parse(new File("D:\\School\\2021 Summer\\CS 240\\FamilyMapServerStudent-master\\json\\fnames.json"));
            Random rand = new Random();
            int index = rand.nextInt(mNames.size());

            String name = mNames.get(index);

            return name;

        }catch (RuntimeException e){
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error: something went wrong";
    }

    //make a last name
    public String lastName() {
        GenerateName generateName = new GenerateName();

        try{
            List<String> mNames = generateName.parse(new File("D:\\School\\2021 Summer\\CS 240\\FamilyMapServerStudent-master\\json\\snames.json"));
            Random rand = new Random();
            int index = rand.nextInt(mNames.size());

            String name = mNames.get(index);

            return name;

        }catch (RuntimeException e){
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error: something went wrong";
    }

    private List<String> parse(File file) throws IOException {
        List<String> names = new ArrayList<>();

        try(FileReader fileReader = new FileReader(file)){
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            JSONTokener tokener = new JSONTokener(bufferedReader);
            JSONObject rootObj = new JSONObject(tokener);

            JSONArray locArr = rootObj.getJSONArray("data");
            for(int i = 0; i < locArr.length(); i++){
                names.add((String) locArr.get(i));
            }
        }
        return names;
    }

}
