package com.example.test.dbconnect_1;

import android.app.ProgressDialog;

import android.os.AsyncTask;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.View;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ListAdapter;

import android.widget.ListView;

import android.widget.SimpleAdapter;

import android.widget.TextView;



import org.json.JSONArray;

import org.json.JSONException;

import org.json.JSONObject;


import java.io.BufferedReader;


import java.io.InputStream;

import java.io.InputStreamReader;


import java.io.OutputStream;

import java.net.HttpURLConnection;

import java.net.URL;

import java.util.ArrayList;

import java.util.HashMap;



public class abc extends AppCompatActivity {


    private static String TAG = "phpquerytest";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_Tel ="telNo";
    private static final String TAG_email ="eMail";


    ArrayList<HashMap<String, String>> mArrayList;

    ListView mListViewList;

    EditText mEditTextSearchKeyword;

    String mJsonString;



    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.abc);


        mListViewList = (ListView) findViewById(R.id.listView_main_list);

        mEditTextSearchKeyword = (EditText) findViewById(R.id.editText_main_searchKeyword);


        Button button_search = (Button) findViewById(R.id.button_main_search);

        button_search.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                mArrayList.clear();



                GetData task = new GetData();

                task.execute( mEditTextSearchKeyword.getText().toString());

            }

        });



        mArrayList = new ArrayList<>();



    }



    private class GetData extends AsyncTask<String, Void, String>{


        ProgressDialog progressDialog;

        String errorString = null;


        @Override

        protected void onPreExecute() {

            super.onPreExecute();


            progressDialog = ProgressDialog.show(abc.this,

                    "Please Wait", null, true, true);

        }



        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);


            progressDialog.dismiss();



            Log.d(TAG, "response - " + result);


            if (result == null){


            }

            else {


                mJsonString = result;

                showResult();

            }

        }



        @Override

        protected String doInBackground(String... params) {


            String searchKeyword = params[0];


            String serverURL = "http://172.16.14.23/id_check.php";

            String postParameters = "country=" + searchKeyword;


            try {


                URL url = new URL(serverURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();



                httpURLConnection.setReadTimeout(7000);

                httpURLConnection.setConnectTimeout(7000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.connect();



                OutputStream outputStream = httpURLConnection.getOutputStream();

                outputStream.write(postParameters.getBytes("UTF-8"));

                outputStream.flush();

                outputStream.close();



                int responseStatusCode = httpURLConnection.getResponseCode();

                Log.d(TAG, "response code - " + responseStatusCode);


                InputStream inputStream;

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {

                    inputStream = httpURLConnection.getInputStream();

                }

                else{

                    inputStream = httpURLConnection.getErrorStream();

                }



                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                StringBuilder sb = new StringBuilder();

                String line;


                while((line = bufferedReader.readLine()) != null){

                    sb.append(line);

                }



                bufferedReader.close();



                return sb.toString().trim();



            } catch (Exception e) {


                Log.d(TAG, "InsertData: Error ", e);

                errorString = e.toString();


                return null;

            }


        }

    }



    private void showResult(){

        try {

            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){


                JSONObject item = jsonArray.getJSONObject(i);


                String id = item.getString(TAG_ID);

                String name = item.getString(TAG_NAME);

                String tel = item.getString(TAG_Tel);

                String eMail = item.getString(TAG_email);


                HashMap<String,String> hashMap = new HashMap<>();


                hashMap.put(TAG_ID, id);

                hashMap.put(TAG_NAME, name);

                hashMap.put(TAG_Tel, tel);

                hashMap.put(TAG_email, eMail);



                mArrayList.add(hashMap);

            }


            ListAdapter adapter = new SimpleAdapter(

                    abc.this, mArrayList, R.layout.item_list,

                    new String[]{TAG_ID,TAG_NAME, TAG_Tel, TAG_email},

                    new int[]{R.id.textView_list_id, R.id.textView_list_name, R.id.textView_list_teNo, R.id.textView_list_eMail}

            );


            mListViewList.setAdapter(adapter);


        } catch (JSONException e) {


            Log.d(TAG, "showResult : ", e);

        }


    }


}


