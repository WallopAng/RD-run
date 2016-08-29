package rd.angkanasopit.wallop.rdrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }   // Main Method

    //Get Event from Click Button
    public void clickSingUpMain(View view) {
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }
}   // Main Class นี่คือคลาสหลัก
