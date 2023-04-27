package ru.pad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAssigningTestsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView textViewTestName;

    public boolean itemIsChecked;

    private final RecyclerViewAssigningTestsAdapter.OnItemListener onItemListener;

    public RecyclerViewAssigningTestsHolder(@NonNull View itemView, RecyclerViewAssigningTestsAdapter.OnItemListener onItemListener) {
        super(itemView);
        textViewTestName = itemView.findViewById(R.id.textViewTestName);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(this);
    }
}
