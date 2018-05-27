package com.example.test.dbconnect_1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class insert extends AppCompatActivity {

    private static final String TAG = "insert";

    private EditText name, telNum, eMail;
    private Button btnSend, btnSearch;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inset);

        name = (EditText)findViewById(R.id.editName);
        telNum = (EditText)findViewById(R.id.editTel);
        eMail = (EditText) findViewById(R.id.editEmail);
        txt = (TextView) findViewById(R.id.txt);

        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sname = name.getText().toString();
                String stelNum = telNum.getText().toString();
                String seMail = eMail.getText().toString();

                InsertData task = new InsertData();
                task.execute(sname,stelNum,seMail);


                name.setText("");
                telNum.setText("");
                eMail.setText("");



            }
        });

        btnSearch = (Button) findViewById(R.id.btnsearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(insert.this, abc.class);
                startActivity(intent);
            }
        });

    }


    class InsertData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(insert.this, "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//
//            txt.setText(result);
//            Log.d(TAG, "POST response  - " + result);

            progressDialog.dismiss();
            if(result.equals("1")){
                Toast.makeText(insert.this, "추가완료", Toast.LENGTH_SHORT).show();
            } else if(result.equals("-1")){
                Toast.makeText(insert.this, "추가실패", Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected String doInBackground(String... params) {

            String name = (String)params[0];
            String telNo = (String)params[1];
            String eMail = (String)params[2];

            String serverURL = "http://211.236.54.253/insert_data.php";
            String postParameters = "name=" + name + "&telNo=" + telNo + "&eMail=" + eMail;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


//                httpURLConnection.setReadTimeout(5000);
//                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }

}

