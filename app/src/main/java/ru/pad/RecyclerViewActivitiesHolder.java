package ru.pad;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewActivitiesHolder extends RecyclerView.ViewHolder {
    public final Button buttonActivityName;

    public RecyclerViewActivitiesHolder(@NonNull View itemView) {
        super(itemView);
        buttonActivityName = itemView.findViewById(R.id.buttonActivityName);
    }

    public RecyclerViewActivitiesHolder linkAdapter() {
        return this;
    }
}
