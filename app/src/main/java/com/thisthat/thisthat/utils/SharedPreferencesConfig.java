package com.thisthat.thisthat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.thisthat.thisthat.R;

public class SharedPreferencesConfig {
    private SharedPreferences sharedPreferences;
    private Context context;
    public SharedPreferencesConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.SHARED_PREFERENCES), Context.MODE_PRIVATE);
    }
    public void saveFriend(String friendPhone){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.FRIEND_PHONE),friendPhone);

        editor.commit();
    }

    public void saveAuthenticationInformation(String phone){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.CLIENTS_PHONE),phone);
        //editor.putString(context.getResources().getString(R.string.CLIENTS_NAME),name);

        editor.commit();
    }
    public String readFriendsPhone(){
        String friendPhone;
        friendPhone = sharedPreferences.getString(context.getResources().getString(R.string.FRIEND_PHONE),"");
        return  friendPhone;
    }
    public String readClientsPhone(){
        String phone;
        phone = sharedPreferences.getString(context.getResources().getString(R.string.CLIENTS_PHONE),"");
        return  phone;
    }
//    public String readClientsName(){
//        String name;
//        name = sharedPreferences.getString(context.getResources().getString(R.string.CLIENTS_NAME),"");
//        return  name;
//    }


    public void clear() {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear()
                .apply();
    }

}
