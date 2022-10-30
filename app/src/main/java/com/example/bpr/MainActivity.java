package com.example.bpr;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.bpr.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static Context context;
    private NetworkImpl network = new NetworkImpl();
    private CoopStoreCore core = new CoopStoreCore();
    private Converters converters = new Converters();
    private List<CoopProducts> coopProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        //core = network.CoopStoreAPI();
        coopProducts = network.CoopProductsAPI();
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "databasetest").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        CoopStoreDao coopStoreDao = db.coopStoreDao();
        CoopProductsDao coopProductsDao = db.coopProductsDao();
        /*
        coopStoreDao.insertAll(core);
        CoopStoreCore testCore = new CoopStoreCore();
        testCore = coopStoreDao.getAll();
        Log.e("Rest Respone", "testCore.apiVersion");
        Log.e("Rest Respone", testCore.apiVersion);

         */
        coopProductsDao.insertAll(coopProducts);
        List<CoopProducts> test;

        test = coopProductsDao.getAll();
        //Log.e("Rest Respone", test.get(0).navn);







        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_offers, R.id.navigation_search, R.id.navigation_list, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }


}