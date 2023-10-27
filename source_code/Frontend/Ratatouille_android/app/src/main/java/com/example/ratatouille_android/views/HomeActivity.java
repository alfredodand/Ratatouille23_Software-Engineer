package com.example.ratatouille_android.views;

import static com.example.ratatouille_android.views.MainActivity.analytics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ratatouille_android.controllers.HomeController;
import com.example.ratatouille_android.controllers.LogoutController;
import com.example.ratatouille_android.models.Attivita;
import com.example.ratatouille_android.models.Avviso;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.dialogs.LogoutDialog;
import com.example.ratatouille_android.views.jfragment.FunctionFragment;
import com.example.ratatouille_android.views.jfragment.HomeFragment;
import com.example.ratatouille_android.views.jfragment.NoticesFragment;
import com.example.ratatouille_android.R;
import com.example.ratatouille_android.views.jfragment.AccountFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Observable;
import java.util.Observer;

public class HomeActivity extends AppCompatActivity implements Observer {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private NoticesFragment noticesFragment;
    private FunctionFragment functionFragment;
    private User loggedUser;
    private LogoutController logoutController;
    private HomeController homeController;
    private String nomeRistorante = "";
    private int currentFragment;
    private BadgeDrawable badgeDrawable;
    private ImageView immagineProfilo;
    private TextView textNomeAttivita,textRuolo, textNomeCognome;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton floatingActionButton;
    private FirebaseAnalytics analytics = MainActivity.analytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loggedUser = (User) getIntent().getSerializableExtra("loggedUser");
        homeController = new HomeController(loggedUser, this);
        accountFragment = new AccountFragment(this, loggedUser);
        noticesFragment = new NoticesFragment(this, loggedUser);
        functionFragment = new FunctionFragment(this, loggedUser);
        homeFragment = new HomeFragment(this);
        logoutController = new LogoutController(this, loggedUser);

        textNomeCognome = findViewById(R.id.textNomeCognome);
        textNomeAttivita = findViewById(R.id.textnomeAttività);
        textRuolo = findViewById(R.id.textRuolo);
        immagineProfilo = findViewById(R.id.imageView7);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        floatingActionButton = findViewById(R.id.home);

        currentFragment = getIntent().getExtras().getInt("frgToLoad");
        switch (currentFragment){
            case R.id.notices:
                bottomNavigationView.getMenu().findItem(R.id.notices).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, noticesFragment).commit();
                break;
            case R.id.functions:
                bottomNavigationView.getMenu().findItem(R.id.functions).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, functionFragment).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                break;
        }

        setNumberOfNoticeToRead();
        setTopActivityInfo();
        disableNothingButton();

        View.OnClickListener onHomeButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeController.getNumberOfNoticesToRead();
                bottomNavigationView.getMenu().findItem(R.id.nothing).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
            }
        };

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                homeController.getNumberOfNoticesToRead();

                switch (item.getItemId()) {
                    case R.id.notices:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, noticesFragment).commit();
                        return true;
                    case R.id.account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();
                        return true;
                    case R.id.logout:
                        LogoutDialog logoutDialog = new LogoutDialog(logoutController, HomeActivity.this);
                        logoutDialog.show(getSupportFragmentManager(), "");
                        return true;
                    case R.id.functions:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, functionFragment).commit();
                        return true;
                    case R.id.nothing:
                        return true;
                }

                return false;
            }
        });

        makesRoundedBottomNavBar(bottomAppBar);
        bottomNavigationView.setBackground(null);
        floatingActionButton.setOnClickListener(onHomeButtonClickListener);
    }

    private void setNumberOfNoticeToRead() {
        homeController.getNumberOfNoticesToRead();
        badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.notices);
        badgeDrawable.setVisible(true);
    }

    private void disableNothingButton() {
        bottomNavigationView.getMenu().findItem(R.id.nothing).setChecked(true);
        bottomNavigationView.getMenu().findItem(R.id.nothing).setEnabled(false);
    }

    private void setTopActivityInfo() {
        homeController.getNomeRistorante();
        textNomeCognome.setText(String.format("%s %s", loggedUser.getNome(), loggedUser.getCognome()));
        textRuolo.setText(loggedUser.getRuolo());
    }

    private void makesRoundedBottomNavBar(BottomAppBar bottomAppBar) {
        MaterialShapeDrawable bottomBarBackground = (MaterialShapeDrawable) bottomAppBar.getBackground();
        bottomBarBackground.setShapeAppearanceModel(
                bottomBarBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, 40)
                        .setTopLeftCorner(CornerFamily.ROUNDED, 40)
                        .setBottomRightCorner(CornerFamily.ROUNDED, 40)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, 40)
                        .build());
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof Attivita){
            Attivita a = (Attivita) o;
            nomeRistorante = a.getNome();
            TextView textNomeAttivita = findViewById(R.id.textnomeAttività);
            TextView textNomeAttivitaHome = findViewById(R.id.nomeAttivitaHome);
            textNomeAttivita.setText(a.getNome());
            if(currentFragment == R.id.home)
                textNomeAttivitaHome.setText(a.getNome());
        } else if(o instanceof Avviso){
            Avviso a = (Avviso) o;
            noticesFragment.generateCard(a);
        }
    }

    public void goToHomeActivity(){
        Intent switchActivityIntent = new Intent(this, HomeActivity.class);
        switchActivityIntent.putExtra("loggedUser", loggedUser);
        this.startActivity(switchActivityIntent);
        this.finish();
    }

    public HomeController getHomeController() {
        return homeController;
    }

    public BadgeDrawable getBadgeDrawable() {
        return badgeDrawable;
    }

    public void setTextNomeCognome(String nomeCognome) {
        textNomeCognome.setText(nomeCognome);
    }

    public String getNomeRistorante() {
        return nomeRistorante;
    }
}
