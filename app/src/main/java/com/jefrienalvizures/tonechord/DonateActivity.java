package com.jefrienalvizures.tonechord;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DonateActivity extends AppCompatActivity {

    //private static PayPalConfiguration config;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.txtCantidadDonar)
    EditText _monto;
    @Bind(R.id.txtEmailDonar) EditText _email;
    @Bind(R.id.txtNombreDonar) EditText _nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate2);
        ButterKnife.bind(this);
        setupToolbar();
        /*config = new PayPalConfiguration();
        config.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX);
        // Productivo
        //config.clientId("AYPh28uXbcH4LcZSxp28SVRmGemIOvZL436WXXUs3qggZJseJgNxcDW75YOm8dzwjCheq8xR99YCyJff");
        // Sandbox pruebas
        config.clientId("AVp9VcLFgrUPm7RSuVi5dn1hZxQ2QHbwdX2j0FdQvibBZFG5P49ug98InUAQiOLheybyYwsLNvBc8Mug");

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        Button btnDonar = (Button) findViewById(R.id.btnDonarPaypal);
        btnDonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validar()) {
                    donar();
                }
            }
        });
        */
    }

    @Override
    protected void onDestroy() {
       // stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validar(){
        boolean resultado = true;
        if(_nombre.getText().toString().isEmpty() || _nombre.getText().toString().trim().length() == 0){
            _nombre.setText("Anonimo");
        }
        if(_email.getText().toString().isEmpty()
                || _email.getText().toString().trim().length() < 6
                || !_email.getText().toString().trim().contains("@")){
            _email.setError("Correo es requerio");
            resultado = false;
        } else {
            _email.setError(null);
        }

        if (_monto.getText().toString().trim().isEmpty() || _monto.getText().toString().trim().length() == 0){
            _monto.setError("Monto requerido");
            resultado = false;
        } else {
            _monto.setError(null);
        }
        return  resultado;
    }

   /* // Cuando se presiona el boton de  donar
    public void donar(){
        PayPalPayment payment = new PayPalPayment(new BigDecimal(_monto.getText().toString().trim()), "USD", "Donación de", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                // Pago con exito

                try {
                    Log.i("ejemploPago",confirm.toJSONObject().toString(4));
                } catch (JSONException  e) {
                    Log.e("paymentExample", "an extremely unlikely failure ocurred: ",e);
                }
            }
        }
        else if (requestCode == Activity.RESULT_CANCELED) {
            Log.i("ejemplp pago","El usuario cancelo");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}
