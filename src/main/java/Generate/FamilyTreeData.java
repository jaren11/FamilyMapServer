package Generate;

import Model.Event;
import Model.Person;

import java.util.ArrayList;

//Basically, this class contains a family tree list and an event list to store generated data
public class FamilyTreeData {
    private ArrayList<Event> theEvents;
    private ArrayList<Person> familyTree;

    public FamilyTreeData(ArrayList<Event> theEvents, ArrayList<Person> familyTree) {
        this.theEvents = theEvents;
        this.familyTree = familyTree;
    }

    public FamilyTreeData() {
        this.theEvents = null;
        this.familyTree = null;
    }

    public ArrayList<Event> getTheEvents() {
        return theEvents;
    }

    public void setTheEvents(ArrayList<Event> theEvents) {
        this.theEvents = theEvents;
    }

    public ArrayList<Person> getFamilyTree() {
        return familyTree;
    }

    public void setFamilyTree(ArrayList<Person> familyTree) {
        this.familyTree = familyTree;
    }
}
