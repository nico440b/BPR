package com.example.bpr.ui.login.ui.login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bpr.Converters;
import com.example.bpr.MainActivity;
import com.example.bpr.NetworkImpl;
import com.example.bpr.Objects.CoopStoreCore;
import com.example.bpr.R;

import com.example.bpr.ui.MainFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String email,password, name, uID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dataB = FirebaseFirestore.getInstance();
    public FirebaseUser user;
    private ProgressBar pBar;


    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        pBar = view.findViewById(R.id.pBarLog);
        pBar.setVisibility(View.INVISIBLE);

        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();


        Button loginBtn = view.findViewById(R.id.loginBtn);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText mail = view.findViewById(R.id.mailField);
        EditText pw = view.findViewById(R.id.pwField);
        TextView signUp = view.findViewById(R.id.signupText);

        FragmentContainerView fragmentContainerView = view.findViewById(R.id.fragCV);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBar.setVisibility(View.VISIBLE);
                email = mail.getText().toString();
                password = pw.getText().toString();
                if (email.equals("")||password.equals("")){
                    Toast.makeText(getContext(),"Fill In All The Fields",Toast.LENGTH_LONG).show();
                }
                else {
                    signIn(email,password);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragCV,SignUpFragment.newInstance("","")).commit();

            }
        });

        return view;
    }

    private void signIn(String e,String p) {

        mAuth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            getParentFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragCV, ProfileSelectorFragment.newInstance(user.getUid(),""))
                                    .commit();


                        } else {
                            Toast.makeText(getContext(), "Log In Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}