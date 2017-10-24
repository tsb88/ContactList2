package com.example.tejasbhoir.contactlist;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sanjaybhoir2002 on 10/17/17.
 */

class Relationships implements Serializable {
    private ArrayList<String> relations;

    Relationships(ArrayList<String> relations) {
        this.relations = relations;
    }

    void removeElement(String item) {
        this.relations.remove(item);
    }

    void addEmptyMessage() {
        this.relations.add("No relationships");
    }

    boolean isEmpty() {
        return relations.isEmpty();
    }

    void addElement(String item, int position) {
        this.relations.add(position, item);
    }

    String getElement(int position) {
        return relations.get(position);
    }

    ArrayList<String> getRelations() {
        return relations;
    }
}
