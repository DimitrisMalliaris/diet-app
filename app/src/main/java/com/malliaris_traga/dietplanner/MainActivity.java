package com.malliaris_traga.dietplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Logged UserAccount
    private static UserAccount loggedUser;
    public static UserAccount getLoggedUser() {
        return MainActivity.loggedUser;
    }

    // Food List
    private static List<Food> foodList = new ArrayList<>();
    public static List<Food> getFoodList() {
        return MainActivity.foodList;
    }

    FirebaseDatabase database;
    TextView name, age, gender, height, bmi, fat, weight, oweight, tcalories;
    ImageView imageViewProfile;
    LinearLayout diary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // cache UI Views
        name = findViewById(R.id.text_UserName);
        age = findViewById(R.id.text_Age);
        gender = findViewById(R.id.text_Gender);
        height = findViewById(R.id.text_Height);
        bmi = findViewById(R.id.text_BMI);
        fat = findViewById(R.id.text_Fat);
        weight = findViewById(R.id.text_Weight);
        oweight = findViewById(R.id.text_OptimalWeight);
        tcalories = findViewById(R.id.text_TargetDailyCalories);
        imageViewProfile = findViewById(R.id.profileImageView);
        diary = findViewById(R.id.Diary);

        // get DataBase
        database = FirebaseDatabase.getInstance("https://dietplanner-663e1-default-rtdb.firebaseio.com/");

        // get uid from extras
        String uId = FirebaseAuth.getInstance().getUid();

        // load user data from database - set static variable loggedUser
        LoadUserAccount(uId);

        // get foodList from database
        foodList = LoadFoodsFromDatabase();
    }

    private void LoadUIUserInfo(){

        List<DiaryEntry> diaryEntries = loggedUser.getDiaryEntries();

        Collections.sort(diaryEntries, new Comparator<DiaryEntry>() {
            @Override
            public int compare(DiaryEntry o1, DiaryEntry o2) {
                Calendar date1 = ExtraMethods.ConvertDateFromString(o1.getDate());
                Calendar date2 = ExtraMethods.ConvertDateFromString(o2.getDate());

                if(date2.after(date1)){
                    return 1;
                }
                else{
                    return -1;
                }
            }
        });

        loggedUser.setDiaryEntries(diaryEntries);

        name.setText(loggedUser.getUsername());
        age.setText(String.valueOf(loggedUser.CalculateAge()));
        gender.setText(loggedUser.getGender());
        height.setText(String.valueOf(loggedUser.getHeight()));
        bmi.setText(String.format("%.2f", CalculateBMI()));
        fat.setText(String.format("%.2f", CalculateBodyFat()) + " %");
        DiaryEntry lastDiaryEntry = GetLastDiaryEntry();
        weight.setText(String.format("%.0f kg", lastDiaryEntry.getWeightInKG()));
        oweight.setText(String.format("%.0f kg", CalculateIdealWeight()));
        tcalories.setText(String.format("%.0f kcal", CalculateTargetDailyCalories(lastDiaryEntry)));

        // load image
        try{
            Picasso.get()
                    .load(loggedUser.getImageURL())
                    .into(imageViewProfile);
        }
        catch (Exception e){
        }

        // clear previous Diary entries
        diary.removeAllViews();
        // load Diary entries
        for(DiaryEntry diaryEntry: loggedUser.getDiaryEntries()){
            CreateDiaryEntryUIElement(diaryEntry);
        }
    }

    private void CreateDiaryEntryUIElement(DiaryEntry diaryEntry){
        // Outer Container
        LinearLayout outerContainer = new LinearLayout(this);
        outerContainer.setOrientation(LinearLayout.VERTICAL);
        outerContainer.setBackgroundColor(Color.parseColor("#A5EC99"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5,5,5,5);
        outerContainer.setLayoutParams(layoutParams);

        // Date Text
        TextView textView = new TextView(this);
        textView.setText(diaryEntry.getDate());
        textView.setGravity(Gravity.CENTER);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        outerContainer.addView(textView);

        // Inner Container
        LinearLayout innerContainer = new LinearLayout(this);
        innerContainer.setOrientation(LinearLayout.VERTICAL);
        innerContainer.setBackgroundColor(Color.WHITE);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5,5,5,5);
        innerContainer.setLayoutParams(layoutParams);
        outerContainer.addView(innerContainer);

        // Calories Text
        textView = new TextView(this);
        textView.setText("Consumed Calories: " + String.valueOf(diaryEntry.getTotalDailyCalories()));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        innerContainer.addView(textView);

        // Caloric Target Text
        textView = new TextView(this);
        float targetCalories = CalculateTargetDailyCalories(diaryEntry);
        textView.setText("Target Calories: " + String.valueOf(targetCalories));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        innerContainer.addView(textView);

        // Caloric Deviation Text
        textView = new TextView(this);
        float caloricDiff = CalculateCaloricDifferenceFromTargetCalories(diaryEntry);
        if(caloricDiff >= 0){
            if(caloricDiff >= 200){
                textView.setBackgroundColor(Color.parseColor("#FF0000"));
            }
            else{
                textView.setBackgroundColor(Color.parseColor("#00FF00"));
            }
        }
        else{
            if(caloricDiff <= -200){
                textView.setBackgroundColor(Color.parseColor("#FF0000"));
            }
            else{
                textView.setBackgroundColor(Color.parseColor("#00FF00"));
            }
        }

        textView.setText("Caloric Deviation: " + String.valueOf(caloricDiff));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        innerContainer.addView(textView);

        // Measured Weight Text
        textView = new TextView(this);
        float currentWeight = diaryEntry.getWeightInKG();
        textView.setText("Measured Weight: " + String.valueOf(currentWeight));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        innerContainer.addView(textView);

        // Water Text
        textView = new TextView(this);
        textView.setText("Consumed Water: " + String.valueOf(diaryEntry.getWaterInLiters()));
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        innerContainer.addView(textView);

        // Delete Button
        Button button = new Button(this);
        button.setText("Delete");
        button.setTextColor(Color.WHITE);
        button.setTextSize(14f);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        button.setLayoutParams(layoutParams);
        button.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.light_blue));
        button.setOnClickListener(v -> DeleteDiaryEntry(diaryEntry.getDate()));
        outerContainer.addView(button);

        diary.addView(outerContainer);
    }

    private void LoadUserAccount(String uId){
        // user load data from database
        DatabaseReference userDbRef = database.getReference("users").child(uId);

        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                loggedUser = dataSnapshot.getValue(UserAccount.class);

                LoadUserDiary(loggedUser.getUserId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LoadUserDiary(String uId){
        // load user diary from database
        DatabaseReference userDiaryDbRef = database.getReference("diaries").child(uId);

        userDiaryDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // create local diary entry list
                List<DiaryEntry> diaryEntries = new ArrayList<>();
                // loop data snapshot from db - create diary entries
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    DiaryEntry diaryEntry = dataSnapshot1.getValue(DiaryEntry.class);
                    // add to diary entry list
                    diaryEntries.add(diaryEntry);
                }
                // set users diary entries
                loggedUser.setDiaryEntries(diaryEntries);

                // load user info to the UI
                LoadUIUserInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<Food> LoadFoodsFromDatabase() {
        List<Food> foodList = new ArrayList<>();

        DatabaseReference foodDbRef = database.getReference("foods");
        // get food from DB
        foodDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // create foodList - foreach loop
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Food food1 = ds.getValue(Food.class);
                    foodList.add(food1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return foodList;
    }

    private float CalculateTargetDailyCalories(DiaryEntry diaryEntry){
        if(diaryEntry.getWeightInKG() == 0){return 0;}

        float idealWeight = CalculateIdealWeight();
        float bmr = CalculateBMR(diaryEntry);

        if(diaryEntry.getWeightInKG() >= 0.75 * idealWeight && diaryEntry.getWeightInKG() < idealWeight){
            return bmr + 200;
        }
        else if(diaryEntry.getWeightInKG() <= 1.25 * idealWeight && diaryEntry.getWeightInKG() > idealWeight){
            return bmr - 200;
        }

        float targetDailyCalories = 0;
        float weightDiff = diaryEntry.getWeightInKG() - idealWeight;

        int op = 1;
        if(weightDiff < 0){
            op = -1;
        }

        float maxWeightLossPerWeek = 0.45f;
        float caloriesPerKG = 7000f;
        float maxCaloriesPerWeek = maxWeightLossPerWeek * caloriesPerKG;

        float caloriesToLose = weightDiff * 7000;

        float caloriesPerWeek = op * maxCaloriesPerWeek;

        if(op * caloriesToLose < maxCaloriesPerWeek){
            caloriesPerWeek = caloriesToLose;
        }

        float caloriesPerDay = caloriesPerWeek / 7;

        targetDailyCalories = bmr - caloriesPerDay;

        return targetDailyCalories;
    }

    private float CalculateIdealWeight(){
        float idealWeight = 0;
        float height = loggedUser.getHeight();
        if(loggedUser.getGender().equals("Male")){
            idealWeight = (float) (52 + 1.9 * ((height-152.4)/2.54));
        }else{
            idealWeight = (float) (49 + 1.7 * ((height-152.4)/2.54));
        }
        return idealWeight;
    }

    private float CalculateBodyFat(){
        if(loggedUser.getDiaryEntries().size() <= 0){ return 0; }

        float bodyfat = 0;
        DiaryEntry lastDiaryEntry = loggedUser.getDiaryEntries().get(0);

        if(loggedUser.getGender().equals("Male")){
            float abdomen = lastDiaryEntry.getAbdomenInCM();
            float neck = lastDiaryEntry.getNeckInCM();
            float height = loggedUser.getHeight();
            bodyfat = (float) (86.01 * Math.log10(abdomen - neck) - 70.041 * Math.log10(height) + 30.30);
        }
        else{
            float waist = lastDiaryEntry.getWaistInCM();
            float hip = lastDiaryEntry.getHipInCM();
            float neck = lastDiaryEntry.getNeckInCM();
            float height = loggedUser.getHeight();
            bodyfat = (float) (163.205 * Math.log10(waist + hip - neck) - 97.684 * Math.log10(height) + 104.912);
        }
        return bodyfat;
    }

    private float CalculateBMR(DiaryEntry diaryEntry){
        float weight = diaryEntry.getWeightInKG();
        float physicalActivity = diaryEntry.getPhysicalActivity();
        float height = loggedUser.getHeight();
        int age = loggedUser.CalculateAge();

        float bmr = 0;
        if(loggedUser.getGender().equals("Male")){
            bmr = (float) (10 * weight + 6.25 * height - 5 * age + 5) * physicalActivity;
        }else{
            bmr = (float) (10 * weight + 6.25 * height - 5 * age - 161) * physicalActivity;
        }
        return bmr;
    }

    private float CalculateBMI(){
        if(loggedUser.getDiaryEntries().size() <= 0){ return 0; }
        DiaryEntry lastDiaryEntry = loggedUser.getDiaryEntries().get(0);
        float weight = lastDiaryEntry.getWeightInKG();
        float height = loggedUser.getHeight();
        float bmi = (float) (weight / Math.pow(height/100,2));
        return bmi;
    }

    private DiaryEntry GetLastDiaryEntry(){
        if(loggedUser.getDiaryEntries().size() <= 0){ return new DiaryEntry(); }
        DiaryEntry lastDiaryEntry = loggedUser.getDiaryEntries().get(0);
        return lastDiaryEntry;
    }

    public void btn_EditClick(View view){
        GoToUpdateUserActivity();
    }

    public void btn_AddDiaryEntryClick(View view){
        GoToFoodSelectionActivity();
    }

    private void GoToUpdateUserActivity(){
        Intent intent = new Intent(this, UpdateUserActivity.class);
        startActivity(intent);
    }

    private void GoToFoodSelectionActivity(){
        Intent intent = new Intent(this, FoodSelectionActivity.class);
        startActivity(intent);
    }

    private void DeleteDiaryEntry(String id){
        String uId = loggedUser.getUserId();
        DatabaseReference userDiaryDbRef = database.getReference("diaries").child(uId);
        userDiaryDbRef.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Diary entry deleted successfully!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Diary entry could not be deleted!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private float CalculateCaloricDifferenceFromTargetCalories(DiaryEntry diaryEntry){
        float targetCalories = CalculateTargetDailyCalories(diaryEntry);
        float consumedCalories = diaryEntry.getTotalDailyCalories();

        float caloricDifference = targetCalories - consumedCalories;
        return caloricDifference;
    }
}