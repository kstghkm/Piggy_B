package company.co.kr.piggy_b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by user on 2016-05-25.
 */
public class Register extends Activity implements AdapterView.OnItemSelectedListener {

    EditText etname, etphone, etusername, etpassword, etconfirm_password, etaccount;
    Spinner spinner;
    String bank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        etname = (EditText)findViewById(R.id.ETname);
        etphone = (EditText)findViewById(R.id.ETphone);
        etusername = (EditText)findViewById(R.id.ETuname);
        etpassword = (EditText)findViewById(R.id.ETpass1);
        etconfirm_password = (EditText)findViewById(R.id.ETpass2);
        spinner = (Spinner)findViewById(R.id.spinner);
        etaccount = (EditText)findViewById(R.id.ETaccount);

        //은행 선택 spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.bank_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(position){
            case 0: bank = "WooriB";break;
            case 1: bank = "KB";break;
            case 2: bank = "ShinhanB";break;
            case 3: bank = "KEB";break;
            case 4: bank = "IBK";break;
            case 5: bank = "NH";break;
            case 6: bank = "Post";break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "은행을 선택하세요", Toast.LENGTH_SHORT).show();
    }

    // 회원가입클릭시
    public void onRegisterClick(View view){

        String str_name = etname.getText().toString();
        String str_phone = etphone.getText().toString();
        String str_uname = etusername.getText().toString();
        String str_pass1 = etpassword.getText().toString();
        String str_pass2 = etconfirm_password.getText().toString();
        String str_account = etaccount.getText().toString();
        UserInfo userInfo;

        if(!bank.isEmpty() && str_account != null) {
            if (str_pass1.equals(str_pass2)) {
                userInfo = new UserInfo(str_name, str_phone, str_uname, str_pass1, bank, str_account, 0);
                ServerRequests serverRequests = new ServerRequests(this);
                serverRequests.storeDataInBackground(userInfo, new GetUserCallback() {
                    @Override
                    public void done(UserInfo returnedUserInfo) {
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                Toast.makeText(this, "회원가입 되었습니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(this, "은행 계좌를 입력하세요", Toast.LENGTH_LONG).show();

    }
    public void onLoginClick(View view){
        Intent in = new Intent(Register.this, MainActivity.class);
        startActivity(in);
    }
}
