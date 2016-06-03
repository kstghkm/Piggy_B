package company.co.kr.piggy_b;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText etusername, etpassword;
    LocalDB localDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localDB = new LocalDB(this);
        etusername = (EditText)findViewById(R.id.TFusername);
        etpassword = (EditText)findViewById(R.id.TFpass);
    }
    public void onLoginClick(View view){
        String username = etusername.getText().toString();
        String password = etpassword.getText().toString();

        Contact contact = new Contact(username, password);
        Log.e("uname = " , contact.username);
        authenticate(contact);

    }
    public  void onRegisterClick(View view){
        Intent in = new Intent(MainActivity.this, Register.class);
        startActivity(in);
    }

    private  void  authenticate(Contact contact){
        ServerRequests serverRequests = new ServerRequests(this);

        serverRequests.fetchDataInBackground(contact, new GetUserCallback() {
            @Override
            public void done(Contact returnedContact) {

                if(returnedContact == null){
                    // show an error message
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Username, Password don't exist");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                }
                else {
                    // User log in
                    localDB.storeData(returnedContact);
                    localDB.setUserLoggedIn(true);

                    Intent intent = new Intent(MainActivity.this, DisplayInfo.class);
                    startActivity(intent);
                }
            }
        });
    }
}
