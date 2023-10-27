package com.example.ratatouille_android.views.jfragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ratatouille_android.R;
import com.example.ratatouille_android.controllers.NoticesController;
import com.example.ratatouille_android.models.Avviso;
import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.HomeActivity;
import com.example.ratatouille_android.views.MainActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;


public class NoticesFragment extends Fragment {
    private HomeActivity homeActivity;
    private NoticesController noticesController;
    private View view;
    private User loggedUser;
    private LinearLayout layout;
    private ArrayList<Avviso> avviso = new ArrayList<Avviso>();
    private Button messaggiNascostiButton;
    private FirebaseAnalytics analytics = MainActivity.analytics;

    public NoticesFragment(HomeActivity homeActivity, User loggedUser){
        this.homeActivity = homeActivity;
        this.loggedUser = loggedUser;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notices, container, false);
        layout = view.findViewById(R.id.linear_layoutCard);
        messaggiNascostiButton = view.findViewById(R.id.messaggi_nascosti_button);

        firebaseLog();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticesController.goToHideMessageActivity();
            }
        };

        messaggiNascostiButton.setOnClickListener(onClickListener);
        noticesController = new NoticesController(homeActivity, loggedUser);

        return view;
    }

    private void firebaseLog() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "avvisiFragment");
        bundle.putString(FirebaseAnalytics.Param.VALUE , "avvisi_clicked");
        analytics.logEvent("InAvvisiFragment", bundle);
    }

    private void generateCard(float factor, CardView card, String header, String textMessage, boolean isRead, Integer id_avviso) {
        card.setLayoutParams(new LinearLayout.LayoutParams((int)(322 * factor), (int)(112 * factor)));
        card.setBackgroundColor(Color.parseColor("#FFFBF3"));
        card.setElevation(0);

        ConstraintLayout constraintLayout = new ConstraintLayout(homeActivity);
        constraintLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        constraintLayout.setBackgroundColor(Color.parseColor("#FFFBF3"));
        card.addView(constraintLayout);

        generateHeaderTextCard(factor, constraintLayout, header);
        generateShowMessageCard(factor, constraintLayout, textMessage);
        generateHideButtonSpaceCard(card, factor, constraintLayout, isRead, id_avviso);


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

        set.connect(R.id.read_or_not, ConstraintSet.BOTTOM, R.id.spacing, ConstraintSet.BOTTOM);
        set.connect(R.id.read_or_not, ConstraintSet.END, R.id.spacing, ConstraintSet.END);
        set.connect(R.id.read_or_not, ConstraintSet.START, R.id.spacing, ConstraintSet.START);
        set.connect(R.id.read_or_not, ConstraintSet.TOP, R.id.hide_button, ConstraintSet.BOTTOM);

        set.applyTo(constraintLayout);
    }

    private void generateHideButtonSpaceCard(CardView card, float factor, ConstraintLayout constraintLayout, boolean isRead, Integer id_avviso) {
        TextView spacing = new TextView(homeActivity);
        spacing.setId(R.id.spacing);
        spacing.setBackgroundResource(R.drawable.square_bottom_rounded2);
        LinearLayout.LayoutParams layout_274 = new LinearLayout.LayoutParams((int)(81 * factor), (int)(93 * factor));
        spacing.setLayoutParams(layout_274);
        constraintLayout.addView(spacing);

        ImageView hide_button = new ImageView(homeActivity);
        hide_button.setId(R.id.hide_button);
        hide_button.setImageResource(R.drawable.nascondi_button);
        ConstraintLayout.LayoutParams layout_325 = new ConstraintLayout.LayoutParams((int)(80 * factor), (int)(40 * factor));
        hide_button.setLayoutParams(layout_325);
        constraintLayout.addView(hide_button);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticesController.markAsHideNotice(id_avviso);
                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) card.getLayoutParams();
                layoutParams.height = 0;
                card.setLayoutParams(layoutParams);
            }
        };
        hide_button.setOnClickListener(onClickListener);

        CheckBox read_or_not = new CheckBox(homeActivity);
        read_or_not.setId(R.id.read_or_not);
        read_or_not.setChecked(isRead);

        read_or_not.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    if(isChecked){
                        noticesController.markAsReadNotice(id_avviso);
                    }else{
                        noticesController.markAsNotReadNotice(id_avviso);
                    }
                }
            }
        );


        ConstraintLayout.LayoutParams layout_837 = new ConstraintLayout.LayoutParams((int)(33 * factor),(int)(33 * factor));
        read_or_not.setLayoutParams(layout_837);
        constraintLayout.addView(read_or_not);
    }

    private void generateShowMessageCard(float factor, ConstraintLayout constraintLayout, String textMessage) {
        TextView show_message = new TextView(homeActivity);
        show_message.setId(R.id.show_message);
        show_message.setBackgroundResource(R.drawable.square_bottom_rounded);
        show_message.setPaddingRelative(0, 0, (int) (10/homeActivity.getResources().getDisplayMetrics().density), 0);
        show_message.setPadding(10,0,0,0);
        show_message.setText(textMessage);
        LinearLayout.LayoutParams layout_343 = new LinearLayout.LayoutParams((int)(242 * factor), (int)(93 * factor));
        layout_343.setMarginEnd(2);
        show_message.setLayoutParams(layout_343);
        constraintLayout.addView(show_message);
    }

    private void generateHeaderTextCard(float factor, ConstraintLayout constraintLayout, String header) {
        TextView header_message = new TextView(homeActivity);
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
        if(!avviso.isHidden()) {
            float factor = homeActivity.getResources().getDisplayMetrics().density;
            CardView card = new CardView(homeActivity);
            generateCard(factor, card, avviso.getAutore() + " " + avviso.getDataOra(), avviso.getTesto(), avviso.isRead(), avviso.getIdAvviso());
            layout.addView(card);
        }
    }


}