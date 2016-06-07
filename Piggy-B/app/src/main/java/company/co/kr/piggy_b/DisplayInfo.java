package company.co.kr.piggy_b;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by user on 2016-05-25.
 */
public class DisplayInfo extends Activity {
// NFC 기능 수행 액티비티
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String [][] NFCTechLists;
    int receivedCoin;
    int savedCoin;

    BackPressCloseHandler backPressCloseHandler;

    TextView tvname, tvphone, tvusername, tvaccount;
    TextView tvsavedCoin;
    LocalDB localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_info);

        backPressCloseHandler = new BackPressCloseHandler(this);
        tvname = (TextView) findViewById(R.id.show_name);
        tvphone = (TextView)findViewById(R.id.show_phone);
        tvusername = (TextView)findViewById(R.id.show_uname);
        tvaccount = (TextView)findViewById(R.id.show_account);
        tvsavedCoin = (TextView)findViewById(R.id.show_coin);
        localDB = new LocalDB(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);    //NFC 어댑터 설정
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available!", Toast.LENGTH_LONG);
            return;
        }
        savedCoin = localDB.getCoin();

//      NFC 데이터 활성화에 필요한 인텐트를 생성
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try{
            intentFilter.addDataType("*/*");
            intentFilters = new IntentFilter[]{intentFilter};
        }catch (Exception e){
            Toast.makeText(this,"IntentFilter error",Toast.LENGTH_LONG);
        }

//      NFC 클래스명이 저장된 배열
        NFCTechLists = new String[][] {new String[]{NfcF.class.getName()}};
    }

    @Override//뒤로가기 눌렀을 때
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }


//    액티비티가 실행될 때 NFC어댑터 활성화
    public void onResume() {
        super.onResume();
        if(nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, NFCTechLists);
    }

//    다른 액티비티로 넘어갈 때 NFC 어댑터 비활성화
    public void onPause() {
        super.onPause();
        if(nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);

    }

//    NFC 정보 수신 함수 - 인텐트에 포함된 정보를 분석 후 동전 정보 저장
    public void onNewIntent(Intent intent) {

        UserInfo userInfo = localDB.getLoggedInUser();
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages == null) {
            return;
        }
        // 동전 정보 저장
        receivedCoin = readCoin((NdefMessage)messages[0]);
        updateCoin_localDB(userInfo, receivedCoin);
        // 동전 정보 갱신
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.updateCoinInBackground(userInfo, new GetUserCallback() {
            @Override
            public void done(UserInfo returnedUserInfo) {
                displayContactDetails();
            }
        });
    }

//      NFC 메세지로부터 동전 정보 읽기
    public int readCoin(NdefMessage ndefMessage){
        String strRec = "";
        NdefRecord[] records = ndefMessage.getRecords();
        // 레코드가 여러개 일 때
        for(int i = 0; i < records.length; i++){
            NdefRecord record = records[i];
            byte[] payload = record.getPayload();
            if(Arrays.equals(record.getType(),NdefRecord.RTD_TEXT)){
                strRec = byteDecoding(payload);
            }
        }
        return Integer.valueOf(strRec);
    }


    // encoding된 메세지 decoding
    public String byteDecoding(byte[] buf){
        String strText="";
        String textEncoding = ((buf[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int langCodeLen = buf[0] & 0077;

        try{
            strText = new String (buf, langCodeLen + 1, buf.length - langCodeLen - 1, textEncoding);
        }catch (Exception e){
            Log.d("tag1", e.toString());
        }
        return strText;
    }

    // 액티비티가 시작하면 수행
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

    // 유저가 로그인했는지 확인
    private boolean authenticate(){
        return localDB.getUserLoggedIn();
    }

    //
    private void displayContactDetails(){
        UserInfo userInfo = localDB.getLoggedInUser();
        String temp = "";
        switch (userInfo.bank){
            case "WooriB": temp = "우리은행";break;
            case "KB": temp = "국민은행";break;
            case "ShinhanB": temp = "신한은행";break;
            case "KEB": temp = "외환은행";break;
            case "IBK": temp = "기업은행";break;
            case "NH": temp = "농협";break;
            case "Post": temp = "우체국";break;
            default : break;
        }
        tvname.setText(userInfo.name);
        tvphone.setText(userInfo.phone);
        tvusername.setText(userInfo.username);
        tvaccount.setText(temp + "\n" + userInfo.account);
        tvsavedCoin.setText(String.valueOf(userInfo.getCoin()));
    }
    public void onLogoutClick(View view){
        localDB.clearData();
        localDB.setUserLoggedIn(false);

        Intent intent = new Intent(DisplayInfo.this, MainActivity.class);
        startActivity(intent);

    }

    private void updateCoin_localDB(UserInfo userInfo, int coin){
        userInfo.update_User_coin(coin);
        localDB.setCoin(userInfo.getCoin());
    }


}

