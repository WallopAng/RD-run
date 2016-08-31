package rd.angkanasopit.wallop.rdrun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {

    //Expicit การประกาศตัวแปร
    private EditText nameEditText, surnameEditText, userEditText, passwordEditText;
    private RadioGroup radioGroup;
    private RadioButton avata1RadioButton, avata2RadioButton, avata3RadioButton, avata4RadioButton, avata5RadioButton;
    private String nameString, surnameString, userString, passwordString, avataString;
    private static final String urlPHP = "http://swiftcodingthai.com/rd/add_user_wallop.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //Bind or Initial Widget คือ การผูกความสัมพันธ์ระหว่างตัวแปรกับ widget
        nameEditText = (EditText) findViewById(R.id.editText);
        surnameEditText = (EditText) findViewById(R.id.editText2);
        userEditText = (EditText) findViewById(R.id.editText3);
        passwordEditText = (EditText) findViewById(R.id.editText4);
        radioGroup = (RadioGroup) findViewById(R.id.ragAvata);
        avata1RadioButton = (RadioButton) findViewById(R.id.radioButton);
        avata2RadioButton = (RadioButton) findViewById(R.id.radioButton2);
        avata3RadioButton = (RadioButton) findViewById(R.id.radioButton3);
        avata4RadioButton = (RadioButton) findViewById(R.id.radioButton4);
        avata5RadioButton = (RadioButton) findViewById(R.id.radioButton5);

        //Radio Controller
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radioButton:
                        avataString = "0";
                        break;
                    case R.id.radioButton2:
                        avataString = "1";
                        break;
                    case R.id.radioButton3:
                        avataString = "2";
                        break;
                    case R.id.radioButton4:
                        avataString = "3";
                        break;
                    case R.id.radioButton5:
                        avataString = "4";
                        break;
                }

            }
        });


    }   //main method

    public void clickSignUpSign(View view) {

        //Get value from Edit Text
        nameString = nameEditText.getText().toString().trim();
        surnameString = surnameEditText.getText().toString().trim();
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //ck space หาชองวาง
        if (checkSpace()) {

            MyAlert myAlert = new MyAlert();
            myAlert.myDiaog(this, R.drawable.doremon48, "มีช่องว่าง", "กรุณากรอกทุกชอง ค่ะ");

        } else if (checkChoose()) {

            confirmValue();

        } else {
            MyAlert myAlert = new MyAlert();
            myAlert.myDiaog(this, R.drawable.nobita48, "มีช่องว่าง", "เติม Avarta ค่ะ");
        }

    }   //cickSign

    private void confirmValue() {
        MyConstant myConstant = new MyConstant();
        int[] avataInts = myConstant.getAvataInts();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setIcon(avataInts[Integer.parseInt(avataString)]);
        builder.setTitle("โปรดรวจสอบข้อมูล");
        builder.setMessage("Name = " + nameString + "\n" +
        "Surname = " + surnameString + "\n" +
        "user =" + userString + "\n" +
        "password " + passwordString);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadValueToServer();
                dialog.dismiss();

            }
        });
        builder.show();

    }   //confirmvalue

    private void uploadValueToServer() {

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormEncodingBuilder()
                .add("isAdd", "true")
                .add("Name", nameString)
                .add("Surname", surnameString)
                .add("User", userString)
                .add("Password", passwordString)
                .add("Avata", avataString)
                .build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(urlPHP).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("31AugV1", "Result ==> " + response.body().string());
                finish();

            }
        });

    }

    private boolean checkChoose() {
        boolean result = false;
        if (avata1RadioButton.isChecked() || avata2RadioButton.isChecked() || avata3RadioButton.isChecked() ||
                avata4RadioButton.isChecked() || avata5RadioButton.isChecked()) {
            result = true;
        }

        return result;
    }

    private boolean checkSpace() {
        boolean result = false;

        if (nameString.equals("") ||
                surnameString.equals("") ||
                userString.equals("") ||
                passwordString.equals("")) {
            result = true;
        }
        return result;
    }
}   //main class
