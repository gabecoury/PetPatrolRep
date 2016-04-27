package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* Pet stores the information for the
* Pets used in the app.
*************************************
*/

import java.util.UUID;

public class Pet {

    private UUID mPetId;
    private boolean mFound;
    // Insert location variable
    private String mPetType;
    private String mPetDescription;
    private String mPetName;
    private int mContactNumber;
    private String mDetails;
    private int mAdoptID;

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public double getLatit() {
        return latit;
    }

    public void setLatit(double latit) {
        this.latit = latit;
    }

    private double longi;
    private double latit;

    public Pet() {
        mPetId = UUID.randomUUID();
    }

    public UUID getId() {
        return mPetId;
    }

    public void setId(UUID id) {
        this.mPetId = id;
    }

    public boolean isFound() {
        return mFound;
    }

    public void setFound(boolean found) {
        this.mFound = found;
    }

    public String getPetType() {
        return mPetType;
    }

    public void setPetType(String petType) {
        this.mPetType = petType;
    }

    public String getPetDescription() {
        return mPetDescription;
    }

    public void setPetDescription(String petDescription) {
        this.mPetDescription = petDescription;
    }

    public String getPetName() {
        return mPetName;
    }

    public void setPetName(String petName) {
        this.mPetName = petName;
    }

    public int getContactNumber() {
        return mContactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.mContactNumber = contactNumber;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        this.mDetails = details;
    }

    public int getAdoptID() {
        return mAdoptID;
    }

    public void setAdoptID(int adoptID) {
        this.mAdoptID = adoptID;
    }
}