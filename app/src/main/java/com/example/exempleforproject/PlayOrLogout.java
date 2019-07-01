package com.example.exempleforproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class PlayOrLogout extends DialogFragment {

    private String question;
    private PlayOrLogoutFragmentListener playOrLogoutFragmentListener;

    public void setQuestion(String question){
        this.question = question;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_play_or_logout, container, false);
        TextView lblQuestion = view.findViewById(R.id.lbltxtName);
        if(question != null)
            lblQuestion.setText(question);
        Button btnYes = view.findViewById(R.id.btnPlay);
        Button btnNo = view.findViewById(R.id.btnLogout);
        btnYes.setOnClickListener(listener);
        btnNo.setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isYes = false;
            switch (view.getId()) {
                case R.id.btnPlay:
                    isYes = true;
                    break;
                case R.id.btnLogout:
                    isYes = false;
                    break;
            }
            if(playOrLogoutFragmentListener != null)
                playOrLogoutFragmentListener.onChoose(isYes);

            dismiss();
        }
    };

    public void setPlayOrLogoutFragmentListener(PlayOrLogoutFragmentListener playOrLogoutFragmentListener) {
        this.playOrLogoutFragmentListener = playOrLogoutFragmentListener;
    }

    public interface PlayOrLogoutFragmentListener{
        void onChoose(boolean isYes);
    }
}
