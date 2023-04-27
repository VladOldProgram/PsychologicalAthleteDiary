package ru.pad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAssigningTestsAdapter extends RecyclerView.Adapter<RecyclerViewAssigningTestsHolder> {
    public final List<String> testNames;

    private final RecyclerViewAssigningTestsAdapter.OnItemListener onItemListener;

    public RecyclerViewAssigningTestsAdapter(List<String> testNames, OnItemListener onItemListener) {
        this.testNames = testNames;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public RecyclerViewAssigningTestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_assignig_tests_item, parent, false);
        view.setBackgroundColor(0xFFF85E6F);
        return new RecyclerViewAssigningTestsHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAssigningTestsHolder holder, int position) {
        holder.textViewTestName.setText(testNames.get(position));
    }

    @Override
    public int getItemCount() {
        return testNames.size();
    }

    public interface OnItemListener {
        void onItemClick(RecyclerViewAssigningTestsHolder recyclerViewAssigningTestsHolder);
    }
}
