package ru.pad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewSportsmenAdapter extends RecyclerView.Adapter<RecyclerViewSportsmenHolder> {
    public final ArrayList<String> sportsmenNameSurnameEmail;

    private final String psychologistUid;

    private final Context context;

    public RecyclerViewSportsmenAdapter(ArrayList<String> sportsmenNameSurnameEmail, String psychologistUid, Context context) {
        this.sportsmenNameSurnameEmail = sportsmenNameSurnameEmail;
        this.psychologistUid = psychologistUid;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewSportsmenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_sportsmen_item, parent, false);
        return new RecyclerViewSportsmenHolder(view).linkAdapter();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewSportsmenHolder holder, int position) {
        holder.buttonSportsmanNameSurnameEmail.setText(sportsmenNameSurnameEmail.get(position));
        String sportsmanUid = holder.buttonSportsmanNameSurnameEmail.getText().toString()
                .split(" ")[2]
                .replace("(", "")
                .replace(")", "")
                .replace(".", "•");
        holder.buttonSportsmanNameSurnameEmail.setOnClickListener(view -> {
            Intent selectedSportsmanProfileActivity = new Intent(context, SelectedSportsmanProfileActivity.class);
            selectedSportsmanProfileActivity.putExtra("psychologistUid", psychologistUid);
            selectedSportsmanProfileActivity.putExtra("sportsmanUid", sportsmanUid);
            context.startActivity(selectedSportsmanProfileActivity);
        });
    }

    @Override
    public int getItemCount() {
        return sportsmenNameSurnameEmail.size();
    }
}
