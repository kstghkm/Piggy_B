package company.co.kr.piggy_b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by user on 2016-05-25.
 */
public class Register extends Activity{

    EditText etname, etphone, etusername, etpassword, etconfirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        etname = (EditText)findViewById(R.id.ETname);
        etphone = (EditText)findViewById(R.id.ETphone);
        etusername = (EditText)findViewById(R.id.ETuname);
        etpassword = (EditText)findViewById(R.id.ETpass1);
        etconfirm_password = (EditText)findViewById(R.id.ETpass2);
    }

    public void onRegisterClick(View view){

        String str_name = etname.getText().toString();
        String str_phone = etphone.getText().toString();
        String str_uname = etusername.getText().toString();
        String str_pass1 = etpassword.getText().toString();
        String str_pass2 = etconfirm_password.getText().toString();

        Contact contact;

        if(str_pass1.equals(str_pass2)){
            contact = new Contact(str_name, str_phone, str_uname, str_pass1);
            ServerRequests serverRequests = new ServerRequests(this);
            serverRequests.storeDataInBackground(contact, new GetUserCallback() {
                @Override
                public void done(Contact returnedContact) {
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
        else{
            Toast.makeText(this, "Passwords don't match! Enter again!", Toast.LENGTH_LONG).show();
        }

    }
    public void onLoginClick(View view){
        Intent in = new Intent(Register.this, MainActivity.class);
        startActivity(in);
    }
}
