package com.example.ratatouille_android.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;

import com.example.ratatouille_android.controllers.ModificaProdottoController;
import com.example.ratatouille_android.views.ModificaProdottoActivity;

public class DeleteDialog extends DialogFragment {
    ModificaProdottoController modificaProdottoController;
    ModificaProdottoActivity modificaProdottoActivity;

    public DeleteDialog(ModificaProdottoController modificaProdottoController, ModificaProdottoActivity modificaProdottoActivity){
        this.modificaProdottoController = modificaProdottoController;
        this.modificaProdottoActivity = modificaProdottoActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sei sicuro di voler eliminare il prodotto? ")
                .setPositiveButton(HtmlCompat.fromHtml("<font color='#FF0000'>Annulla</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        modificaProdottoActivity.setErrorLableOnCancel();
                    }
                })
                .setNegativeButton(HtmlCompat.fromHtml("<font color='#008000'>Conferma</font>", HtmlCompat.FROM_HTML_MODE_LEGACY), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int i = modificaProdottoActivity.findIndexProductInDispensa();
                        modificaProdottoController.eliminaPiattoDalServer(modificaProdottoActivity.getDispensa().get(i).getIdProdotto());
                        modificaProdottoController.goToDispensaActivity();
                    }
                });
        return builder.create();
    }
}