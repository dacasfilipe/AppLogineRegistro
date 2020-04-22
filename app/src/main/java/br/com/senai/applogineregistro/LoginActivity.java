package br.com.senai.applogineregistro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    //declaração dos objetos
    private EditText email,senha;
    private Button btn_login;
    private TextView linkdoregistro;
    private ProgressBar loading;
    private static String URL_LOGIN = "http://192.168.0.103/applogin/login.php";
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

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

        linkdoregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });

    }

    //método login
    private void Login(final String email, final String senha){
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("sucess");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            if (sucess.equals("1")) {

                                for (int i =0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String nome = object.getString("nome").trim();
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id").trim();

                                    sessionManager.createSession(nome,email, id);

                                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                    intent.putExtra("nome",nome); // passa os parametros
                                    intent.putExtra("email",email);
                                    startActivity(intent); // dá o start numa activity passando a intent

                                    /*Toast.makeText(LoginActivity.this, "Login feito com sucessso! \n" +
                                            " Seu nome: "+nome+"\n Seu Email: "+email, Toast.LENGTH_SHORT).show();*/

                                    loading.setVisibility(View.GONE);

                                }

                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Erro " +e.toString(),Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Erro " +error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                params.put("senha", senha);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
