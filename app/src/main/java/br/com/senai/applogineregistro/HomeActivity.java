package br.com.senai.applogineregistro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName(); //pega informações
    private TextView nome,email; // cria os objetos
    private Button btn_logout, btn_foto; // cria os objetos
    SessionManager sessionManager;
    String getId;
    private static String URL_READ = "http://192.168.0.103/applogin/ler_detalhes.php";
    private static String URL_EDIT = "http://192.168.0.103/applogin/editar_detalhes.php";
    private static String URL_UPLOAD = "http://192.168.0.103/applogin/upload.php";
    private Menu action;
    private Bitmap bitmap;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        //relacionar os objetos com a classe R
        nome = findViewById(R.id.edt_nome_home);
        email = findViewById(R.id.edt_email_home);
        btn_logout = findViewById(R.id.btn_logout);
        btn_foto = findViewById(R.id.btn_foto);
        profile_image = findViewById(R.id.profile_image);


        HashMap<String,String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        //ação do botão logout
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.Logout();
                //finish(); // encerra a activity
            }
        });

        // ação do botão editar foto
        btn_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escolherFoto();
                //finish(); // encerra a activity
            }
        });

    }//oncreate

    //getUserDetail
    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("sucess");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (sucess.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strNome = object.getString("nome").trim();
                                    String strEmail = object.getString("email").trim();

                                    nome.setText(strNome);
                                    email.setText(strEmail);

                                }//for
                            }//if
                        }catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(HomeActivity.this, "Erro lendo detalhe" +
                                     e.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Erro lendo detalhe" +
                                error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",getId);
                return params;
            }
        }; // fim do StringRequest
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }//on create

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.meni_action, menu);

        action = menu;
        action.findItem(R.id.menu_salvar).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_editar:
                nome.setFocusableInTouchMode(true);
                email.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(nome, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_editar).setVisible(false);
                action.findItem(R.id.menu_salvar).setVisible(true);

                return true;
            case R.id.menu_salvar:

                SaveEditDetail();

                action.findItem(R.id.menu_editar).setVisible(true);
                action.findItem(R.id.menu_salvar).setVisible(false);

                nome.setFocusableInTouchMode(false);
                email.setFocusableInTouchMode(false);
                nome.setFocusable(false);
                email.setFocusable(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    // salvar os dados editados
    private void SaveEditDetail() {

        final String nome = this.nome.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String id = getId;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Salvando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                       // Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("sucess");

                            if (sucess.equals("1")) {
                                Toast.makeText(HomeActivity.this, "Sucesso!!" , Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(nome,email,id);
                            }//if
                        }catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(HomeActivity.this, "Erro salvando detalhe" +
                                    e.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Erro salvando detalhe" +
                                error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nome",nome);
                params.put("email",email);
                params.put("id",id);
                return params;
            }
        }; // fim do StringRequest

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    } // saveEditDetail

    // escolher foto
    private void escolherFoto(){
       Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent, "Selecione a foto"),1);
    } // escolherFoto

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();

            }//fim catch
            UploadPicture(getId, getStringImage(bitmap));
        } //if
    }// onActivityResult

    private void UploadPicture(final String id,final String photo){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String sucess = jsonObject.getString("sucess");

                            if (sucess.equals("1")) {
                                Toast.makeText(HomeActivity.this, "Sucessso!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(HomeActivity.this, "Tente novamente!" + e.toString(), Toast.LENGTH_SHORT).show();
                        }



                    }
                }, // ResponseListener
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Tente novamente!" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                } //Response.ErrorListener
        ) // String Request
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("photo", photo);
                return params;
            }
        }; // fim do String Request

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }// fim uploadPicture

    //pega a String da foto
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodeImage;
    }//fim getStringImage

} //class
