package company.co.kr.piggy_b;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 2016-06-01.
 */
public class LocalDB {
    public static final String SP_NAME = "UserDetails";
    SharedPreferences localDB;

    public LocalDB(Context context){
        localDB = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeData(Contact contact){
        SharedPreferences.Editor spEditor = localDB.edit();
        spEditor.putString("name", contact.name);
        spEditor.putString("phone", contact.phone);
        spEditor.putString("username", contact.username);
        spEditor.putString("password", contact.password);
        spEditor.commit();
    }

    public Contact getLoggedInUser(){
        String name = localDB.getString("name", "");
        String phone = localDB.getString("phone", "");
        String username = localDB.getString("username", "");
        String password = localDB.getString("password", "");

        Contact storedContact = new Contact(name, phone, username, password);

        return  storedContact;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = localDB.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(localDB.getBoolean("loggedIn", false))
            return true;
        else
            return false;
    }

    public void clearData(){
        SharedPreferences.Editor spEditor = localDB.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
