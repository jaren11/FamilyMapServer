package Generate;

import Model.Event;
import Model.Person;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateEvent {
    private GenerateLocation generateLocation = new GenerateLocation();
    private Random random = new Random();
    public String username;
    public ArrayList<Event> arrayEvents;

    public GenerateEvent(String nameUser){
        this.username = nameUser;
        this.arrayEvents = new ArrayList<Event>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Event> getArrayEvents() {
        return arrayEvents;
    }

    public void setArrayEvents(ArrayList<Event> arrayEvents) {
        this.arrayEvents = arrayEvents;
    }

    public void marriage(Person husband, Person wife, int currentYear){
        int year = 0;
        year = currentYear + random.nextInt(6) + 18;

        Event dadMarriage = generateLocation.generateLocation();
        dadMarriage.setAssociatedUsername(username);
        dadMarriage.setEventType("Marriage");
        dadMarriage.setYear(year);
        dadMarriage.setPersonID(husband.getPersonID());

        Event momMarriage = new Event();
        momMarriage.setPersonID(wife.getPersonID());
        momMarriage.setAssociatedUsername(username);
        momMarriage.setEventType("Marriage");
        momMarriage.setYear(year);
        momMarriage.setLatitude(dadMarriage.getLatitude());
        momMarriage.setLongitude(dadMarriage.getLongitude());
        momMarriage.setCity(dadMarriage.getCity());
        momMarriage.setCountry(dadMarriage.getCountry());

        arrayEvents.add(dadMarriage);
        arrayEvents.add(momMarriage);
    }

    public void death(Person thePerson, int currentYear){
        int life = 40;
        int year = 0;
        year = currentYear + life + random.nextInt(40);

        if(year > 2021){
            year = 2021;
        }

        Event theDeath = generateLocation.generateLocation();
        theDeath.setAssociatedUsername(username);
        theDeath.setEventType("Death");
        theDeath.setYear(year);
        theDeath.setPersonID(thePerson.getPersonID());

        arrayEvents.add(theDeath);
    }

    public void birth(Person thePerson, int currentYear){
        int year = 0;
        year = currentYear - random.nextInt(6);

        Event theBirth = generateLocation.generateLocation();
        theBirth.setAssociatedUsername(username);
        theBirth.setEventType("Birth");
        theBirth.setYear(year);
        theBirth.setPersonID(thePerson.getPersonID());

        arrayEvents.add(theBirth);
    }

    public void otherEvent(Person thePerson, int currentYear){
        int year = 10;
        year = currentYear + 10 + random.nextInt(20);

        Event theOther = generateLocation.generateLocation();
        theOther.setAssociatedUsername(username);
        theOther.setEventType(makeType(username));
        theOther.setYear(year);
        theOther.setPersonID(thePerson.getPersonID());

        arrayEvents.add(theOther);
    }

    public String makeType(String username){
        GenerateEvent generateEvent = new GenerateEvent(username);
        try{
            List<String> typesEvent = generateEvent.parse(new File("D:\\School\\2021 Summer\\CS 240\\FamilyMapServerStudent-master\\json\\typesofevent.json"));
            Random rand = new Random();
            int index = rand.nextInt(typesEvent.size());

            String type = typesEvent.get(index);

            return type;

        }catch (RuntimeException e){
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> parse(File file) throws IOException {
        List<String> typeEvent = new ArrayList<>();

        try(FileReader fileReader = new FileReader(file)){
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            JSONTokener tokener = new JSONTokener(bufferedReader);
            JSONObject rootObj = new JSONObject(tokener);

            JSONArray locArr = rootObj.getJSONArray("data");
            for(int i = 0; i < locArr.length(); i++){
                typeEvent.add((String) locArr.get(i));
            }
        }
        return typeEvent;
    }
}
