package com.mojaaplikacja.igor.sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mojaaplikacja.igor.sqlite.data.PhonesContract;
import com.mojaaplikacja.igor.sqlite.data.ResolverHelper;

public class PhoneActivity extends AppCompatActivity {
    private Bundle bundle;
    private EditText producentET;
    private EditText modelET;
    private EditText verET;
    private EditText wwwET;
    private int id;
    private boolean isInEditMode;
    private boolean prodV = false;
    private boolean modelV = false;
    private boolean verV = false;
    private boolean wwwV = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        initViews();
        checkExtras();
        initListeners();
    }

    private void initListeners() {
        producentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prodV = !s.toString().isEmpty(); //jezeli puste to false
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        modelET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                modelV = !s.toString().isEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        verET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //czy zawiera jaks liczbe
                verV = s.toString().matches("(.*)[0-9](.*)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        wwwET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wwwV = s.toString()
                        .matches("(^www\\.(.*)\\.(.*))|(^http://www\\.(.*)\\.(.*))|(^(.*)\\.(.*))");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initViews(){
        producentET = (EditText) findViewById(R.id.producentEditText);
        modelET = (EditText) findViewById(R.id.modelEditText);
        verET = (EditText) findViewById(R.id.verEditText);
        wwwET= (EditText) findViewById(R.id.wwwEditText);
        bundle = getIntent().getExtras();
    }

    /**
     * jesli przesłalismy bundla to odbieramy dane
     */
    private void checkExtras(){
        if(bundle != null){
            producentET.setText(bundle.getString(Constants.PRODUCENT));
            modelET.setText(bundle.getString(Constants.MODEL_NAME));
            verET.setText(bundle.getString(Constants.ANDR_VER));
            wwwET.setText(bundle.getString(Constants.WWW));
            id = Integer.parseInt(bundle.getString(Constants._ID));
            prodV = true;
            wwwV = true;
            verV = true;
            modelV = true;
            //aby wybrac odpowiednia metode (update albo insert)
            isInEditMode = true;
        }
    }

    /**
     * otwiera link w przegladarce
     * @param view
     */
    public void gotoWWW(View view) {
        String url = wwwET.getText().toString();
        if(!url.startsWith("http://"))
            url = "http://"+url;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void clickedAnuluj(View view) {
        onBackPressed();
    }

    /**
     * zapisuje nowy rekord albo updateuje - w zaleznosci od isInEditMode
     * @param view
     */
    public void saveRecord(View view) {
        try {
            if (isInEditMode)
                ResolverHelper.updateData(id, this.getContentResolver(), getValidatedValues());
            else
                ResolverHelper.createPhoneModel(getValidatedValues(), this.getContentResolver());
        }
        catch (IllegalStateException e) {
            Toast.makeText(this, "Sprawdź poprawność wpisanych danych", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,"Zapisano!",Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    /**
     * sprawdzamy czy wpisalismy poprawne dane (mniej wiecej)
     * @return ContentValues
     * @throws IllegalStateException jesli błąd
     */
    private ContentValues getValidatedValues() throws IllegalStateException{

        if(!prodV || !modelV || !verV || !wwwV )
            throw new IllegalStateException("Validate error!");

        ContentValues values = new ContentValues();
        values.put(PhonesContract.PhonesEntry.COLUMN_ANDR_VERSION,verET.getText().toString());
        values.put(PhonesContract.PhonesEntry.COLUMN_WWW,wwwET.getText().toString());
        values.put(PhonesContract.PhonesEntry.COLUMN_MODEL,modelET.getText().toString());
        values.put(PhonesContract.PhonesEntry.COLUMN_PRODUCENT,producentET.getText().toString());
        return values;
    }
}
