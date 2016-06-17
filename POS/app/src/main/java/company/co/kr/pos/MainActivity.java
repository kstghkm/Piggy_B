package company.co.kr.pos;

import android.content.DialogInterface;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback,
        NfcAdapter.OnNdefPushCompleteCallback {
    NfcAdapter nfcAdapter = null;

    TextView tvsavingCoin;
    EditText etsum, etpay;
    int total_price, paid_cash;
    int piggycoin = 0;
    private int savingcoin = 0;
    BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backPressCloseHandler = new BackPressCloseHandler(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        tvsavingCoin = (TextView)findViewById(R.id.show_coin);

    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{
                createTextrecord(String.valueOf(piggycoin), Locale.ENGLISH),
        });
        return ndefMessage;
    }

    public NdefRecord createTextrecord(String text, Locale locale){
        byte[] data = byteEncoding(text, locale);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    public byte[] byteEncoding(String text, Locale locale){
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        byte[] textBytes = text.getBytes(utfEncoding);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte)langBytes.length;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        return data;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        handler.obtainMessage(1).sendToTarget();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tvsavingCoin.setText(String.valueOf(savingcoin));
                    Toast.makeText(MainActivity.this,"적립되었습니다.",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override//뒤로가기 눌렀을 때
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public void onTransferClick(View view){

        Toast.makeText(this,"NFC를 태그해주세요",Toast.LENGTH_LONG).show();
        etsum = (EditText)findViewById(R.id.ETsum);
        etpay = (EditText)findViewById(R.id.ETpay);
        total_price = Integer.valueOf(etsum.getText().toString());
        paid_cash = Integer.valueOf(etpay.getText().toString());
        piggycoin = calculatePiggyCoin(total_price,paid_cash);
        update(piggycoin);
        if(piggycoin>0) {
            nfcAdapter.setNdefPushMessageCallback(this, this);
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
        else if(piggycoin == 0 || piggycoin < 0)
            Toast.makeText(this, "거슬러줄 동전이 없습니다.", Toast.LENGTH_LONG).show();
    }
    public void onRefundClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("쌓인 동전을 Piggy-B에게 정산하겠습니까?");
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.savingcoin = 0;
                Toast.makeText(MainActivity.this,"동전을 모두 돌려주었습니다!",Toast.LENGTH_LONG).show();
                tvsavingCoin.setText(String.valueOf(savingcoin));
            }
        });
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private int calculatePiggyCoin(int total_price, int paid_cash){
        int piggy = paid_cash - total_price;
        if(piggy >= 1000)
            return piggy % 1000;
        else if(piggy < 1000 && piggy >= 0)
            return piggy;
        else
            return -1;
    }

    private void update(int coin){
        this.savingcoin += coin;
    }
}