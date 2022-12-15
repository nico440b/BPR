package com.example.bpr.ui.login.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bpr.Converters;
import com.example.bpr.MainActivity;
import com.example.bpr.NetworkImpl;
import com.example.bpr.Objects.CoopStoreCore;
import com.example.bpr.R;

import com.example.bpr.ui.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private String email,password, name;
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Button signUpBtn = view.findViewById(R.id.signUpBtn);
        EditText sMail = view.findViewById(R.id.emailSignUp);
        EditText sPw = view.findViewById(R.id.pwSignUp);
        EditText profileName = view.findViewById(R.id.nameSignUp);
        FirebaseApp.initializeApp(getActivity());
        mAuth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = sMail.getText().toString();
                password = sPw.getText().toString();
                name = profileName.getText().toString();
                if (email.equals("")||password.equals("")){
                    Toast.makeText(getActivity(),"Fill In All The Fields",Toast.LENGTH_LONG).show();
                }
                else {
                    signUp(email,password,name);
                }
            }
        });

        return view;
    }

    private void signUp (String email, String password, String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
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

                            signIn(name, password);
                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragCV, ProfileSelectorFragment.newInstance(user.getUid(),""))
                                    .commit();
                        } else {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String e,String p) {

        mAuth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                        } else {

                        }
                    }
                });
    }
}