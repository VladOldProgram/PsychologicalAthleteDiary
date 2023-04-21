package ru.pad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewSportsmenAdapter extends RecyclerView.Adapter<RecyclerViewSportsmenHolder> {
    public final List<String> items;

    private final String psychologistUid;

    private final Context context;

    public RecyclerViewSportsmenAdapter(List<String> items, String psychologistUid, Context context) {
        this.items = items;
        this.psychologistUid = psychologistUid;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewSportsmenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_sportsmen_item, parent, false);
        return new RecyclerViewSportsmenHolder(view, psychologistUid, context).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewSportsmenHolder holder, int position) {
        holder.buttonSportsmanNameSurnameEmail.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
