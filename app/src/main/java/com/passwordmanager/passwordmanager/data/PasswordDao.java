package com.passwordmanager.passwordmanager.data;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PasswordDao {
    //Insert query

    @Insert(onConflict = REPLACE)
    void insert(passwordLocalDB passwordLocalDB);

    //Delete query
    @Delete
    void delete(passwordLocalDB passwordLocalDB);

    @Delete
    void reset(List<passwordLocalDB> passwordLocalDB);

    @Query("UPDATE user_password SET name= :sName WHERE ID= :sID")
    void updateName(int sID,String sName);

    @Query("UPDATE user_password SET username= :sUsername WHERE ID= :sID")
    void updateUsername(int sID,String sUsername);

    @Query("UPDATE user_password SET password= :sPassword WHERE ID= :sID")
    void updatePassword(int sID,String sPassword);

    @Query("UPDATE user_password SET description= :sDescription WHERE ID= :sID")
    void updateDescription(int sID,String sDescription);

    //get all data from table
    @Query("SELECT * FROM user_password")
    List<passwordLocalDB> getAll();
}
