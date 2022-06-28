package com.malliaris_traga.dietplanner;

import java.util.ArrayList;
import java.util.List;

public class UserAccount {

    private String UserId;
    private String Username;
    private String DateOfBirth;
    private float Height;
    private String Gender;
    private String ImageURL;

    private List<DiaryEntry> DiaryEntries = new ArrayList<>();

    public UserAccount(){ }

    public UserAccount(String userId, String username, String dateOfBirth, float height, String gender, String imageURL){
        UserId = userId;
        Username = username;
        DateOfBirth = dateOfBirth;
        Height = height;
        Gender = gender;
        ImageURL = imageURL;
    }

    public String getUserId() {return UserId;}
    public void setUserId(String userId) {UserId = userId;}
    public String getUsername() {return Username;}
    public void setUsername(String username) {Username = username;}
    public String getDateOfBirth() {return DateOfBirth;}
    public void setDateOfBirth(String dateOfBirth) {DateOfBirth = dateOfBirth;}
    public float getHeight() {return Height;}
    public void setHeight(float height) {Height = height;}
    public String getGender() {return Gender;}
    public void setGender(String gender) {Gender = gender;}
    public String getImageURL() {return ImageURL;}
    public void setImageURL(String imageURL) {ImageURL = imageURL;}
    public List<DiaryEntry> getDiaryEntries() {
        return DiaryEntries;
    }
    public void setDiaryEntries(List<DiaryEntry> diaryEntries) {
        DiaryEntries = diaryEntries;
    }

    public int CalculateAge(){
        return ExtraMethods.YearsBetweenDates(this.DateOfBirth, ExtraMethods.GetCurrentDate());
    }
}
