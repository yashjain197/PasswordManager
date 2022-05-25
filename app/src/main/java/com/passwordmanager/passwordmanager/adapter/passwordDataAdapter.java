package com.passwordmanager.passwordmanager.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.passwordmanager.passwordmanager.R;
import com.passwordmanager.passwordmanager.data.RoomDB;
import com.passwordmanager.passwordmanager.data.passwordLocalDB;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

public class passwordDataAdapter extends RecyclerView.Adapter<passwordDataAdapter.ViewHolder> {
    private List<passwordLocalDB> data_list;
    private Activity context;
    private RoomDB database;

    public passwordDataAdapter(Activity context,List<passwordLocalDB> data_list){
        this.context=context;
        this.data_list=data_list;
        notifyDataSetChanged();
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
        holder.cardView.setCardBackgroundColor(getRandomColorCode());

        database=RoomDB.getInstance(context);

        holder.cardView.setOnClickListener(view -> {
        //initializing passwordLocalDB
        passwordLocalDB d=data_list.get(holder.getAdapterPosition());
        int sId=d.getID();

        String sName=d.getName();

        //create dialog
            passwordShowDialog(position);
        });
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.PasswordName);
            name = itemView.findViewById(R.id.name);
        }
    }
    public int getRandomColorCode(){
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256),random.nextInt(256));

    }
    private void passwordShowDialog(int position) {
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.password_display_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        EditText name=dialog.findViewById(R.id.name);
        EditText username=dialog.findViewById(R.id.username);
        EditText password=dialog.findViewById(R.id.password);
        EditText description=dialog.findViewById(R.id.description);
        ImageView back=dialog.findViewById(R.id.backBtn);
        passwordLocalDB d=data_list.get(position);
        name.setText(d.getName());
        username.setText(d.getUsername());
        password.setText(d.getPassword());
        if(d.getDescription().equals("N/A")){
            description.setText("Description is not available");
        }else{
            description.setText(d.getDescription());
        }
        back.setOnClickListener(view -> {
            dialog.dismiss();
        });

    }

}
