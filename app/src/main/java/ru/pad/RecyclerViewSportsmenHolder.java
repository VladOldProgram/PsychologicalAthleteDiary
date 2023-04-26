package ru.pad;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewSportsmenHolder extends RecyclerView.ViewHolder {
    public final Button buttonSportsmanNameSurnameEmail;

    public RecyclerViewSportsmenHolder(@NonNull View itemView) {
        super(itemView);
        buttonSportsmanNameSurnameEmail = itemView.findViewById(R.id.buttonSportsmanNameSurnameEmail);
    }

    public RecyclerViewSportsmenHolder linkAdapter() {
        return this;
    }
}
