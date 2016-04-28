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

import org.w3c.dom.Text;

public class InscriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

    Button mEmailSignUpButton = (Button) findViewById(R.id.inscription_button);

    mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean infoOk= checkInfo();
            if(infoOk) {
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
}
