package company.co.kr.piggy_b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onButtonClick(View view){
        if(view.getId() == R.id.Blogin) {
            EditText a = (EditText)findViewById(R.id.TFid);
            String str = a.getText().toString();

            Intent i = new Intent(MainActivity.this, Display.class);
            i.putExtra("USERNAME",str);
            startActivity(i);
        }
        if(view.getId() == R.id.Bsignup1){
            Intent i = new Intent(MainActivity.this, SignUp.class);
            startActivity(i);
        }
    }
}
