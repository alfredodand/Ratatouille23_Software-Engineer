package com.example.ratatouille_android.views.jfragment;

import static com.example.ratatouille_android.views.MainActivity.analytics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.controllers.HomeController;
import com.example.ratatouille_android.views.HomeActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

public class HomeFragment extends Fragment {

    private TextView nomeAttivita;
    private HomeActivity homeActivity;

    public HomeFragment(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fireBaseLog();

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nomeAttivita = view.findViewById(R.id.nomeAttivitaHome);
        nomeAttivita.setText(homeActivity.getNomeRistorante());

        return view;
    }

    private void fireBaseLog(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "home");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "home_clicked");
        analytics.logEvent("InHomePage", bundle);
    }

    public TextView getNomeAttivita() {
        return nomeAttivita;
    }
}