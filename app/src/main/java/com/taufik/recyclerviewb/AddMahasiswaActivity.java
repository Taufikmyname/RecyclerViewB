package com.taufik.recyclerviewb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AddMahasiswaActivity extends AppCompatActivity {
    private Button _saveButton;
    private EditText _alamatEditText, _namaEditText, _nimEditText, _tahunMasukEditText, _tanggalLahirEditText, _tempatLahirEditText;
    private Spinner _jenisKelaminSpinner, _jpSpinner, _statusNikahSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mahasiswa);

//        initInputs();
        initSaveButtons();
    }

    private void initSaveButtons() {
        _saveButton = findViewById(R.id.saveButton);

        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alamat = _alamatEditText.getText().toString();
                String jenisKelamin = _jenisKelaminSpinner.getSelectedItem().toString();
                String jp = _jpSpinner.getSelectedItem().toString();
                String nama = _namaEditText.getText().toString();
                String nim = _nimEditText.getText().toString();
                String statusPernikahan = _statusNikahSpinner.getSelectedItem().toString();
                String tahunMasuk = _tahunMasukEditText.getText().toString();
                String tanggalLahir = _tanggalLahirEditText.getText().toString();
                String tempatLahir = _tempatLahirEditText.getText().toString();

                try {
                    alamat = URLEncoder.encode(alamat,"utf-8");
                    jenisKelamin = URLEncoder.encode(jenisKelamin,"utf-8");
                    jp = URLEncoder.encode(jp,"utf-8");
                    nama = URLEncoder.encode(nama,"utf-8");
                    nim = URLEncoder.encode(nim,"utf-8");
                    statusPernikahan = URLEncoder.encode(statusPernikahan,"utf-8");
                    tanggalLahir = URLEncoder.encode(tanggalLahir,"utf-8");
                    tempatLahir = URLEncoder.encode(tempatLahir,"utf-8");
                }
                catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                String url = "https://stmikpontianak.net/011100862/tambahMahasiswa.php" +
                        "?nim=" + nim +
                        "&nama=" + nama +
                        "&jenisKelamin=" + jenisKelamin +
                        "&tempatLahir=" + tempatLahir +
                        "&tanggalLahir=" + tanggalLahir +
                        "&alamat=" + alamat +
                        "&jp=" + jp +
                        "&statusPernikahan=" + statusPernikahan +
                        "&tahunMasuk=" + tahunMasuk;

                AsyncHttpClient ahc = new AsyncHttpClient();

                ahc.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("*tw*", new String(responseBody));

                        Gson g = new Gson();
                        String responseString = new String(responseBody);

                        Map<String, String> responseMap = g.fromJson(responseString, new TypeToken<Map<String,String>>(){}.getType());
                        String status = responseMap.get("status");

                        if (status != null && status.equals("ok")){
                            String message = responseMap.get("message");
                            new AlertDialog.Builder(AddMahasiswaActivity.this)
                                    .setTitle("Berhasil")
                                    .setMessage(message)
                                    .show();
                        }else {
                            String errorMessage = responseMap.get("message");
                            new AlertDialog.Builder(AddMahasiswaActivity.this)
                                    .setMessage("Error")
                                    .setMessage(errorMessage)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}