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

    public ServerRequests(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);    // false : 사용자가 다이얼로그 알림창을 띄울 때 뒤로 나가지 못하게 함
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait..");
    }


    //백그라운드에서 데이터를 서버에 저장중..액티비티는 동결
    public void storeDataInBackground(Contact contact, GetUserCallback callback){
        progressDialog.show();
        new StoreDataAsyncTask(contact, callback).execute();
    }

    public void fetchDataInBackground(Contact contact, GetUserCallback callback){
        progressDialog.show();
        new FetchDataAsyncTask(contact, callback).execute();
    }

    public class StoreDataAsyncTask extends AsyncTask<Void, Void, Void> {
        Contact contact;
        GetUserCallback callback;

        public StoreDataAsyncTask(Contact contact, GetUserCallback callback){
            this.contact = contact;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();
            data_to_send.add(new BasicNameValuePair("name", contact.name));
            data_to_send.add(new BasicNameValuePair("phone", contact.phone));
            data_to_send.add(new BasicNameValuePair("username", contact.username));
            data_to_send.add(new BasicNameValuePair("password", contact.password));

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

    public class FetchDataAsyncTask extends AsyncTask<Void, Void, Contact>{
        Contact contact;
        GetUserCallback callback;

        public FetchDataAsyncTask(Contact contact, GetUserCallback callback){
            this.contact = contact;
            this.callback = callback;
        }

        @Override
        protected Contact doInBackground(Void... params) {
            ArrayList<NameValuePair> data_to_send = new ArrayList<>();

            data_to_send.add(new BasicNameValuePair("username", contact.username));
            data_to_send.add(new BasicNameValuePair("password", contact.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS+"/FetchUserData.php");

            Contact retunedContact = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse =client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                JSONObject jsonObject = new JSONObject(result);
/*
               JSONArray jsonObject= new JSONArray(result);

                for(int i=0;i<jsonObject.length();i++){
                    JSONObject object = jsonObject.getJSONObject(i);
                    String name, phone;
                    name = null;
                    phone = null;

                    if(object.has("name"))
                        name = object.getString("name");
                    if(object.has("phone"))
                        phone = object.getString("phone");

                    retunedContact = new Contact(name, phone, contact.username, contact.password);
                }*/

                if(jsonObject.length() == 0){
                    retunedContact = null;
                }
                else{
                    String name, phone;
                    name = null;
                    phone = null;

                    if(jsonObject.has("name"))
                        name = jsonObject.getString("name");
                    if(jsonObject.has("phone"))
                        phone = jsonObject.getString("phone");

                    retunedContact = new Contact(name, phone, contact.username, contact.password);

                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return retunedContact;
        }

        @Override
        protected void onPostExecute(Contact returnedContact) {

            progressDialog.dismiss();
            callback.done(null);
            super.onPostExecute(returnedContact);
        }
    }
}
