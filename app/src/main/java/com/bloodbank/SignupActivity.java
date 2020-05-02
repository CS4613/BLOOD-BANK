package com.bloodbank;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputDob,inputName,inputConfirmPassword,inputAddress,inputZipCode,inputMobileNo;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private RadioGroup genderGroup;
    private RadioButton genderButton;
    private CheckBox donorCheck;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Spinner spinnerChooseBloodGroup;

    private FirebaseFirestore firebaseFirestore;
    private String userId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign Up");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputName = findViewById(R.id.name);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfirmPassword = findViewById(R.id.confirm_password);
        inputAddress = findViewById(R.id.address);
        inputZipCode = findViewById(R.id.zip_code);
        inputMobileNo = findViewById(R.id.mobile_no);
        inputDob = findViewById(R.id.dob);
        genderGroup = findViewById(R.id.radioGenderGroup);
        donorCheck = findViewById(R.id.donor);
        progressBar = findViewById(R.id.progressBar);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        spinnerChooseBloodGroup = findViewById(R.id.spinner_choose_bloodgroup_signup);
        ArrayAdapter<String> bloodGroupArrayAdapter = new ArrayAdapter<>(SignupActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.blood_groups));
        bloodGroupArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerChooseBloodGroup.setAdapter(bloodGroupArrayAdapter);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = inputName.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmPassword = inputConfirmPassword.getText().toString().trim();
                final String address = inputAddress.getText().toString().trim();
                final String zipCode = inputZipCode.getText().toString().trim();
                final String mobileNo= inputMobileNo.getText().toString().trim();
                final String dob = inputDob.getText().toString().trim();
                final String bloodGroup = spinnerChooseBloodGroup.getSelectedItem().toString();
                int selectedId = genderGroup.getCheckedRadioButtonId();
                genderButton = findViewById(selectedId);
                String gender = null;
                if (genderButton!=null) {
                    gender = genderButton.getText().toString();
                }
                final boolean isDonor = donorCheck.isChecked();

                if (TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    inputEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    inputEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    inputPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    inputPassword.requestFocus();
                    return;
                }

                if (!confirmPassword.equals(password)) {
                    Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                    inputConfirmPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Enter address!", Toast.LENGTH_SHORT).show();
                    inputAddress.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(zipCode)) {
                    Toast.makeText(getApplicationContext(), "Enter zipcode!", Toast.LENGTH_SHORT).show();
                    inputZipCode.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(mobileNo)) {
                    Toast.makeText(getApplicationContext(), "Enter mobile no!", Toast.LENGTH_SHORT).show();
                    inputMobileNo.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(dob)) {
                    Toast.makeText(getApplicationContext(), "Enter date of birth!", Toast.LENGTH_SHORT).show();
                    inputDob.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(bloodGroup) || bloodGroup.equals("Choose blood group")){
                    Toast.makeText(getApplicationContext(), "Choose blood group!", Toast.LENGTH_SHORT).show();
                    spinnerChooseBloodGroup.requestFocus();
                    return;
                }

                if (gender==null){
                    Toast.makeText(getApplicationContext(), "Choose gender!", Toast.LENGTH_SHORT).show();
                    genderGroup.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                final String finalGender = gender;
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Map<String,Object> user = new HashMap<>();
                                    user.put("name",name);
                                    user.put("email",email);
                                    user.put("address",address);
                                    user.put("zipCode",zipCode);
                                    user.put("mobileNo",mobileNo);
                                    user.put("dob",dob);
                                    user.put("bloodGroup",bloodGroup);
                                    user.put("gender", finalGender);
                                    user.put("isDonor",isDonor);

                                    userId = auth.getCurrentUser().getUid();
                                    DocumentReference documentReference = firebaseFirestore.collection("users").document(userId);
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Toast.makeText(SignupActivity.this, "successfully inserted data", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    DocumentReference bloodGroupReference = firebaseFirestore.collection("blood_groups").document("groups");
                                    bloodGroupReference.update(bloodGroup, FieldValue.increment(1));

                                    DocumentReference zipcodeReference = firebaseFirestore.collection("zip_code").document(zipCode).collection(bloodGroup).document(userId);
                                    zipcodeReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Toast.makeText(SignupActivity.this, "successfully inserted data", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });




            }
        });

        Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                inputDob.setText(year+"/"+(month+1)+"/"+dayOfMonth);
            }
        };

        inputDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this,date, mYear,mMonth,mDay).show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
