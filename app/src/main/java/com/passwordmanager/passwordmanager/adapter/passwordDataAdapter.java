package com.passwordmanager.passwordmanager.adapter;

/**
 Created with passion by Yash Jain on 8 May 2022
  **/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.passwordmanager.passwordmanager.R;
import com.passwordmanager.passwordmanager.data.RoomDB;
import com.passwordmanager.passwordmanager.data.passwordLocalDB;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import yuku.ambilwarna.AmbilWarnaDialog;

public class passwordDataAdapter extends RecyclerView.Adapter<passwordDataAdapter.ViewHolder> {
    private List<passwordLocalDB> data_list;
    private final Activity context;
    private RoomDB database;
    private int backgroundColor=-1;

    public passwordDataAdapter(Activity context,List<passwordLocalDB> data_list){
        this.context=context;
        this.data_list=data_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.password_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        passwordLocalDB passwordLocalDB=data_list.get(position);
        holder.name.setText(passwordLocalDB.getName());
//        holder.cardView.setCardBackgroundColor(getRandomColorCode());

        //Setting the random color
        holder.cardView.setCardBackgroundColor(passwordLocalDB.getCustomColor());

        database=RoomDB.getInstance(context);


        holder.cardView.setOnClickListener(view -> {
            if(passwordLocalDB.isVerification() && biometricCheck()){
                showBiometric(position);
            }else {
                //create dialog
                passwordShowDialog(position);
            }
        });
    }

    private void showBiometric(int position) {
        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                passwordShowDialog(position);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("User Authentication")
                .setSubtitle("Login using fingerprint")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.PasswordName);
            name = itemView.findViewById(R.id.name);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @SuppressLint("ResourceAsColor")
    private void passwordShowDialog(int position) {
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.password_display_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        EditText name=dialog.findViewById(R.id.name);
        EditText username=dialog.findViewById(R.id.username);
        EditText password=dialog.findViewById(R.id.password);
        EditText description=dialog.findViewById(R.id.description);
        TextView showHide=dialog.findViewById(R.id.showHideText);
        ImageView back=dialog.findViewById(R.id.backBtn);
        TextView time=dialog.findViewById(R.id.time);
        Button EditBtn=dialog.findViewById(R.id.editBtn);
        ConstraintLayout switchLayout=dialog.findViewById(R.id.verificationSwitch);
        ConstraintLayout colorLayout=dialog.findViewById(R.id.colorLayout);
        Button colorSelectButton=dialog.findViewById(R.id.colorSelectButton);
        Switch authentication=dialog.findViewById(R.id.encryptedOrNot);
        //checking if biometric is available
        if(!biometricCheck()){
            switchLayout.setVisibility(View.GONE);
        }

        passwordLocalDB d=data_list.get(position);
        colorSelectButton.setBackgroundColor(d.getCustomColor());
        backgroundColor=d.getCustomColor();
        AtomicBoolean isVerification= new AtomicBoolean(false);
        if(d.isVerification()){
            isVerification.set(true);
            authentication.setChecked(true);
        }

        name.setText(d.getName());
        username.setText(d.getUsername());
        password.setText(d.getPassword());
        time.setText(d.getLastEdited());
        if(d.getDescription().equals("N/A")){
            description.setText(R.string.noDescriptionAvsailabel);
        }else{
            description.setText(d.getDescription());
        }
        back.setOnClickListener(view -> dialog.dismiss());


        EditBtn.setOnClickListener(view -> {
            if(EditBtn.getText().equals("Edit")) {
                name.setEnabled(true);
                name.setFocusable(true);

                username.setEnabled(true);
                username.setFocusable(true);

                password.setEnabled(true);
                password.setFocusable(true);
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                description.setEnabled(true);
                description.setFocusable(true);

                if(biometricCheck()){
                    switchLayout.setVisibility(View.VISIBLE);
                }
                colorLayout.setVisibility(View.VISIBLE);

                EditBtn.setText(R.string.save);
            }else{
                boolean isChanged=false;
                if(!name.getText().toString().equals(d.getName())){
                    database.passwordDao().updateName(d.getID(),name.getText().toString());
                    isChanged=true;
                }

                if(!username.getText().toString().equals(d.getUsername())){
                    database.passwordDao().updateUsername(d.getID(),username.getText().toString());
                    isChanged=true;
                }

                if(!password.getText().toString().equals(d.getPassword())){
                    database.passwordDao().updatePassword(d.getID(),password.getText().toString());
                    isChanged=true;
                }

                if (!description.getText().toString().equals(d.getDescription())){
                    database.passwordDao().updateDescription(d.getID(),description.getText().toString());
                    isChanged=true;
                }

                if(backgroundColor!=d.getCustomColor()){
                    database.passwordDao().updateCustomColor(d.getID(),backgroundColor);
                    isChanged=true;
                }

                if(isVerification.get()!=d.isVerification()){
                    database.passwordDao().updateIsVerificationOn(d.getID(),isVerification.get());
                    isChanged=true;
                }



                if(isChanged){
                    String currTime=getCurrentTime();
                    database.passwordDao().updateLastEditedTime(d.getID(),currTime);
                    data_list=database.passwordDao().getAll();
                    dialog.dismiss();
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Please edit something", Toast.LENGTH_SHORT).show();
                }
            }

        });

        showHide.setOnClickListener(view -> {
            if(showHide.getText().equals("show")){
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showHide.setText(R.string.hide);
            }else{
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                showHide.setText(R.string.show);
            }
        });

        authentication.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                isVerification.set(true);
            }else{
                isVerification.set(false);
            }
        });

        colorSelectButton.setOnClickListener(view -> {
           showColorPickerDialog(colorSelectButton);
        });

    }
    public passwordLocalDB getPasswordAt(int position){
        return data_list.get(position);
    }

    private boolean biometricCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
                return false;
            } else {
                // Everything is ready for fingerprint authentication
                return true;
            }
        }else{
            return false;
        }
    }

    private int showColorPickerDialog(Button colorBtn) {
        int color= ContextCompat.getColor(context,R.color.bluePurp);
        AmbilWarnaDialog ambilWarnaDialog=new AmbilWarnaDialog(context, color, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                backgroundColor=color;
                colorBtn.setBackgroundColor(backgroundColor);
            }
        });
        ambilWarnaDialog.show();
        return backgroundColor;
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL yyyy, KK:mm aaa");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        return dateTime;
    }

    public void filteredList(ArrayList<passwordLocalDB> filteredList){
        data_list=filteredList;
        notifyDataSetChanged();
    }

}
