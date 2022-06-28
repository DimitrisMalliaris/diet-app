package com.malliaris_traga.dietplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    private EditText textURL, textEmail, textPassword, textHeight;
    private RadioButton radioButtonSelected;
    private RadioGroup radioGroup;
    private DatePicker datePicker;
    private ImageView imageViewProfile;

    FirebaseDatabase database;
    DatabaseReference dbRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textURL = findViewById(R.id.editTextImageRegisterURL);
        textEmail = findViewById(R.id.editTextTextEmailAddressRegister);
        textPassword = findViewById(R.id.editTextTextPasswordRegister);
        textHeight = findViewById(R.id.editTextHeightRegister);

        datePicker = findViewById(R.id.simpleDatePickerRegister);
        datePicker.setMaxDate(System.currentTimeMillis() - 31556952000l);

        radioGroup = findViewById(R.id.radioGroupGenderRegister);
        radioButtonSelected = findViewById(radioGroup.getCheckedRadioButtonId());

        imageViewProfile = findViewById(R.id.imageViewImageRegisterURL);

        database = FirebaseDatabase.getInstance("https://dietplanner-663e1-default-rtdb.firebaseio.com/");
        mAuth = FirebaseAuth.getInstance();
        dbRef = database.getReference("users");
    }

    public void ButtonSignUpClick(View view){
        Register(new UserAccount());
    }

    private void Register(UserAccount newUser){

        try{
            mAuth.createUserWithEmailAndPassword(textEmail.getText().toString(),textPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(RegisterActivity.this,mAuth.getCurrentUser().getUid() +" !",
                                //Toast.LENGTH_SHORT).show();

                        newUser.setUserId(mAuth.getCurrentUser().getUid());
                        RegisterExtraData(newUser);
                        GoToLoginActivity();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Account already registered.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        catch (Exception e){
            Toast.makeText(this,"Registration failed. Please check your input.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void RegisterExtraData(UserAccount newUser){

        try{

            newUser.setDateOfBirth(ExtraMethods.GetDateStringFromDatePicker(datePicker));
            newUser.setUsername(textEmail.getText().toString());
            newUser.setGender(radioButtonSelected.getText().toString());
            newUser.setHeight(Float.parseFloat(textHeight.getText().toString()));
            newUser.setImageURL(textURL.getText().toString());

            dbRef.child(newUser.getUserId()).setValue(newUser).addOnCompleteListener(
                    new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Registration successful!",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this,"Registration was not completed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }
        catch(Exception e){
            Toast.makeText(RegisterActivity.this,e.getMessage(),
                    Toast.LENGTH_LONG).show();
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

    private void GoToLoginActivity(){
        onBackPressed();
    }

    public void ButtonCancelClick(View view){
        GoToLoginActivity();
    }
}