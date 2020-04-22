package br.com.senai.applogineregistro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NOME = "NOME";
    public static final String EMAIL = "EMAIL";
    public static final String ID = "ID";

    //método construtor da classe
    public SessionManager(Context context){
        this.context = context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    //criar uma sessão
    public void createSession (String nome, String Email, String id){
        editor.putBoolean(LOGIN, true);
        editor.putString(NOME, nome);
        editor.putString(EMAIL, Email);
        editor.putString(ID, id);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((HomeActivity) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NOME, sharedPreferences.getString(NOME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL,null));
        user.put(ID, sharedPreferences.getString(ID, null));

        return user;
    }

    public void Logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
        ((HomeActivity) context).finish();
    }

}
