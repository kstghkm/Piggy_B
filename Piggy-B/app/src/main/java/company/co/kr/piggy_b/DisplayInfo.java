package company.co.kr.piggy_b;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 2016-05-25.
 */
public class DisplayInfo extends Activity {

    TextView tvname, tvphone, tvusername, tvpassword;

    LocalDB localDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_info);
        /*
        String username = getIntent().getStringExtra("USERNAME");
        TextView tv = (TextView)findViewById(R.id.TVid);
        tv.setText(username);
        */

        tvname = (TextView) findViewById(R.id.show_name);
        tvphone = (TextView)findViewById(R.id.show_phone);
        tvusername = (TextView)findViewById(R.id.show_uname);
        tvpassword = (TextView)findViewById(R.id.show_pass);

        localDB = new LocalDB(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(authenticate() == true){
            displayContactDetails();
        }
        else{
            Intent intent = new Intent(DisplayInfo.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean authenticate(){
        return localDB.getUserLoggedIn();
    }

    private void displayContactDetails(){
        Contact contact = localDB.getLoggedInUser();
        tvname.setText(contact.name);
        tvphone.setText(contact.phone);
        tvusername.setText(contact.username);
        tvpassword.setText(contact.password);
    }
    public void onLogoutClick(View view){
        localDB.clearData();
        localDB.setUserLoggedIn(false);

        Intent intent = new Intent(DisplayInfo.this, MainActivity.class);
        startActivity(intent);

    }
}

