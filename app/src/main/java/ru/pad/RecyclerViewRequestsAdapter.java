package ru.pad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewRequestsAdapter extends RecyclerView.Adapter<RecyclerViewRequestsHolder> {
    public final List<String> items;

    private final String psychologistUid;

    public RecyclerViewRequestsAdapter(List<String> items, String psychologistUid) {
        this.items = items;
        this.psychologistUid = psychologistUid;
    }

    @NonNull
    @Override
    public RecyclerViewRequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_requests_item, parent, false);
        return new RecyclerViewRequestsHolder(view, psychologistUid).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewRequestsHolder holder, int position) {
        holder.textViewSportsmanNameSurnameEmail.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
