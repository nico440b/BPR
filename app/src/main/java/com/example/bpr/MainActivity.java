package com.example.bpr;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bpr.MVVM.CoopProducts.CoopProductsViewModel;
import com.example.bpr.MVVM.CoopStores.CoopStoresViewModel;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.CoopStore;
import com.example.bpr.Objects.CoopStoreCore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import com.example.bpr.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String email,password;
    private ActivityMainBinding binding;
    public static Context context;
    private NetworkImpl network = new NetworkImpl();

    private List<CoopProducts> coopProducts;
    private LiveData<List<CoopProducts>> liveDataProducts;
    private CoopProductsViewModel coopProductsViewModel;
    private CoopStoresViewModel coopStoresViewModel;

    //GPS PERMISSIONS
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    //Alarm
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    BroadcastReceiver broadcastReceiver;
    Calendar calendar;

    //Location
    private final static int ALL_PERMISSIONS_RESULT = 101;
    public LocationTrack locationTrack;
    double latitude = 0;
    double longitude = 0;
    //Radius is in meters
    public int radius = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        Button loginBtn = findViewById(R.id.loginBtn);
        EditText mail = findViewById(R.id.mailField);
        EditText pw = findViewById(R.id.pwField);
        coopStoresViewModel = ViewModelProviders.of(this).get(CoopStoresViewModel.class);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }



        context = getApplicationContext();
        //core = network.CoopStoreAPI();

        locationTrack = new LocationTrack(MainActivity.this);


        if (locationTrack.canGetLocation()) {


            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();

            Log.e("Rest Respone", longitude + " " + latitude);
            addCoopProducts(latitude,longitude);

             } else {

            locationTrack.showSettingsAlert();
            }



        coopProductsViewModel = ViewModelProviders.of(this).get(CoopProductsViewModel.class);


        coopProductsViewModel.getProducts().observe(this, new Observer<List<CoopProducts>>() {
            @Override
            public void onChanged(List<CoopProducts> coopProducts) {

                liveDataProducts = coopProductsViewModel.getProducts();
                //Log.e("Rest Respone", liveDataProducts.getValue().get(0).navn);
            }
        });



        //Alarm
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        long intendedTime = calendar.getTimeInMillis();

        registerMyAlarmBroadcast();
        alarmManager.set(AlarmManager.RTC_WAKEUP,intendedTime,pendingIntent);


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
    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    public void addCoopProducts(double latitude,double longitude)
    {
        network.getCoopStores(latitude,longitude,radius,new VolleyCallBackStores() {
            @Override
            public void onSuccesStores(List<CoopStore> stores) {
                List<CoopStore> list = stores;
                coopStoresViewModel.insertAll(list);
                LiveData<List<CoopStore>> coopStores;

                coopStores = coopStoresViewModel.getAll();
                for (int i=0;i<list.size();i++) {
                    network.getCoopProducts(list.get(i).name,list.get(i).kardex,list.get(i).location,new VolleyCallBack() {
                        @Override
                        public void onSuccessProducts(List<CoopProducts> result) {

                            coopProducts = result;
                            //Log.e("Length to product", String.valueOf(locationTrack.loc.distanceTo(coopProducts.get(1).getLocation())/1000000) + " KM");
                            coopProductsViewModel.insertAll(coopProducts);




                        }

                    });
                }

            }


        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : (ArrayList<String>) permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
        unregisterReceiver(broadcastReceiver);
    }

    private void registerMyAlarmBroadcast()
    {
        Log.i("MainActivity", "Going to register Intent.RegisterAlramBroadcast");

        //This is the call back function(BroadcastReceiver) which will be call when your
        //alarm time will reached.
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                addCoopProducts(latitude,longitude);
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter("com.alarm.example") );
        pendingIntent = PendingIntent.getBroadcast( this, 0, new Intent("com.alarm.example"),0 );
        alarmManager = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }
    private void UnregisterAlarmBroadcast()
    {
        alarmManager.cancel(pendingIntent);
        getBaseContext().unregisterReceiver(broadcastReceiver);
    }



}