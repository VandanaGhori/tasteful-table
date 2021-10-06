package com.example.tastefultable;

import android.app.Activity;
import android.app.AlertDialog;
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

import java.util.Objects;

import PreferencesManager.PreferencesManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private static final int PASSWORD_LENGTH_CONSTRAINT = 6;
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView linkTextView;
    TextInputEditText mTextViewEmail, mTextViewPassword;
    AppCompatButton mLoginButton;
    LinearLayout parentLayout;

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

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initialization(view);

        // When we touch outside, it will close the keyboard
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
            }
        });

        mTextViewEmail.setOnFocusChangeListener(editFocusChangeListener);
        mTextViewPassword.setOnFocusChangeListener(editFocusChangeListener);

        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MyAccountFragment());
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();

                String msg = validateUserInput();
                if (msg.trim().length() != 0) {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                    return;
                }

                String email = mTextViewEmail.getText().toString().trim();
                String password = mTextViewPassword.getText().toString().trim();
                // For Registering user data into database
                userLogin(email,password);
            }
        });

        return view;
    }

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void userLogin(String email, String password) {
        Log.i("INSIDE LOGIN", "userListLogin: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tastefultable.000webhostapp.com/tastefulTable/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitObjectPreparationsAPI service = retrofit.create(RetrofitObjectPreparationsAPI.class);

        // At the time of Requesting API pass userList's detail in body for POST Request.
        Call<GeneralApiResponse<User>> repos = service.userLogin(email, password);

        repos.enqueue(new Callback<GeneralApiResponse<User>>() {
            @Override
            public void onResponse(Call<GeneralApiResponse<User>> call, Response<GeneralApiResponse<User>> response) {
                Log.i("INSIDE ON RESPONSE", "onResponse: " + response.toString());

                GeneralApiResponse<User> apiResponse = response.body();
                Log.i("apiResponse", "onResponse: " + apiResponse);
                if(apiResponse == null) { return; }

                if(!apiResponse.isSuccess() && apiResponse.getError_code() == 401 &&
                        apiResponse.getData() == null && getActivity()!=null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle(getString(R.string.alert_title))
                            .setMessage(getActivity().getString(R.string.user_not_registered))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    loadFragment(new MyAccountFragment());
                                }
                            }).show();
                }

                if(apiResponse.isSuccess() && apiResponse.getError_code() == 200 && apiResponse.getData() != null) {
                    User user = (User) apiResponse.getData();
                    //Log.i("USER", "onResponse: LoggedIn User" + user.toString());
                    showLoggedInUser(user);

                    // Open MainActivity after successfully logged in
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<GeneralApiResponse<User>> call, Throwable t) {
                Log.i("apiFailure", "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void showLoggedInUser(User user) {
        String email = user.getEmail();
        int user_id = (user.getId());
        //Log.i("LOGGED IN USER", "showLoggedInUser: " + user);
        //Log.i("LOGGED IN USER", "showLoggedInUser: " + user.getId());
        //Log.i("LOGGED IN USER", "showLoggedInUser: " + user.getToken());

        //PreferencesManager preferencesManagerInstance = PreferencesManager.getInstance();
        //SharedPreferences sharedPreferences = PreferencesManager.getSharedPreferences(getContext());
        PreferencesManager.putString(getContext(),"Email",email);
        PreferencesManager.putString(getContext(),"Id",String.valueOf(user_id));
        PreferencesManager.putString(getContext(),"Token",user.getToken());

        //Log.i("USER", "showLoggedInUser: " + user.getEmail());

        // Store the email into sharedPreference after registration get successful
        /*SharedPreferences prefsUserEmail = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefsUserEmail.edit();
        editor.putString("Email",email);
        editor.apply();*/
    }

    private String validateUserInput() {
        String email = Objects.requireNonNull(mTextViewEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(mTextViewPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            return getContext().getString(R.string.validate_all_registrations_inputs);
        }

        if (!email.matches(EMAIL_PATTERN)) {
            return getContext().getString(R.string.email_constraint);
        }

        if (password.length() < PASSWORD_LENGTH_CONSTRAINT) {
            return getContext().getString(R.string.password_length_constraint);
        }

        return "";
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

    private void initialization(View view) {
        mTextViewEmail = (TextInputEditText) view.findViewById(R.id.textViewEmail);
        mTextViewPassword = (TextInputEditText) view.findViewById(R.id.textViewPassword);
        linkTextView = (TextView) view.findViewById(R.id.activity_link);
        mLoginButton = (AppCompatButton) view.findViewById(R.id.loginButton);
        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);

        // Set underline dynamically for link
        linkTextView.setPaintFlags(linkTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if(getContext() == null) { return; }
            linkTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    private void loadFragment(Fragment fragment) {
        // load new Fragment
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}