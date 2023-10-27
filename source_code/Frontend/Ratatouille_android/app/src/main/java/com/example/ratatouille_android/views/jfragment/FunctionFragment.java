package com.example.ratatouille_android.views.jfragment;

import static com.example.ratatouille_android.views.MainActivity.analytics;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.controllers.HomeController;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.DispensaActivity;
import com.example.ratatouille_android.views.HomeActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FunctionFragment extends Fragment {
    private User loggedUser;
    private HomeActivity homeActivity;

    public FunctionFragment(HomeActivity homeActivity, User loggedUser){
        this.loggedUser = loggedUser;
        this.homeActivity = homeActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fireBaseLog();

        View view = inflater.inflate(R.layout.fragment_function, container, false);
        Button buttonInventarioDispensa = view.findViewById(R.id.dispensaButton);
        if(loggedUser.getRuolo().equals("addetto_sala")){
            buttonInventarioDispensa.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_lock_24, 0);
            buttonInventarioDispensa.setClickable(false);
        }else{
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent switchActivityIntent = new Intent(homeActivity, DispensaActivity.class);
                    switchActivityIntent.putExtra("loggedUser", loggedUser);
                    homeActivity.startActivity(switchActivityIntent);
                }
            };
            buttonInventarioDispensa.setOnClickListener(onClickListener);
        }
        return view;
    }

    private void fireBaseLog(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "funzionalità");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "funzionalità_clicked");
        analytics.logEvent("InFunzionalità", bundle);
    }

}