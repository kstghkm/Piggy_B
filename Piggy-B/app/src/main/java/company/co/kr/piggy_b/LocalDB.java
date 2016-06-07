package company.co.kr.piggy_b;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 2016-06-01.
 */

// 로컬 DB
public class LocalDB {
    public static final String SP_NAME = "UserDetails";
    SharedPreferences localDB;

    public LocalDB(Context context){
        localDB = context.getSharedPreferences(SP_NAME, 0);
    }

    //유저의 정보를 로컬 DB에 저장한다.
    public void storeData(UserInfo userInfo){
        SharedPreferences.Editor spEditor = localDB.edit();
        spEditor.putString("name", userInfo.name);
        spEditor.putString("phone", userInfo.phone);
        spEditor.putString("username", userInfo.username);
        spEditor.putString("password", userInfo.password);
        spEditor.putString("bank", userInfo.bank);
        spEditor.putString("account", userInfo.account);
        spEditor.putInt("coin",userInfo.getCoin());
        spEditor.commit();
    }

    // 동전 정보 관리
    public int getCoin(){
        return localDB.getInt("coin",0);
    }
    public void setCoin(int coin){
        SharedPreferences.Editor spEditor = localDB.edit();
        spEditor.putInt("coin",coin);
        spEditor.commit();
    }

    // 로컬 DB에서 로그인한 유저의 정보를 가져온다.
    public UserInfo getLoggedInUser(){
        String name = localDB.getString("name", "");
        String phone = localDB.getString("phone", "");
        String username = localDB.getString("username", "");
        String password = localDB.getString("password", "");
        String bank = localDB.getString("bank","");
        String account = localDB.getString("account","");
        int coin = localDB.getInt("coin", 0);
        UserInfo storedUserInfo = new UserInfo(name, phone, username, password, bank, account, coin);

        return storedUserInfo;
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

    //자동로그인 설정
    public void setAutoLogin(boolean autoLogin){
        SharedPreferences.Editor spEditor = localDB.edit();
        spEditor.putBoolean("autologin", autoLogin);
        spEditor.commit();
    }

    public boolean getAutoLogin(){
        if(localDB.getBoolean("autologin", false))
            return true;
        else
            return false;
    }
}
