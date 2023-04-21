package ru.pad;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewSportsmenHolder extends RecyclerView.ViewHolder {
    public final Button buttonSportsmanNameSurnameEmail;

    public RecyclerViewSportsmenHolder(@NonNull View itemView, String psychologistUid, Context context) {
        super(itemView);
        buttonSportsmanNameSurnameEmail = itemView.findViewById(R.id.buttonSportsmanNameSurnameEmail);
        String sportsmanUid = buttonSportsmanNameSurnameEmail.getText().toString()
                .split(" ")[2]
                .replace("(", "")
                .replace(")", "");
        buttonSportsmanNameSurnameEmail.setOnClickListener(view -> {
            Intent selectedSportsmanProfileActivity = new Intent(context, SelectedSportsmanProfileActivity.class);
            selectedSportsmanProfileActivity.putExtra("psychologistUid", psychologistUid);
            selectedSportsmanProfileActivity.putExtra("sportsmanUid", sportsmanUid);
            context.startActivity(selectedSportsmanProfileActivity);
        });
    }

    public RecyclerViewSportsmenHolder linkAdapter(RecyclerViewSportsmenAdapter recyclerViewSportsmenAdapter) {
        return this;
    }
}
