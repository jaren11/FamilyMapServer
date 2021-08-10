package Generate;

import Model.Person;

import java.util.ArrayList;
import java.util.Random;

//Creates a ton of fake data
public class GenerateData {
    private GenerateName generateName = new GenerateName();
    private GenerateLocation generateLocation = new GenerateLocation();
    private GenerateEvent generateEvent;

    private ArrayList<Person> theFamilyTree;
    private String username;
    int genGap = 32;
    private Random rand = new Random();

    public FamilyTreeData beginGeneration(int generations, Person user){
        username = user.getAssociatedUsername();
        generateEvent = new GenerateEvent(username);

        beginFamilyTree(user, generations);

        return new FamilyTreeData(generateEvent.getArrayEvents(), theFamilyTree);
    }

    public void beginFamilyTree(Person root, int generations){
        theFamilyTree = new ArrayList<Person>();
        theFamilyTree.add(root);
        int startingYear = 2002;

        generateEvent.birth(root, startingYear);

        makeParents(root, generations - 1, startingYear);
    }

    public void makeParents(Person current, int generations, int year){
        year = year - genGap - rand.nextInt(10);

        //initialize parents by making mom null, then dad, then mom with dad info
        Person mom = new Person();
        Person dad = makeDad(current, mom.getPersonID());
        mom = makeMom(mom, dad);

        current.setMotherID(mom.getPersonID());
        current.setFatherID(dad.getPersonID());

        makeLife(mom, dad, year);

        theFamilyTree.add(mom);
        theFamilyTree.add(dad);

        if(generations != 0){
            makeParents(mom, generations - 1, year);
            makeParents(dad, generations - 1, year);
        }
    }

    /**
     *
     * @param child
     * @param wife
     * @return
     */
    private Person makeDad(Person child, String wife){
        Person dad = new Person();
        dad.setFirstName(generateName.maleFirstName());
        dad.setGender("m");
        dad.setSpouseID(wife);
        dad.setAssociatedUsername(username);

        if(child.getGender().equals("m")){
            dad.setLastName(child.getLastName());
        }else{
            dad.setLastName(generateName.lastName());
        }
        return dad;
    }

    private Person makeMom(Person mom, Person husband){
        mom.setFirstName(generateName.femaleFirstName());
        mom.setLastName(generateName.lastName());
        mom.setSpouseID(husband.getPersonID());
        mom.setGender("f");
        mom.setAssociatedUsername(username);
        return mom;
    }

    private void makeLife(Person mom, Person dad, int year){
        generateEvent.birth(mom, year);
        generateEvent.birth(dad, year);

        generateEvent.marriage(dad, mom, year);

        generateEvent.death(mom, year);
        generateEvent.death(dad, year);
    }
}
