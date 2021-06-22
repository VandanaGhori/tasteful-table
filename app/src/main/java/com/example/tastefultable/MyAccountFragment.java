package com.example.tastefultable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.service.autofill.RegexValidator;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView linkTextView;
    TextView mNameTextView, mEmailTextView, mPasswordTextView, mConfirmPasswordTextView;
    AppCompatButton mRegisterButton;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyAccountFragment newInstance(String param1, String param2) {
        MyAccountFragment fragment = new MyAccountFragment();
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
        View view;
        view = inflater.inflate(R.layout.fragment_my_account, container, false);

        initialization(view);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateUserInput()){

                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void initialization(View view) {
        mNameTextView = (TextView) view.findViewById(R.id.textViewName);
        mEmailTextView = (TextView) view.findViewById(R.id.textViewEmail);
        mPasswordTextView = (TextView) view.findViewById(R.id.textViewPassword);
        mConfirmPasswordTextView = (TextView) view.findViewById(R.id.textViewConfirmPassword);
        mRegisterButton = (AppCompatButton) view.findViewById(R.id.registerButton);
    }

    private boolean validateUserInput() {
        String msg = "";
        String name = mNameTextView.getText().toString().trim();
        String email = mNameTextView.getText().toString().trim();
        String password = mNameTextView.getText().toString().trim();
        String confirmPassword = mConfirmPasswordTextView.getText().toString().trim();

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            msg = "Please fill all fields correctly!";
            Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
            return false;
        }

        if(password.length() < 6) {
            msg = "Password should contain at least 6 characters!";
            Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
            return false;
        }

        if(!password.equals(confirmPassword)) {
            msg = "Password and confirm password should be matched!";
            Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            msg = "Please enter proper email address!";
            Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}