package com.passwordmanager.passwordmanager.ui.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.passwordmanager.passwordmanager.ui.PasswordDisplayScreen.MainActivity;
import com.passwordmanager.passwordmanager.ui.ProgressBar.ProgressBar;
import com.passwordmanager.passwordmanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OTPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView phnNumber,wrongPhn;
    Context context;
    Activity activity;
    String phoneNumber,verificationId;
    Button verifyBtn;
    ProgressBar dialog;
    EditText in1,in2,in3,in4,in5,in6;

    public OTPFragment() {
        // Required empty public constructor
    } public OTPFragment(Context context) {
        this.context=context;
        this.activity=activity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OTPFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OTPFragment newInstance(String param1, String param2) {
        OTPFragment fragment = new OTPFragment();
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
        View view=inflater.inflate(R.layout.fragment_otp, container, false);
        phnNumber =view.findViewById(R.id.verifyPhone);
        Bundle bundle=this.getArguments();

        dialog=new ProgressBar(getActivity());

        verifyBtn=view.findViewById(R.id.continueBtn);
        wrongPhn=view.findViewById(R.id.wrongPhone);
        //initializing edittext variables
        in1=view.findViewById(R.id.in1);
        in2=view.findViewById(R.id.in2);
        in3=view.findViewById(R.id.in3);
        in4=view.findViewById(R.id.in4);
        in5=view.findViewById(R.id.in5);
        in6=view.findViewById(R.id.in6);
        in1.requestFocus();

        //initializing app
        FirebaseApp.initializeApp(context);

        //Fetching data from last fragment
        phoneNumber=bundle.getString("PhoneNumber");
        verificationId=bundle.getString("OTP");

        //setting text of phone number
        phnNumber.setText("Verify +91-"+phoneNumber);

        //Animation on TextView
        YoYo.with(Techniques.Bounce).duration(2000).repeat(100).playOn(phnNumber);

        verifyBtn.setOnClickListener(view1 -> {
            verifyOtpFromFirebase(verificationId);
        });

        EditTextMove();

        wrongPhn.setOnClickListener(view12 -> {
            LoginFragment loginFragment=new LoginFragment(context);
            goToNextFragment(loginFragment);

        });
        return view;
    }

    private void EditTextMove() {
        in1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                in2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        in2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                in3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        in3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                in4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        in4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                in5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        in5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                in6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
            in6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                verifyOtpFromFirebase(verificationId);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void verifyOtpFromFirebase(String verificationId) {
        //checking if any edit text box is empty
        dialog.startLoadingDialog();
        if(!in1.getText().toString().trim().isEmpty()
                && !in2.getText().toString().trim().isEmpty()
                && !in3.getText().toString().trim().isEmpty()
                && !in4.getText().toString().trim().isEmpty()
                && !in5.getText().toString().trim().isEmpty()
                && !in6.getText().toString().trim().isEmpty()) {

            //Taking all the inputs from Edit text and combine it as one.
            String enteredOTP= in1.getText().toString()+
                    in2.getText().toString()+
                    in3.getText().toString()+
                    in4.getText().toString()+
                    in5.getText().toString()+
                    in6.getText().toString();

            if(verificationId!=null){
                //This is checking the OTP
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, enteredOTP);
                signInWithPhoneAuthCredential(credential);

            }else{
                //If verificationId id null
                dialog.dismissDialog();
                Toast.makeText(context,"Please check internet connection", Toast.LENGTH_SHORT).show();


            }
        }else{
            //If user not entered all the numbers
            dialog.dismissDialog();
            Toast.makeText(context,"Please enter all number",Toast.LENGTH_SHORT).show();


        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        dialog.dismissDialog();
                        Toast.makeText(context,"Incorrect OTP!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goToNextFragment(Fragment fragment){
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout,fragment,"FindThisFragment")
                .commit();
    }


}