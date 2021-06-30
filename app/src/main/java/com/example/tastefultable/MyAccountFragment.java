package com.example.tastefultable;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastefultable.model.ApiResponse;
import com.example.tastefultable.model.Preparations;
import com.example.tastefultable.model.User;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

    TextView mNameTextView, mEmailTextView, mPasswordTextView, mConfirmPasswordTextView;
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
                }

                // For Registering user data into database
                userRegistration(mNameTextView.getText().toString().trim(),
                        mEmailTextView.getText().toString().trim(),
                        mPasswordTextView.getText().toString().trim());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void userRegistration(String name, String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://tastefultable.000webhostapp.com/tastefulTable/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitObjectPreparationsAPI service = retrofit.create(RetrofitObjectPreparationsAPI.class);

        // At the time of Requesting API pass user's detail in body for POST Request.
        Call<List<User>> repos = service.userRegistration(name,email,password);

        repos.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(!response.isSuccessful()) { return; }

                List<User> userList = response.body();

                if(userList.size() != 0) {
                    String email = userList.get(0).getEmail();
                    Toast.makeText(getContext(), "Welcome " + email, Toast.LENGTH_LONG).show();
                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("Failure", "onFailure: " + t.getMessage());
            }
        });
    }

    // When we create variable of type interface we need to implement it's method, // variable created for interface implementation
    private View.OnFocusChangeListener editFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus) {
                closeKeyboard();
            }
        }
    };

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void initialization(View view) {
        mNameTextView = (TextView) view.findViewById(R.id.textViewName);
        mEmailTextView = (TextView) view.findViewById(R.id.textViewEmail);
        mPasswordTextView = (TextView) view.findViewById(R.id.textViewPassword);
        mConfirmPasswordTextView = (TextView) view.findViewById(R.id.textViewConfirmPassword);
        mRegisterButton = (AppCompatButton) view.findViewById(R.id.registerButton);
        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);
    }

    private String validateUserInput() {
        String name = mNameTextView.getText().toString().trim();
        String email = mEmailTextView.getText().toString().trim();
        String password = mPasswordTextView.getText().toString().trim();
        String confirmPassword = mConfirmPasswordTextView.getText().toString().trim();

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