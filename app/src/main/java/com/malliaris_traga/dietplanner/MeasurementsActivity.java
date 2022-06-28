package com.malliaris_traga.dietplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MeasurementsActivity extends AppCompatActivity {

    EditText editTextAbdoment, editTextHip, editTextNeck, editTextWaist,editTextWeight;
    Spinner spinner;

    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);

        String[] dailyActivitySpinner = {"Light" , "Medium", "Intense"};
        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,dailyActivitySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        editTextAbdoment = findViewById(R.id.editTextAbdoment);
        editTextHip = findViewById(R.id.editTextHip);
        editTextNeck = findViewById(R.id.editTextNeck);
        editTextWaist = findViewById(R.id.editTextWaist);
        editTextWeight = findViewById(R.id.editTextWeight);

        database = FirebaseDatabase.getInstance("https://dietplanner-663e1-default-rtdb.firebaseio.com/");
        dbRef = database.getReference("diaries");
        //Toast.makeText(this,String.valueOf(foodEntryList.get(1).getServings()),Toast.LENGTH_SHORT).show();
    }

    private float CalculateDailyCalories(){
        float totalCalories = 0;
        for (FoodEntry foodEntry: FoodQuantityActivity.getFoodEntryList())
        {
            String foodID = foodEntry.getFoodid();
            float foodCalories = 0;
            int servings = foodEntry.getServings();
            for (Food food: MainActivity.getFoodList())
            {
                if(food.getFoodid().equals(foodID)){
                    foodCalories = food.getCaloriesPerServing();
                    break;
                }
            }

            totalCalories += foodCalories * servings;
        }
        return  totalCalories;
    }

    private void SaveToDiary(DiaryEntry diaryEntry){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        float waterLiters = FoodQuantityActivity.getWaterLiters();

        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(date.getTime());

        diaryEntry.setDate(todayDate);
        diaryEntry.setAbdomenInCM(Float.parseFloat(editTextAbdoment.getText().toString()));
        diaryEntry.setHipInCM(Float.parseFloat(editTextHip.getText().toString()));
        diaryEntry.setPhysicalActivity(TranslatePhysicalActivity(spinner.getSelectedItem().toString()));
        diaryEntry.setNeckInCM(Float.parseFloat(editTextNeck.getText().toString()));
        diaryEntry.setWaistInCM(Float.parseFloat(editTextWaist.getText().toString()));
        diaryEntry.setWaterInLiters(waterLiters);
        diaryEntry.setTotalDailyCalories(CalculateDailyCalories());
        diaryEntry.setWeightInKG(Float.parseFloat(editTextWeight.getText().toString()));

        dbRef.child(userId).child(todayDate).setValue(diaryEntry);//.addOnCompleteListener();//HERE
    }

    private float TranslatePhysicalActivity(String activity){

        float dailyActivity = 1.2f;

        switch (activity) {
            case "Light":
                dailyActivity = 1.2f;
                break;
            case "Medium":
                dailyActivity = 1.55f;
                break;

            case "Intense":
                dailyActivity = 1.9f;
                break;
        }

        return dailyActivity;
    }

    private void GoToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void ButtonCancelClick(View view){
        GoToMainActivity();
    }

    public void ButtonNextClick(View view){
        SaveToDiary(new DiaryEntry());
        GoToMainActivity();
    }
}