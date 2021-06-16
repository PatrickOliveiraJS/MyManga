package com.example.mymanga;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {
    CardView cardView;
    ProgressBar progressBar;
    TextView textView;
    String string;
    ConstraintLayout constraintLayout;
    Animation animation;

    ProgressButton(View view, Context context) {
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        cardView = view.findViewById(R.id.card_view);
        constraintLayout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progress_bar);
        textView = view.findViewById(R.id.text_view);
        string = textView.getText().toString();
    }

    void buttonActivate() {
        progressBar.setAnimation(animation);
        progressBar.setVisibility(View.VISIBLE);
        textView.setAnimation(animation);
        textView.setText(R.string.pb_loading);
    }

    void buttonFinished() {
        constraintLayout.setBackgroundColor(cardView.getResources().getColor(R.color.white));
        progressBar.setVisibility(View.GONE);
        textView.setText(string);
    }
}
