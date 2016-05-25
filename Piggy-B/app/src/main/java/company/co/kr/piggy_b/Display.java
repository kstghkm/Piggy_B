package company.co.kr.piggy_b;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by user on 2016-05-25.
 */
public class Display extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        String username = getIntent().getStringExtra("USERNAME");
        TextView tv = (TextView)findViewById(R.id.TVid);
        tv.setText(username);
    }
}

