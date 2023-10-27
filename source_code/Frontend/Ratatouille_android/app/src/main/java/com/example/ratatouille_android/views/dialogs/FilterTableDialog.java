package com.example.ratatouille_android.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.ratatouille_android.views.DispensaActivity;

public class FilterTableDialog extends DialogFragment {
    DispensaActivity dispensaActivity;
    String filter [] = {"Categoria", "Nome"};

    public FilterTableDialog(DispensaActivity dispensaActivity){
        this.dispensaActivity = dispensaActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Scegli Filtro ")
                .setItems(filter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dispensaActivity.setSelectedFilter(filter[which]);
                        if (filter[which] == "Nome")
                            dispensaActivity.setFilterShower("N");
                        else
                            dispensaActivity.setFilterShower("C");
                    }
                });
        return builder.create();
    }
}