package org.miranda.projet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.miranda.EmailPassword;
import org.miranda.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogActivity extends AppCompatActivity {

    ProgressDialog progressD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Button btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressD = ProgressDialog.show(LogActivity.this, getString(R.string.Wait),
                        getString(R.string.Creds), true);
                verifyCredentials("logIn");
            }
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressD = ProgressDialog.show(LogActivity.this, getString(R.string.Wait),
                        getString(R.string.Creds), true);
                verifyCredentials("signUp");
            }
        });

    }

    public void verifyCredentials(String action) {
        EditText usernameValue = findViewById(R.id.etUserName);
        EditText passwordValue = findViewById(R.id.etPassword);
        final String username = usernameValue.getText().toString();
        String password = passwordValue.getText().toString();
        EmailPassword emailPassword = new EmailPassword(username, password);

        if (!username.matches("") && !password.matches("")) {
            if (action.equals("LogIn")) {
                Service service = RetrofitUtils.get();
                service.login(emailPassword).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        new DialogTask<>().execute();
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.welcome) + " " + username, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ListActivity.class);
                            i.putExtra("NOM", response.body().username);
                            CurrentUser.setCurrentUser(response.body());
                            startActivity(i);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Erreur de connexion \nVeuillez véfirier le nom et le mot de passe", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
            else {
                Service service = RetrofitUtils.get();
                service.createUser(emailPassword).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.welcome) + " " + username, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), ListActivity.class);
                            i.putExtra("NOM", username);
                            CurrentUser.setCurrentUser(response.body());
                            startActivity(i);
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Identifiant déjà utilisé", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        }
        else if (!username.matches("") && password.matches("")) {
            Toast.makeText(getApplicationContext(), R.string.errorPassword, Toast.LENGTH_SHORT).show();
        }
        else if (username.matches("") && !password.matches("")) {
            Toast.makeText(getApplicationContext(), R.string.errorUsername, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), R.string.errorCredentials, Toast.LENGTH_SHORT).show();
            int o = 0;
        }
    }

    class DialogTask<A,B,C> extends AsyncTask<A,B,C> {

        @Override
        protected void onPostExecute(C c) {
            progressD.dismiss();
            super.onPostExecute(c);
        }

        @Override
        protected C doInBackground(A... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
