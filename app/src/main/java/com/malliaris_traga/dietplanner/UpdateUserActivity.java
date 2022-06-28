package com.malliaris_traga.dietplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateUserActivity extends AppCompatActivity {

    private EditText textURL, textHeight;
    private RadioButton radioButtonSelected;
    private RadioGroup radioGroup;
    private DatePicker datePicker;
    private ImageView imageViewProfile;

    FirebaseDatabase database;
    DatabaseReference dbRef;

    private UserAccount user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        textURL = findViewById(R.id.editTextImageUpdateURL);
        textHeight = findViewById(R.id.editTextHeightUpdate);

        datePicker = findViewById(R.id.simpleDatePickerUpdate);
        datePicker.setMaxDate(System.currentTimeMillis() - 31556952000l);

        radioGroup = findViewById(R.id.radioGroupGenderUpdate);
        radioButtonSelected = findViewById(radioGroup.getCheckedRadioButtonId());

        imageViewProfile = findViewById(R.id.imageViewImageUpdateURL);

        user = MainActivity.getLoggedUser();
        LoadUserDataInUI(user);

        database = FirebaseDatabase.getInstance("https://dietplanner-663e1-default-rtdb.firebaseio.com/");
        dbRef = database.getReference("users");
    }

    private void LoadUserDataInUI(UserAccount user){
        Picasso.get()
                .load(user.getImageURL())
                .into(imageViewProfile);

        textHeight.setText(String.valueOf(user.getHeight()));
        if(user.getGender().equals("Female")){
            radioGroup.check(R.id.radioButtonFemale);
            radioButtonSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        }

        Calendar date = ExtraMethods.ConvertDateFromString(user.getDateOfBirth());
        datePicker.init(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE),null);

        textURL.setText(user.getImageURL());
    }

    private void Update(UserAccount user){
        try{
            radioButtonSelected = findViewById(radioGroup.getCheckedRadioButtonId());
            user.setGender(radioButtonSelected.getText().toString());
            user.setDateOfBirth(ExtraMethods.GetDateStringFromDatePicker(datePicker));

            user.setHeight(Float.parseFloat(textHeight.getText().toString()));
            user.setImageURL(textURL.getText().toString());

            dbRef.child(user.getUserId()).setValue(user).addOnCompleteListener(
                    new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(UpdateUserActivity.this,"Update successful!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
            );
        }
        catch (Exception e){
            Toast.makeText(this,"Update Failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void ButtonPreviewClick(View view){
        try{
            Picasso.get()
                    .load(textURL.getText().toString())
                    .into(imageViewProfile);
        }
        catch (Exception e){
            return;
        }
    }

    public void ButtonUpdateClick(View view){
        Update(user);
        GoToMainActivity();
    }

    private void GoToMainActivity(){
        finish();
    }

    public void ButtonCancelClick(View view){
        GoToMainActivity();
    }

}