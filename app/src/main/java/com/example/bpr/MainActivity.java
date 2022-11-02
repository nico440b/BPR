package com.example.bpr;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private String email,password;
    private ActivityMainBinding binding;
    public static Context context;
    private NetworkImpl network = new NetworkImpl();
    private CoopStoreCore core = new CoopStoreCore();
    private Converters converters = new Converters();

    private List<CoopProducts> coopProducts;
    private CoopProductsViewModel coopProductsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        Button loginBtn = findViewById(R.id.loginBtn);
        EditText mail = findViewById(R.id.mailField);
        EditText pw = findViewById(R.id.pwField);

        loginBtn.setOnClickListener(new View.OnClickListener() {
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

        context = getApplicationContext();
        //core = network.CoopStoreAPI();

        network.CoopProductsAPI(new VolleyCallBack() {
            @Override
            public void onSuccess(List<CoopProducts> result) {
                coopProducts = result;
                coopProductsViewModel.insertAll(coopProducts);
                LiveData<List<CoopProducts>> test;
                test = coopProductsViewModel.getProducts();
                Log.e("Rest Respone", test.getValue().get(0).navn);


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
    private void signIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user){
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_offers, R.id.navigation_search, R.id.navigation_list, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);
    }
}