package com.passwordmanager.passwordmanager.ui.PasswordDisplayScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.passwordmanager.passwordmanager.adapter.passwordDataAdapter;
import com.passwordmanager.passwordmanager.data.RoomDB;
import com.passwordmanager.passwordmanager.data.passwordLocalDB;
import com.passwordmanager.passwordmanager.ui.AddPasswordScreen.AddPasswordData;
import com.passwordmanager.passwordmanager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private String TAG="MainActivity";
    private GridLayoutManager layoutManager;
    RoomDB database;
    public static List<passwordLocalDB> dataList;
    public static passwordDataAdapter adapter;
    private GestureDetector gestureDetector;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityMainBinding binding;
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /** Setting click listener on fab button **/
        binding.addPassword.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddPasswordData.class));
        });

        gestureDetector= new GestureDetector(this,new GestureListener());

        //initialize database
        database= RoomDB.getInstance(this);

        //Storing database value in database
        dataList=new ArrayList<>();
        dataList.clear();
        dataList=database.passwordDao().getAll();
        if(!dataList.isEmpty()) binding.noData.setVisibility(View.GONE);
//        dataList.addAll(database.passwordDao().getAll());

        //SettingUp adapter
        if(!dataList.isEmpty())
        adapter=new passwordDataAdapter(this,dataList);

//        setting layout manager
        layoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);


        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            binding.swipeRefreshLayout.setRefreshing(false);
            dataList=database.passwordDao().getAll();
            if(!dataList.isEmpty()) binding.noData.setVisibility(View.GONE);
//        dataList.addAll(database.passwordDao().getAll());

            //SettingUp adapter
            if(!dataList.isEmpty())
                adapter=new passwordDataAdapter(this,dataList);

//        setting layout manager
            layoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

            binding.recyclerView.setLayoutManager(layoutManager);
            binding.recyclerView.setAdapter(adapter);

        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    database.passwordDao().delete(adapter.getPasswordAt(viewHolder.getAdapterPosition()));

                    dataList.clear();
                    dataList=database.passwordDao().getAll();
                    if(dataList.isEmpty()) binding.noData.setVisibility(View.VISIBLE);
                    adapter=new passwordDataAdapter(MainActivity.this,dataList);
                    binding.recyclerView.setAdapter(adapter);


            }

        }).attachToRecyclerView(binding.recyclerView);
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterPasswordName(editable.toString());
            }
        });
    }

    private void filterPasswordName(String name) {
    ArrayList<passwordLocalDB> searchList=new ArrayList<>();

    if(!dataList.isEmpty()) {
        for (passwordLocalDB item : dataList) {
            if (item.getName().toLowerCase().contains(name.toLowerCase())) {
                searchList.add(item);
            }
        }
        adapter.filteredList(searchList);
    }

    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            Toast.makeText(MainActivity.this, "Fling", Toast.LENGTH_SHORT).show();
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void onSwipeRight() {

    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {

    }

    public void onSwipeBottom() {
    }


}