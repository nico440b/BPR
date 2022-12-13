package com.example.bpr.ui.login.ui.login;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.bpr.Adapters.ProfileAdapter;
import com.example.bpr.Objects.Profile;
import com.example.bpr.R;
import com.example.bpr.ui.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileSelectorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileSelectorFragment extends Fragment implements ProfileAdapter.OnProfileListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ArrayList<Profile> profiles = new ArrayList<>();

    private ProfileAdapter adapter;
    private String uID;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    public FirebaseUser user;
    private RecyclerView recyclerView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText profileName;
    private Button createBtn;
    private String pID, pName;
    private ProgressBar pBar;

    public ProfileSelectorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileSelectorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileSelectorFragment newInstance(String param1, String param2) {
        ProfileSelectorFragment fragment = new ProfileSelectorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_selector, container, false);

        uID = mParam1;
        mAuth = FirebaseAuth.getInstance();

        pBar = view.findViewById(R.id.pBarProfileSelector);

        //TextView text = view.findViewById(R.id.ptext);
        FloatingActionButton btn = view.findViewById(R.id.floatingActionButton);
        recyclerView = view.findViewById(R.id.profileRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ProfileAdapter(getContext(),profiles,this);
        recyclerView.setAdapter(adapter);

        pBar.setVisibility(View.VISIBLE);
        getProfiles();

        updateView();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewProfileDialog();
            }
        });

        pBar.setVisibility(View.INVISIBLE);
        return view;

    }

    public void updateView(){
        adapter = new ProfileAdapter(getContext(),profiles,this);
        recyclerView.setAdapter(adapter);

    }

    public void getProfiles(){
        dataB.collection("Users").document(uID).collection("Profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        dataB.collection("Users").document(uID).collection("Profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        if (document.getId().equals(pID)){
                            Profile profile = new Profile(document.getString("Profile"),document.getId());
                            profiles.add(profile);
                            updateView();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onProfileClick(int position) {
        getParentFragmentManager().beginTransaction().replace(R.id.fragCV, MainFragment.newInstance(profiles.get(position).getName(),profiles.get(position).getID())).commit();
    }

    public void createNewProfileDialog(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View profilePopUp = getLayoutInflater().inflate(R.layout.profile_popup,null);
        profileName = profilePopUp.findViewById(R.id.createProfileTxt);
        createBtn = profilePopUp.findViewById(R.id.createProfileBtn);
        dialogBuilder.setView(profilePopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = profileName.getText().toString();
                pName = uName;
                Map<String, Object> uPData = new HashMap<>();
                uPData.put("Profile",uName);
                CollectionReference profileRef = FirebaseFirestore.getInstance().collection("Users").document(mAuth.getUid()).collection("Profiles");
                profileRef.add(uPData);
                pID = profileRef.document().getId();


                dialog.cancel();
                getParentFragmentManager().beginTransaction().replace(R.id.fragCV, MainFragment.newInstance(pName, pID)).commit();

            }
        });
    }
}