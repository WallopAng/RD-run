package rd.angkanasopit.wallop.rdrun;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Expicit
    private ImageView imageView;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        imageView = (ImageView) findViewById(R.id.imageView6);
        userEditText = (EditText) findViewById(R.id.editText5);
        passwordEditText = (EditText) findViewById(R.id.editText6);

        //Load Image from server
        Picasso.with(this).load("http://swiftcodingthai.com/rd/Image/rd_logo.png").resize(150, 150)
                .into(imageView);


    }   // Main Method

    //create Iner Class
    private class SynUser extends AsyncTask<Void, Void, String> {
        //Explicit
        private Context context;
        private String myUserString, myPasswordString, truePasswordString,
                nameString, surnameString, idString;

        private static final String urlJSON = "http://swiftcodingthai.com/rd/get_user_master.php";
        private boolean statusABoolean = true;

        public SynUser(Context context, String myUserString, String myPasswordString) {
            this.context = context;
            this.myUserString = myUserString;
            this.myPasswordString = myPasswordString;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("31AugV2", "e doInBack ==>" + e.toString());
                return null;
            }


        }   //doInBak

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("31AugV2", "JSon ==> " + s);
            try {
                JSONArray jsonArray = new JSONArray(s);

                for (int i=0; i<jsonArray.length(); i+=1) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    if (myUserString.equals(jsonObject.getString("User"))) {
                        //เท่ากันทำงานที่นี่
                        statusABoolean = false;
                        truePasswordString = jsonObject.getString("Password");
                        nameString = jsonObject.getString("Name");
                        surnameString = jsonObject.getString("Surname");
                        idString = jsonObject.getString("id");



                    }

                }   //loop for

                if (statusABoolean) {
                    //User not found
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDiaog(context, R.drawable.kon48, "User False", "ไม่ม" + myUserString + "ในฐาน");
                } else if (myPasswordString.equals(truePasswordString)) {
                    //password true
                    Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                    startActivity(intent);

                    Toast.makeText(context, "welcome" + nameString + " " + surnameString,
                            Toast.LENGTH_SHORT).show();
                } else {
                    //password false
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDiaog(context, R.drawable.bird48, "Password Fase", "Please Try again");
                }


            } catch (Exception e) {
                Log.d("31AugV3", "e onPost ==> " + e.toString());
            }


        }   //onPost

    }   //SynUser Class





    public void clickSignInMain(View view) {
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //Check space
        if (userString.equals("") || passwordString.equals("")) {
            //have space
            MyAlert myAlert = new MyAlert();
            myAlert.myDiaog(this, R.drawable.rat48, "Have Space", "Pleas Fill All Every Blank");

        } else {
            //no space
            SynUser synUser = new SynUser(this, userString,passwordString);
            synUser.execute();

        }

    }   //ciickSignIn

    //Get Event from Click Button
    public void clickSingUpMain(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }
}   // Main Class นี่คือคลาสหลัก
