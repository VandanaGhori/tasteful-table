package com.example.tastefultable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastefultable.model.GeneralApiResponse;
import com.example.tastefultable.model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private static final int PASSWORD_LENGTH_CONSTRAINT = 6;
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputEditText mNameTextView, mEmailTextView, mPasswordTextView, mConfirmPasswordTextView;
    TextView myLoginLink;
    AppCompatButton mRegisterButton;
    LinearLayout parentLayout;

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

        myLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LoginFragment());
            }
        });

        // When we touch outside, it will close the keyboard
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("OnClick Parent","----");
                closeKeyboard();
            }
        });

        mNameTextView.setOnFocusChangeListener(editFocusChangeListener);
        mEmailTextView.setOnFocusChangeListener(editFocusChangeListener);
        mPasswordTextView.setOnFocusChangeListener(editFocusChangeListener);
        mConfirmPasswordTextView.setOnFocusChangeListener(editFocusChangeListener);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();

                String msg = validateUserInput();
                if (msg.trim().length() != 0) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    return;
                }

                Log.i("ON REGISTER BUTTON", "onClick: ");
                // For Registering userList data into database
                String name = Objects.requireNonNull(mNameTextView.getText()).toString().trim();
                String email = Objects.requireNonNull(mEmailTextView.getText()).toString().trim();
                String password = Objects.requireNonNull(mPasswordTextView.getText()).toString().trim();
                userRegistration(name, email, password);
            }
        });

        /*Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);*/

        // Inflate the layout for this fragment
        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void userRegistration(String name, String email, String password) {
        //Log.i("INSIDE REGISTRATION", "userListRegistration: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tastefultable.000webhostapp.com/tastefulTable/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitObjectPreparationsAPI service = retrofit.create(RetrofitObjectPreparationsAPI.class);

        // At the time of Requesting API pass userList's detail in body for POST Request.
        Call<GeneralApiResponse<User>> repos = service.userRegistration(name, email, password);

        repos.enqueue(new Callback<GeneralApiResponse<User>>() {

            @Override
            public void onResponse(Call<GeneralApiResponse<User>> call, Response<GeneralApiResponse<User>> response) {
                //Log.i("INSIDE ON RESPONSE", "onResponse: ");

                GeneralApiResponse<User> apiResponse = response.body();
                //Log.i("apiResponse", "onResponse: " + apiResponse);
                if(apiResponse == null) { return; }

                if(!apiResponse.isSuccess() && apiResponse.getError_code() == 409 &&
                        apiResponse.getData() == null && getActivity()!=null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.alert_title))
                            .setMessage(getActivity().getString(R.string.user_already_registered))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loadFragment(new LoginFragment());  // if user is registered already then redirect for login
                                }
                            }).show();
                }

                else if(apiResponse.isSuccess() && apiResponse.getError_code() == 200 && apiResponse.getData() != null) {
                    User user = (User) apiResponse.getData();
                    //Log.i("Registered USER:", "onResponse: User" + user.toString());
                    showRegisteredUser(user);
                    loadFragment(new LoginFragment());
                }
            }

            @Override
            public void onFailure(Call<GeneralApiResponse<User>> call, Throwable t) {
                Log.i("apiFailure", "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void showRegisteredUser(User user) {
        if (user == null) {
            //Log.i("------", "user list is null: ");
            return;
        }
        //Log.i("------", "showRegisteredUser: ");
        String email = user.getEmail();
        //Toast.makeText(getContext(), "Welcome " + email, Toast.LENGTH_LONG).show();
        // Store user object and sessionID means token into the sharedPreference
        // Create a new token, store that token and pass it with user object
        // check it with every authenticated API's activity
        Log.i("------", "Email: " + email);

        // Store the email into sharedPreference after registration get successful
        SharedPreferences prefsUserEmail = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefsUserEmail.edit();
        editor.putString("Email",email);
        editor.apply();
    }

    // When we create variable of type interface we need to implement it's method, // variable created for interface implementation
    private final View.OnFocusChangeListener editFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                closeKeyboard();
            }
        }
    };

    private void closeKeyboard() {
        if(getActivity() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initialization(View view) {
        mNameTextView = (TextInputEditText) view.findViewById(R.id.textViewName);
        mEmailTextView = (TextInputEditText) view.findViewById(R.id.textViewEmail);
        mPasswordTextView = (TextInputEditText) view.findViewById(R.id.textViewPassword);
        mConfirmPasswordTextView = (TextInputEditText) view.findViewById(R.id.textViewConfirmPassword);
        myLoginLink = (TextView) view.findViewById(R.id.activity_link);
        mRegisterButton = (AppCompatButton) view.findViewById(R.id.registerButton);
        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);

        // Set underline dynamically for link
        myLoginLink.setPaintFlags(myLoginLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if(getContext() == null) return;
        myLoginLink.setTextColor(ContextCompat.getColor(getContext(), R.color.teal_700));
    }

    private String validateUserInput() {
        String name = Objects.requireNonNull(mNameTextView.getText()).toString().trim();
        String email = Objects.requireNonNull(mEmailTextView.getText()).toString().trim();
        String password = Objects.requireNonNull(mPasswordTextView.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(mConfirmPasswordTextView.getText()).toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            return getContext().getString(R.string.validate_all_registrations_inputs);
        }

        if (!(email.matches(EMAIL_PATTERN))) {
            return getContext().getString(R.string.email_constraint);
        }

        if (password.length() < PASSWORD_LENGTH_CONSTRAINT) {
            return getContext().getString(R.string.password_length_constraint);
        }

        if (confirmPassword.length() < PASSWORD_LENGTH_CONSTRAINT) {
            return getContext().getString(R.string.confirm_password_length_constraint);
        }

        if (!(password.equals(confirmPassword))) {
            return getContext().getString(R.string.password_mismatch);
        }

        return "";
    }
}