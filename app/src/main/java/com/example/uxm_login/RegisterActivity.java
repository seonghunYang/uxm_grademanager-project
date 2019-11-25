package com.example.uxm_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final String TAG="RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance(); //초기화

        Button sign_Button = findViewById(R.id.btn_Signup_Register);
        sign_Button.setOnClickListener(onClickListener2);
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener2=new View.OnClickListener(){
        @Override
        public void onClick(View v){

            String email = ((EditText)findViewById(R.id.enter_id)).getText().toString();
            String password = ((EditText)findViewById(R.id.enter_password)).getText().toString();
            String name = ((EditText)findViewById(R.id.enter_name)).getText().toString();
            String student_id = ((EditText)findViewById(R.id.enter_student_id)).getText().toString();

            String err = validateForm(email, password, name, student_id);
            if(err == null) {
                register(email, password);
            }
            else{
                Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
            }

        }
    };
    public void dbregister(FirebaseUser user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String email = ((EditText)findViewById(R.id.enter_id)).getText().toString();
        String password = ((EditText)findViewById(R.id.enter_password)).getText().toString();
        String name = ((EditText)findViewById(R.id.enter_name)).getText().toString();
        String student_id = ((EditText)findViewById(R.id.enter_student_id)).getText().toString();

        String hash_password = sha256(password);

        user registUser = new user(email, hash_password, name, student_id);

        db.collection("users").document(user.getUid())
                .set(registUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Toast.makeText(getApplicationContext(), "회원가입에 성공하셨습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "회원가입에 실패하셨습니다.", Toast.LENGTH_LONG).show();

                    }
                });
    }


    public void register(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                dbregister(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "회원가입에 실패하셨습니다.", Toast.LENGTH_LONG).show();
                            }

                    }
                    });

                        // ...
                    }

    public String validateForm(String email, String password, String name,String student_id){
        if (email.equals("")) {
            return "이메일을 입력해주세요.";
        }
        if (password.equals("")) {
            return "비밀번호를 입력해주세요.";
        }
        if (name.equals("")) {
            return "이름을 입력해 주세요";
        }
        if (student_id.equals("")) {
            return "학번을 입력해주세요.";
        }
        if (email.contains("@")==false) {
            return "이메일 형식이 아닙니다" ;
        }
        if (password.trim().length()<6){
            return "6자리 이상 입력해주세요";
        }
        boolean isEnglish = false;
        for(int i=0; i<password.length();i++){
            char a= password.toUpperCase().charAt(i);
            if(a > 'A' && a< 'Z'){
                isEnglish = true ;
            }
            }
        if (isEnglish == false){
            return "비밀번호 양식이 틀렸습니다." ;
        }
        if (student_id.length() != 8) {
            return "학번 양식이 틀렸습니다";
        }
        return null;
    };

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    }



