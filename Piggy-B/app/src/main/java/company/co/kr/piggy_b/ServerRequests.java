package company.co.kr.piggy_b;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by user on 2016-06-01.
 */


public class ServerRequests {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 15000;     //15초
    public static final String SERVER_ADDRESS = "http://piggg.comxa.com";  //서버주소

    //서버에 요청하는동안 기다림
    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);    // false : 사용자가 다이얼로그 알림창을 띄울 때 뒤로 나가지 못하게 함
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait..");
    }


    //백그라운드에서 데이터를 서버에 저장중..액티비티는 동결
    public void storeDataInBackground(UserInfo userInfo, GetUserCallback callback){
        progressDialog.show();
        new StoreDataAsyncTask(userInfo, callback).execute();
    }

    public void fetchDataInBackground(UserInfo userInfo, GetUserCallback callback){
        progressDialog.show();
        new FetchDataAsyncTask(userInfo, callback).execute();
    }

    public class StoreDataAsyncTask extends AsyncTask<Void, Void, Void> {
        UserInfo userInfo;
        GetUserCallback callback;

        public StoreDataAsyncTask(UserInfo userInfo, GetUserCallback callback){
            this.userInfo = userInfo;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();
            data_to_send.add(new BasicNameValuePair("name", userInfo.name));
            data_to_send.add(new BasicNameValuePair("phone", userInfo.phone));
            data_to_send.add(new BasicNameValuePair("username", userInfo.username));
            data_to_send.add(new BasicNameValuePair("password", userInfo.password));
            data_to_send.add(new BasicNameValuePair("bank", userInfo.bank));
            data_to_send.add(new BasicNameValuePair("account",userInfo.account));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+"/Register.php");

            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                client.execute(post);
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.dismiss();
            callback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class FetchDataAsyncTask extends AsyncTask<Void, Void, UserInfo>{
        UserInfo userInfo;
        GetUserCallback callback;

        public FetchDataAsyncTask(UserInfo userInfo, GetUserCallback callback){
            this.userInfo = userInfo;
            this.callback = callback;
        }

        @Override
        protected UserInfo doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("username", userInfo.username));
            data_to_send.add(new BasicNameValuePair("password", userInfo.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+"/FetchUserData.php");

            UserInfo retunedUserInfo = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0){
                    retunedUserInfo = null;
                }
                else{
                    int coin;
                    String name, phone, bank, account;
                    name = null;
                    phone = null;
                    bank = null;
                    account = null;
                    coin = 0;

                    if(jsonObject.has("name"))
                        name = jsonObject.getString("name");
                    if(jsonObject.has("phone"))
                        phone = jsonObject.getString("phone");
//                    if(jsonObject.has("bank"))
//                        bank = jsonObject.getString("bank");
//                    if(jsonObject.has("account"))
//                        account = jsonObject.getString("account");
//                    if(jsonObject.has("coin"))
//                        coin = jsonObject.getInt("coin");

                    retunedUserInfo = new UserInfo(name, phone, userInfo.username, userInfo.password, bank, account, coin);

                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return retunedUserInfo;
        }

        @Override
        protected void onPostExecute(UserInfo returnedUserInfo) {

            progressDialog.dismiss();
            callback.done(returnedUserInfo);
            super.onPostExecute(returnedUserInfo);
        }
    }
}