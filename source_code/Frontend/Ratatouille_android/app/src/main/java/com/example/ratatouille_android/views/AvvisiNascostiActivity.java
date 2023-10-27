package com.example.ratatouille_android.views;

import android.graphics.Color;
import android.os.Bundle;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.controllers.AvvisiNascostiController;
import com.example.ratatouille_android.models.Avviso;
import com.example.ratatouille_android.models.User;
import com.google.firebase.analytics.FirebaseAnalytics;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Iterator;

public class AvvisiNascostiActivity extends AppCompatActivity {

    private User loggedUser;
    private LinearLayout layout;
    private ArrayList<Avviso> avvisiNascosti, avvisi;
    private AvvisiNascostiController avvisiNascostiController;
    private ImageView back_button;
    private FirebaseAnalytics analytics = MainActivity.analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avvisi_nascosti);

        firebaseLog();

        back_button = findViewById(R.id.back_button2);
        layout = findViewById(R.id.linear_layoutCard2);
        loggedUser = (User) getIntent().getSerializableExtra("loggedUser");
        avvisiNascostiController = new AvvisiNascostiController(this, loggedUser);
        avvisiNascosti = (ArrayList<Avviso>) getIntent().getSerializableExtra("avvisiNascosti");
        avvisi = (ArrayList<Avviso>) getIntent().getSerializableExtra("avvisi");

        for(Avviso a : avvisiNascosti) {
            for (Avviso avviso : avvisi) {
                if (avviso.getIdAvviso() == a.getIdAvviso())
                    generateCard(avviso);
            }
        }

        View.OnClickListener onClickBackToNoticeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avvisiNascostiController.goToNoticesFragment();
            }
        };
        back_button.setOnClickListener(onClickBackToNoticeListener);

    }

    private void firebaseLog() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "avvisiNascosti");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "avvisiNascosti_clicked");
        analytics.logEvent("InPaginaAvvisiNascosti", bundle);
    }

    private void generateCard(float factor, CardView card, String header, String textMessage, Avviso avviso) {
        card.setLayoutParams(new LinearLayout.LayoutParams((int)(322 * factor), (int)(112 * factor)));
        card.setBackgroundColor(Color.parseColor("#FFFBF3"));
        card.setElevation(0);

        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        constraintLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        constraintLayout.setBackgroundColor(Color.parseColor("#FFFBF3"));
        card.addView(constraintLayout);

        generateHeaderTextCard(factor, constraintLayout, header);
        generateShowMessageCard(factor, constraintLayout, textMessage);
        generateHideButtonSpaceCard(card, factor, constraintLayout, avviso);

        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        set.connect(R.id.header_message, ConstraintSet.BOTTOM, R.id.show_message, ConstraintSet.TOP);
        set.connect(R.id.header_message, ConstraintSet.END, R.id.layout_constraint, ConstraintSet.END);
        set.connect(R.id.header_message, ConstraintSet.START, R.id.layout_constraint, ConstraintSet.START);
        set.connect(R.id.header_message, ConstraintSet.TOP, R.id.layout_constraint, ConstraintSet.TOP);

        set.connect(R.id.show_message, ConstraintSet.BOTTOM, R.id.layout_constraint, ConstraintSet.BOTTOM);
        set.connect(R.id.show_message, ConstraintSet.END, R.id.hide_button, ConstraintSet.START);
        set.connect(R.id.show_message, ConstraintSet.START, R.id.layout_constraint, ConstraintSet.START);
        set.connect(R.id.show_message, ConstraintSet.TOP, R.id.header_message, ConstraintSet.BOTTOM);

        set.connect(R.id.spacing, ConstraintSet.BOTTOM, R.id.layout_constraint, ConstraintSet.BOTTOM);
        set.connect(R.id.spacing, ConstraintSet.END, R.id.layout_constraint, ConstraintSet.END);
        set.connect(R.id.spacing, ConstraintSet.START, R.id.show_message, ConstraintSet.END);
        set.connect(R.id.spacing, ConstraintSet.TOP, R.id.header_message, ConstraintSet.BOTTOM);

        set.connect(R.id.hide_button, ConstraintSet.BOTTOM, R.id.spacing, ConstraintSet.BOTTOM);
        set.connect(R.id.hide_button, ConstraintSet.END, R.id.spacing, ConstraintSet.END);
        set.connect(R.id.hide_button, ConstraintSet.START, R.id.spacing, ConstraintSet.START);
        set.connect(R.id.hide_button, ConstraintSet.TOP, R.id.spacing, ConstraintSet.TOP);

        set.applyTo(constraintLayout);
    }

    private void generateHideButtonSpaceCard(CardView card, float factor, ConstraintLayout constraintLayout, Avviso avviso) {
        TextView spacing = new TextView(this);
        spacing.setId(R.id.spacing);
        spacing.setBackgroundResource(R.drawable.square_bottom_rounded2);
        LinearLayout.LayoutParams layout_274 = new LinearLayout.LayoutParams((int)(81 * factor), (int)(93 * factor));
        spacing.setLayoutParams(layout_274);
        constraintLayout.addView(spacing);

        ImageView hide_button = new ImageView(this);
        hide_button.setId(R.id.hide_button);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avvisiNascostiController.markAsNotHideNotice(avviso.getIdAvviso());
                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) card.getLayoutParams();
                layoutParams.height = 0;
                avvisiNascosti.remove(avviso);
                card.setLayoutParams(layoutParams);
                firebaseLogNoticeHidden();
            }
        };
        hide_button.setOnClickListener(onClickListener);
        hide_button.setImageResource(R.drawable.show_button);
        ConstraintLayout.LayoutParams layout_325 = new ConstraintLayout.LayoutParams((int)(80 * factor), (int)(40 * factor));
        hide_button.setLayoutParams(layout_325);
        constraintLayout.addView(hide_button);

    }

    private void firebaseLogNoticeHidden() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "noticeSettedHidden");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "noticeHidden");
        analytics.logEvent("noticeSettedHidden", bundle);
    }

    private void generateShowMessageCard(float factor, ConstraintLayout constraintLayout, String textMessage) {
        TextView show_message = new TextView(this);
        show_message.setId(R.id.show_message);
        show_message.setBackgroundResource(R.drawable.square_bottom_rounded);
        show_message.setPaddingRelative(0, 0, (int) (10/this.getResources().getDisplayMetrics().density), 0);
        show_message.setPadding(10,0,0,0);
        show_message.setText(textMessage);
        LinearLayout.LayoutParams layout_343 = new LinearLayout.LayoutParams((int)(242 * factor), (int)(93 * factor));
        layout_343.setMarginEnd(2);
        show_message.setLayoutParams(layout_343);
        constraintLayout.addView(show_message);
    }

    private void generateHeaderTextCard(float factor, ConstraintLayout constraintLayout, String header) {
        TextView header_message = new TextView(this);
        header_message.setId(R.id.header_message);
        header_message.setBackgroundResource(R.drawable.square_top_rounded);
        header_message.setText(header);
        header_message.setPadding(10,0,0,0);
        header_message.setTextColor(Color.parseColor("#FFFFFF"));
        LinearLayout.LayoutParams layout_566 = new LinearLayout.LayoutParams((int)(322 * factor), 0);
        header_message.setLayoutParams(layout_566);
        constraintLayout.addView(header_message);
    }

    public void generateCard(Avviso avviso) {
        float factor = this.getResources().getDisplayMetrics().density;
        CardView card = new CardView(this);
        generateCard(factor, card, avviso.getAutore() + " " + avviso.getDataOra(), avviso.getTesto(), avviso);
        layout.addView(card);
    }

    public void removeFromAvvisiNascosti(Integer id_avviso){
        Iterator<Avviso> it = avvisiNascosti.iterator();
        while(it.hasNext()){
            Avviso a = (Avviso) it.next();
            if(a.getIdAvviso() == id_avviso){
                it.remove();
            }
        }
    }
}