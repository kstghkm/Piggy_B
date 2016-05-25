package company.co.kr.piggy_b;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by user on 2016-05-25.
 */
public class SignUp extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }

    public void onSignUpClick(View view){
        if(view.getId() == R.id.Bsignup2){
            EditText name = (EditText)findViewById(R.id.ETname);
            EditText email = (EditText)findViewById(R.id.ETemail);
            EditText id = (EditText)findViewById(R.id.ETid);
            EditText pass1 = (EditText)findViewById(R.id.ETpass1);
            EditText pass2 = (EditText)findViewById(R.id.ETpass2);

            String str_name = name.getText().toString();
            String str_email = email.getText().toString();
            String str_id = id.getText().toString();
            String str_pass1 = pass1.getText().toString();
            String str_pass2 = pass2.getText().toString();

            if(!str_pass1.equals(str_pass2)){
                //pop up message
                Toast pass = Toast.makeText(SignUp.this, "Passwords don't match", Toast.LENGTH_SHORT);
                pass.show();
            }
        }
    }
}
