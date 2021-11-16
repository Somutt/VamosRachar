package com.example.borarachar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText edtValor, edtPessoas;
    TextView tvRes;
    ImageButton btShare, btTTS;
    TextToSpeech ttsPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtValor =(EditText) findViewById(R.id.editTextValor);
        edtPessoas =(EditText) findViewById(R.id.editTextPessoas);
        tvRes =(TextView) findViewById(R.id.textViewResultado);
        btShare =(ImageButton) findViewById(R.id.imageButtonShare);
        btTTS =(ImageButton) findViewById(R.id.imageButtonSpeak);

        edtValor.addTextChangedListener(this);
        edtPessoas.addTextChangedListener(this);
        btShare.setOnClickListener(this);
        btTTS.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1122){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                ttsPlayer = new TextToSpeech(this, this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.v("PDM", edtValor.getText().toString());
        try {
            double edtValordb = Double.parseDouble(edtValor.getText().toString());
            double edtPessoasdb = Double.parseDouble(edtPessoas.getText().toString());
            double res = edtValordb / edtPessoasdb;
            DecimalFormat df = new DecimalFormat("#.00");
            tvRes.setText("R$ "+df.format(res));
        } catch (Exception e) {
            tvRes.setText("R$ 0.00");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btShare) {
            Intent intent = new Intent (Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "A conta dividida por pessoa deu: "+tvRes.getText().toString());
            startActivity(intent);
        }
        if (v == btTTS) {
            if(ttsPlayer != null) {
                ttsPlayer.speak("O valor por pessoa é de " + tvRes.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Toast.makeText(this, "TTS ativado...", Toast.LENGTH_LONG).show();
        } else if (status == TextToSpeech.ERROR){
            Toast.makeText(this, "Sem TTS habilitado...", Toast.LENGTH_LONG).show();
        }
    }
}