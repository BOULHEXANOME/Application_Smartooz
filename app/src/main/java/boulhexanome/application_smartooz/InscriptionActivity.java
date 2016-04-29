package boulhexanome.application_smartooz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import boulhexanome.application_smartooz.WebServices.PostTask;

public class InscriptionActivity extends AppCompatActivity implements PostTask.AsyncResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button mEmailSignUpButton = (Button) findViewById(R.id.inscription_button);

        assert mEmailSignUpButton != null;
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            atttemptInscription();
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
            dispDialog("L'adresse mail est invalide.", "Erreur : mail invalide");
            return false;
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

    protected void atttemptInscription() {
        boolean infoOk = checkInfo();

        PostTask inscription_thread = new PostTask(Config.getRequest(Config.REGISTER));

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
    }

    @Override
    public void processFinish(JsonObject results) {
        System.out.println(results.toString());
        if (results.get("status").getAsString().equals("OK")) {
            User.getInstance().setEmail(results.get("email").getAsString());
            User.getInstance().setUsername(results.get("username").getAsString());

            Intent intent = new Intent(InscriptionActivity.this, MainActivity.class);
            startActivity(intent);
            //Toast.makeText(InscriptionActivity.this, User.getInstance().toString(), Toast.LENGTH_SHORT).show();


        } else if (results.get("status").getAsString().equals("KO")) {
            Toast.makeText(InscriptionActivity.this, results.get("error").getAsString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(InscriptionActivity.this, "BUG", Toast.LENGTH_SHORT).show();
        }
    }
}
