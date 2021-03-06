package com.passwordmanager.passwordmanager.ui.auth;

import android.content.Context;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.renderscript.ScriptGroup;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.passwordmanager.passwordmanager.databinding.FragmentLoginBinding;
import com.passwordmanager.passwordmanager.ui.ProgressBar.ProgressBar;
import com.passwordmanager.passwordmanager.R;

import java.util.concurrent.TimeUnit;

import com.google.firebase.auth.PhoneAuthOptions;
import com.passwordmanager.passwordmanager.util.ViewUtilKt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements AuthListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context context;
    private FirebaseAuth Auth;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressBar dialog;
    FragmentLoginBinding binding;

    public LoginFragment(){}

    public LoginFragment(Context context) {
        this.context=context;
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
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false);
        View view=binding.getRoot();


        dialog=new ProgressBar(getActivity());

        //initialize the app
        FirebaseApp.initializeApp(context);
        Auth = FirebaseAuth.getInstance();

        //If current user is already logged in we don't need this Activity to run.
        binding.getOtpBtn.setOnClickListener(view1 -> {
            verifyPhoneNumber(Auth);

        });
        YoYo.with(Techniques.Bounce).duration(2000).repeat(50).playOn(binding.verifyPhone);

        return view;
    }

    public void goToNextFragment(Fragment fragment){
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout,fragment,"FindThisFragment")
                .commit();
    }
    //To verify phone number from firebase we use this function.
    public void verifyPhoneNumber(FirebaseAuth auth){

        if(!binding.phoneNumber.getText().toString().isEmpty()){
            if((binding.phoneNumber.getText().toString().trim()).length()==10){
                dialog.startLoadingDialog();
                PhoneAuthOptions options=PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91"+binding.phoneNumber.getText().toString())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(getActivity())
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                dialog.dismissDialog();
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verifyId, forceResendingToken);
                                Bundle bundle=new Bundle();
                                bundle.putString("OTP",verifyId);
                                dialog.dismissDialog();
                                bundle.putString("PhoneNumber",binding.phoneNumber.getText().toString());
                                OTPFragment otpFragment=new OTPFragment(context);
                                otpFragment.setArguments(bundle);
                                goToNextFragment(otpFragment);
                                Toast.makeText(context,"Enter OTP!",Toast.LENGTH_SHORT).show();
                            }
                        }).build();
                PhoneAuthProvider.verifyPhoneNumber(options);//This is the firebase object's function
            }else{
                Toast.makeText(context,"Enter 10-Digit phone number",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context,"Enter a valid phoneNumber",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStarted() {
        ViewUtilKt.toast(context,"Login Started");
    }

    @Override
    public void onSuccess() {
        ViewUtilKt.toast(context,"Login Success");

    }

    @Override
    public void onFailure(String message) {
        ViewUtilKt.toast(context,message);

    }
}