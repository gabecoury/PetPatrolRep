package edu.wmich.petpatrol;

import java.util.UUID;

//object for each lost or found pet. individual object
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