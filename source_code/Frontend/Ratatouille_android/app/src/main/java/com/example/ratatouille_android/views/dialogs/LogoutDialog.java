package com.example.ratatouille_android.views.dialogs;

import static com.example.ratatouille_android.views.MainActivity.analytics;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;

import com.example.ratatouille_android.controllers.LogoutController;
import com.example.ratatouille_android.views.HomeActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LogoutDialog extends DialogFragment {

    LogoutController logoutController;
    HomeActivity homeActivity;

    public LogoutDialog(LogoutController logoutController, HomeActivity homeActivity){
        this.logoutController = logoutController;
        this.homeActivity = homeActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Uscire dall'account?")
                .setPositiveButton(HtmlCompat.fromHtml("<font color='#FF0000'>Annulla</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        homeActivity.goToHomeActivity();
                    }
                })
                .setNegativeButton(HtmlCompat.fromHtml("<font color='#008000'>Conferma</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        logoutController.logout();
                        logoutController.goToMainActivity();
                    }
                });
        return builder.create();
    }

    private void fireBaseLog(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "logout");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "logout_clicked");
        analytics.logEvent("LogoutEffettuato", bundle);
    }
}
