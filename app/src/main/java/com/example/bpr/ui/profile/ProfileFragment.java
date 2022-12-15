package com.example.bpr.ui.profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bpr.Adapters.ProfileAdapter;
import com.example.bpr.Objects.Profile;
import com.example.bpr.R;
import com.example.bpr.ui.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements ProfileAdapter.OnProfileListener {


    private ProfileAdapter adapter;
    private FirebaseAuth mAuth;
    private String profile, profileID, pName;
    private ArrayList<Profile> profiles = new ArrayList<>();
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText profileName;
    private TextView profileIndicator;
    private FloatingActionButton createBtn;
    private Button addBtn;
    private String pID;
    private ProgressBar pBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile = MainFragment.profileName;
        mAuth = FirebaseAuth.getInstance();
        profileID = MainFragment.profileID;

        createBtn = view.findViewById(R.id.addProfileBtn2);

        profileIndicator = view.findViewById(R.id.profileIDIndicator);
        pBar = view.findViewById(R.id.pBarProfile);
        FloatingActionButton btn = view.findViewById(R.id.floatingActionButton);
        recyclerView = view.findViewById(R.id.profileRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ProfileAdapter(getContext(),profiles,this);
        recyclerView.setAdapter(adapter);

        pBar.setVisibility(View.VISIBLE);
        getProfiles();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewProfileDialog();
            }
        });

        updateView();
        profileIndicator.setText("Currently signed in as: " + MainFragment.profileName);
        pBar.setVisibility(View.INVISIBLE);

        return view;
    }

    public void updateView(){
        adapter = new ProfileAdapter(getContext(),profiles,this);
        recyclerView.setAdapter(adapter);

    }

    public void getProfiles(){
        dataB.collection("Users").document(mAuth.getUid()).collection("Profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        // profiles.add(document.getId());
                        Profile profile = new Profile(document.getString("Profile"),document.getId());
                        profiles.add(profile);
                        updateView();

                    }
                } else {
                    task.getException();
                }
            }
        });
    }

    public void updateProfiles(){
        dataB.collection("Users").document(mAuth.getUid()).collection("Profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    profiles.clear();
                    for(QueryDocumentSnapshot document : task.getResult()){

                            Profile profile = new Profile(document.getString("Profile"),document.getId());
                            profiles.add(profile);

                    }
                    updateView();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onProfileClick(int position) {
        MainFragment.profileID = profiles.get(position).getID();
        MainFragment.profileName = profiles.get(position).getName();
        profileIndicator.setText("Currently signed in as: " + MainFragment.profileName);
    }

    public void createNewProfileDialog(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View profilePopUp = getLayoutInflater().inflate(R.layout.profile_popup,null);
        profileName = profilePopUp.findViewById(R.id.createProfileTxt);
        addBtn = profilePopUp.findViewById(R.id.createProfileBtn);
        dialogBuilder.setView(profilePopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = profileName.getText().toString();
                Map<String, Object> uPData = new HashMap<>();
                uPData.put("Profile",uName);
                CollectionReference profileRef = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(mAuth.getUid())
                        .collection("Profiles");
                profileRef.add(uPData);
                pID = profileRef.document().getId();
                pName = uName;

                MainFragment.profileName = pName;
                MainFragment.profileID = pID;
                profileIndicator.setText("Currently signed in as:"+ MainFragment.profileName);
                updateProfiles();
                updateView();
                adapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });
    }

}