package boulhexanome.application_smartooz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import boulhexanome.application_smartooz.WebServices.Inscription;

public class InscriptionActivity extends AppCompatActivity implements Inscription.AsyncResponse{

    Inscription inscription_thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

    Button mEmailSignUpButton = (Button) findViewById(R.id.inscription_button);

        assert mEmailSignUpButton != null;
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(inscriptionManager()){
                Intent myIntent = new Intent(InscriptionActivity.this, LoginActivity.class);
                InscriptionActivity.this.startActivity(myIntent);
            }
        }
    });
    }

    protected boolean checkInfo(){
        if(checkPseudo() && checkMail() && checkPassword() && checkConditions()) {
            return true;
        }
        return false;
    }

    private boolean checkConditions() {
        CheckBox checkBoxConditions = (CheckBox) findViewById(R.id.check_conditions);
        if (checkBoxConditions.isChecked()) {
            return true;
        }
        else {
            dispDialog("Veuillez accepter les conditions d'utilisations", "Erreur : conditions non-acceptées");
            return false;
        }
    }

    private boolean checkPassword() {
        EditText password =(EditText) findViewById(R.id.mdp_editText);
        EditText passwordConfirm =(EditText) findViewById(R.id.confMdp_editText);

        if(!password.getText().toString().equals(passwordConfirm.getText().toString())){
            dispDialog("Les champs de mots de passe doivent être identiques.", "Erreur : mot de passe");
            return false;
        }
        return true;
    }

    private boolean checkMail() {
        EditText mail =(EditText) findViewById(R.id.mail_EditText);
        EditText mailConf =(EditText) findViewById(R.id.confMail_editText);

        if(!mail.getText().toString().contains("@")) {

        }

        if(!mail.getText().toString().equals(mailConf.getText().toString())){
            dispDialog("Les champs d'adresse mail doivent être identiques.", "Erreur : adresse mail");
            return false;
        }
        return true;
    }

    protected boolean checkPseudo(){
        return true;

    }

    protected void dispDialog(String message, String title) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setMessage(message);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
    }

    protected boolean inscriptionManager() {
        boolean infoOk = checkInfo();
        Inscription inscription_thread = new Inscription();
        inscription_thread.delegate = this;
        if (infoOk) {

            String pseudo = ((EditText) findViewById(R.id.pseudo_editText)).getText().toString();
            String mail = ((EditText) findViewById(R.id.mail_EditText)).getText().toString();
            String mdp = ((EditText) findViewById(R.id.mdp_editText)).getText().toString();

            JsonObject user = new JsonObject();
            user.addProperty("password", mdp);
            user.addProperty("username", pseudo);
            user.addProperty("email", mail);

            inscription_thread.execute(user);
        }
        return false;
    }

    @Override
    public void processFinish(JsonObject results) {
        if (results.get("status").toString() == "OK"){
            User.getInstance().setPassword(results.get("password").toString());
            User.getInstance().setUsername(results.get("username").toString());
            Toast.makeText(InscriptionActivity.this, User.getInstance().toString(), Toast.LENGTH_SHORT).show();
        } else if (results.get("status").toString() == "KO"){
            Toast.makeText(InscriptionActivity.this, results.get("error").toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(InscriptionActivity.this, "BUG", Toast.LENGTH_SHORT).show();
        }
    }
}
