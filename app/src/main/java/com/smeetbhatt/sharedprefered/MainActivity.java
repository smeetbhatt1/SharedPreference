package com.smeetbhatt.sharedprefered;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private static final String TAG = "MainAcitivity";
    @BindView(R.id.btn_sign_in) Button btn_sign_in;
    @BindView(R.id.prompt_email) EditText prompt_email;
    @BindView(R.id.prompt_password) EditText prompt_password;
    @BindView(R.id.textInputLayout_email) TextInputLayout textInputLayout_email;
    @BindView(R.id.textInputLayout_password) TextInputLayout textInputLayout_password;
    @BindView(R.id.saveLoginDetails)
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences("myDatabaseTest",Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        checkPreferences();
    }

    private void checkPreferences(){
        String checkbox = mPreferences.getString(getString(R.string.checkBox),"false");
        String email = mPreferences.getString(getString(R.string.email_sh),"");
        String password = mPreferences.getString(getString(R.string.password_sh),"");
        prompt_email.setText(email);
        prompt_password.setText(password);
        if(checkbox.equals("true"))
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);
    }

    public void setOnClick(View view){
        if(!validateEmail())
            return;
        if(!validatePassword())
            return;
        doWork();
    }

    private void doWork(){
        if(checkBox.isChecked()){
            mEditor.putString(getString(R.string.checkBox),"true");
            mEditor.commit();

            mEditor.putString(getString(R.string.email_sh),prompt_email.getText().toString());
            mEditor.commit();

            mEditor.putString(getString(R.string.password_sh),prompt_password.getText().toString());
            mEditor.commit();
        }
        else{
            mEditor.putString(getString(R.string.checkBox),"false");
            mEditor.commit();

            mEditor.putString(getString(R.string.email_sh),"");
            mEditor.commit();

            mEditor.putString(getString(R.string.password_sh),"");
            mEditor.commit();
        }
    }

    private boolean validatePassword(){
        if (prompt_password.getText().toString().isEmpty()) {
            prompt_password.setError(getText(R.string.missing));
            requestFocus(prompt_password);
            return false;
        }
        else if(prompt_password.getText().toString().length()<8){
            prompt_password.setError(getText(R.string.invalid_pass));
            requestFocus(prompt_password);
            return false;
        }
        else {
            textInputLayout_password.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail(){
        String email = prompt_email.getText().toString().trim();

        if (email.isEmpty()) {
            prompt_email.setError(getString(R.string.missing));
            requestFocus(prompt_email);
            return false;
        }
        else if(!isValidEmail(email)) {
            prompt_email.setError(getString(R.string.invalid_input));
            requestFocus(prompt_email);
            return false;
        }
            textInputLayout_email.setErrorEnabled(false);
            return true;

    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
