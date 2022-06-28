package com.malliaris_traga.dietplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class FoodQuantityActivity extends AppCompatActivity {

    ImageView imageView;
    EditText editTextQnt, editTextWater;
    ArrayList<String> foodList;
    LinearLayout layoutFoodQnt;
    private static ArrayList<FoodEntry> foodEntryList = new ArrayList<>();

    public static ArrayList<FoodEntry> getFoodEntryList() {
        return foodEntryList;
    }

    private static float waterLiters = 0f;

    public static float getWaterLiters() {
        return waterLiters;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_quantity);

        foodList = getIntent().getStringArrayListExtra("foodList");

        layoutFoodQnt = findViewById(R.id.layoutFoodQuantity);

        editTextWater = findViewById(R.id.editTextWater);

        AddFoodToLayout();
    }

    private LinearLayout CreateLayoutFood(String tagText){

        LinearLayout layoutHorizontal = new LinearLayout(this);
        layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutHorizontalParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        layoutHorizontal.setLayoutParams(layoutHorizontalParams);


        String imageName = tagText.toLowerCase();
        int resId= getResources().getIdentifier(imageName,"drawable",getPackageName());

        imageView = new ImageView(this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
        imageParams.gravity = Gravity.CENTER;
        imageParams.setMargins(10,0,0,10);
        imageView.setLayoutParams(imageParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(resId);
        imageView.setTag(tagText);

        editTextQnt = new EditText(this);
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        editTextParams.gravity = Gravity.CENTER;
        editTextParams.setMargins(10,0,10,0);
        editTextQnt.setLayoutParams(editTextParams);
        editTextQnt.setInputType(InputType.TYPE_CLASS_NUMBER);
        editTextQnt.setTag(tagText+"txt");

        layoutHorizontal.addView(imageView);
        layoutHorizontal.addView(editTextQnt);

        return layoutHorizontal;
    }

    private void AddFoodToLayout(){
        for(String food : foodList){
            layoutFoodQnt.addView(CreateLayoutFood(food));
        }
    }

    private void CreateFoodEntry(){
        foodEntryList.clear();

        for(String food : foodList){
            FoodEntry foodEntry = new FoodEntry();
            foodEntry.setFoodid(food);
            editTextQnt = (EditText)layoutFoodQnt.findViewWithTag(food+"txt");
            foodEntry.setServings(Integer.parseInt(editTextQnt.getText().toString()));

            foodEntryList.add(foodEntry);
        }
    }

    private void GoToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void ButtonCancelClick(View view){
        GoToMainActivity();
    }

    private void GoToMeasurementsActivity(){
        Intent intent = new Intent(this,MeasurementsActivity.class);
        waterLiters = Float.parseFloat(editTextWater.getText().toString());
        startActivity(intent);
    }

    public void ButtonNextClick(View view){
        CreateFoodEntry();
        //Toast.makeText(this,String.valueOf(foodEntryList.get(1).getServings()),Toast.LENGTH_SHORT).show();
        GoToMeasurementsActivity();
    }
}