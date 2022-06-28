package com.malliaris_traga.dietplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FoodSelectionActivity extends AppCompatActivity {

    private ArrayList<String> foodList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_selection);

    }

    public void AddToFoodList(View view){
        if(((CheckBox)view).isChecked()){
            view.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.blueSelection));
            foodList.add(view.getTag().toString());
        }
        else{
            view.setBackgroundTintList(ContextCompat.getColorStateList(this,R.color.white));
            foodList.remove(view.getTag().toString());
        }
    }

    private void GoToMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void ButtonCancelClick(View view){
        GoToMainActivity();
    }

    private void GoToFoodQuantityActivity(){
        Intent intent = new Intent(this,FoodQuantityActivity.class);
        intent.putStringArrayListExtra("foodList",foodList);
        startActivity(intent);
    }

    public void ButtonNextClick(View view){
        GoToFoodQuantityActivity();
    }
}