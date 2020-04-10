package br.com.senai.applogineregistro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //declaração dos objetos
    private EditText email,senha;
    private Button btn_login;
    private TextView linkdoregistro;
    private ProgressBar loading;
    private static String URL_LOGIN = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //conexão dos objetos com a classe R
        email = findViewById(R.id.edt_email);
        senha = findViewById(R.id.edt_senha);
        btn_login = findViewById(R.id.btn_login);
        linkdoregistro = findViewById(R.id.txt_registro);
        loading = findViewById(R.id.loading);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mSenha = senha.getText().toString().trim();

                if (!mEmail.isEmpty() || !mSenha.isEmpty()) {
                    Login(mEmail,mSenha);
                } else {
                    email.setError("Por favor insira o email");
                    senha.setError("Por favor insira a senha");
                }
            }
        });

    }

    //método login
    private void Login(String email, String senha){
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

    }
}
