package company.co.kr.piggy_b;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText etusername, etpassword;
    LocalDB localDB;
    CheckBox autoLogin;


    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);
        localDB = new LocalDB(this);
        etusername = (EditText)findViewById(R.id.TFusername);
        etpassword = (EditText)findViewById(R.id.TFpass);
        autoLogin = (CheckBox)findViewById(R.id.checkBox);
        if(localDB.getAutoLogin()) {
            autoLogin.setChecked(true);
            Intent i = new Intent(MainActivity.this, DisplayInfo.class);
            startActivity(i);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        localDB.setAutoLogin(autoLogin.isChecked());
    }


    @Override//뒤로가기 눌렀을 때
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    //로그인 버튼 클릭
    public void onLoginClick(View view){
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();
        UserInfo userInfo = new UserInfo(username, password);

        authenticate(userInfo);
    }

    //회원가입 버튼 클릭
    public void onRegisterClick(View view){
        Intent in = new Intent(MainActivity.this, Register.class);
        startActivity(in);
    }

    //회원인지 판단 DB서버와 통신
    private void authenticate(UserInfo userInfo){
        ServerRequests serverRequests = new ServerRequests(this);

        serverRequests.fetchDataInBackground(userInfo, new GetUserCallback() {
            @Override
            public void done(UserInfo returnedUserInfo) {

                if(returnedUserInfo == null){
                    // show an error message
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("아이디 혹은 비밀번호가 틀렸습니다.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
                else {
                    // User log in
                    localDB.storeData(returnedUserInfo);
                    localDB.setUserLoggedIn(true);

                    Intent intent = new Intent(MainActivity.this, DisplayInfo.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
