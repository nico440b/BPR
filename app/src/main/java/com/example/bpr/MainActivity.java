package com.example.bpr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bpr.MVVM.CoopProductsViewModel;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.CoopStoreCore;
import com.example.bpr.ui.login.ui.login.ProfileSelectorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.bpr.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email,password, name;
    private ActivityMainBinding binding;
    public static Context context;
    private NetworkImpl network = new NetworkImpl();
    private CoopStoreCore core = new CoopStoreCore();
    private Converters converters = new Converters();
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    public FirebaseUser currentUser;

    private List<CoopProducts> coopProducts;
    private CoopProductsViewModel coopProductsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        Button loginBtn = findViewById(R.id.loginBtn);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText mail = findViewById(R.id.mailField);
        EditText pw = findViewById(R.id.pwField);
        TextView signUp = findViewById(R.id.signupText);




        /*loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mail.getText().toString();
                password = pw.getText().toString();
                if (email.equals("")||password.equals("")){
                    Toast.makeText(MainActivity.this,"Fill In All The Fields",Toast.LENGTH_LONG).show();
                }
                else {
                    signIn(email,password);
                }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.fragment_sign_up);
                Button signUpBtn = findViewById(R.id.signUpBtn);
                EditText sMail = findViewById(R.id.emailSignUp);
                EditText sPw = findViewById(R.id.pwSignUp);
                EditText profileName = findViewById(R.id.nameSignUp);
                signUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email = sMail.getText().toString();
                        password = sPw.getText().toString();
                        name = profileName.getText().toString();
                        if (email.equals("")||password.equals("")){
                            Toast.makeText(MainActivity.this,"Fill In All The Fields",Toast.LENGTH_LONG).show();
                        }
                        else {
                            signUp(email,password,name);
                        }
                    }
                });
            }
        });*/



        context = getApplicationContext();

        network.getCoopProducts(new VolleyCallBack() {
            @Override
            public void onSuccess(List<CoopProducts> result) {
                coopProducts = result;
                coopProductsViewModel.insertAll(coopProducts);
                LiveData<List<CoopProducts>> test;
                test = coopProductsViewModel.getProducts();
            }
        });
        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);
        coopProductsViewModel.getProducts().observe(this, new Observer<List<CoopProducts>>() {
            @Override
            public void onChanged(List<CoopProducts> coopProducts) {
                Toast.makeText(MainActivity.this, "Changed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void signUp (String email, String password, String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                             String uMail = user.getEmail();
                             String uID = user.getUid();
                             String uName = name;

                            Map<String, Object> uData = new HashMap<>();
                            uData.put("Email", uMail);

                            Map<String, Object> uPData = new HashMap<>();
                            uPData.put("Profile",uName);

                            db.collection("Users").document(uID).set(uData);
                            db.collection("Users").document(uID).collection("Profiles").document().set(uPData);


                            setContentView(R.layout.activity_main);
                        } else {


                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }



    public FirebaseUser getCurrentUser(){
        return currentUser;
    }


}