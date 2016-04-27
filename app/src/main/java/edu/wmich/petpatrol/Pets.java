package edu.wmich.petpatrol;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//singleton that stores all the lost or found pets.so basically this is collection of all of the events.
public class Pets {
    private static Pets sPets;
    private static double templng = 0;
    private static double templat= 0;

    public static double getTemplat() {
        return templat;
    }

    public static void setTemplat(double templat) {
        Pets.templat = templat;
    }

    public static double getTemplng() {
        return templng;
    }

    public static void setTemplng(double templng) {
        Pets.templng = templng;
    }

    private ArrayList<Pet> mPets;

    public static Pets get(Context context) {
        if (sPets == null) {
            sPets = new Pets(context);
        }
        return sPets;
    }

    private Pets(Context context) {
        mPets = new ArrayList<>();
    }

    public void addPet(Pet p){
        mPets.add(p);
    }

    public List<Pet> getPets() {
        return mPets;
    }

    public Pet getPet(UUID id) {
        for(Pet pet: mPets) {
            if(pet.getId().equals(id)) {
                return pet;
            }
        }
        return null;
    }
}