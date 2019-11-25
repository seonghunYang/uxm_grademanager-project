package com.example.uxm_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddinfoActivity extends AppCompatActivity {
    private  FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addinfo);
        mAuth=FirebaseAuth.getInstance();
        findViewById(R.id.btn_addInfo).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_addInfo:
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String name = ((EditText)findViewById(R.id.addInfo_Name)).getText().toString();
                    String student_id = ((EditText)findViewById(R.id.addInfo_student_id)).getText().toString();
                    String err = addInfo_validationForm(name, student_id);
                    if(err != null){
                        Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                    }
                    else{
                        FirebaseUser user = mAuth.getCurrentUser();
                        String email = user.getEmail();
                        user addInfo = new user(email, "google", name, student_id);
                        db.collection("users").document(user.getUid())
                                .set(addInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void avoid) {
                                        Toast.makeText(getApplicationContext(), "회원가입에 성공하셨습니다.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(AddinfoActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "회원가입에 실패하셨습니다.", Toast.LENGTH_LONG).show();

                                    }
                                });
                    }
            }
        }
    };
    public String addInfo_validationForm(String name, String student_id){
        if (name.equals("")) {
            return "이름을 입력해 주세요";
        }
        if (student_id.equals("")) {
            return "학번을 입력해주세요.";
        }
        if (student_id.length() != 8) {
            return "학번 양식이 틀렸습니다";
        }
        return null;
    }

}
