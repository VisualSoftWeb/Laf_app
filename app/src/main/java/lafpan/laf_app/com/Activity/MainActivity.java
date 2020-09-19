package lafpan.laf_app.com.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import lafpan.laf_app.com.splashactivity.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                abrirAutenticacao();

            }
        },3500);
    }

private void abrirAutenticacao(){

    Intent i = new Intent(MainActivity.this, AutenticacaoActivity.class);

    startActivity(i);

    finish();
}

}