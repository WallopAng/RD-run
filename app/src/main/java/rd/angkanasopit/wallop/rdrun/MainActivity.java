package rd.angkanasopit.wallop.rdrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    //Expicit
    private ImageView imageView;
    private EditText userEditText, passwordEditText;

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


    public void clickSignInMain(View view) {

    }   //ciickSignIn

    //Get Event from Click Button
    public void clickSingUpMain(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }
}   // Main Class นี่คือคลาสหลัก
