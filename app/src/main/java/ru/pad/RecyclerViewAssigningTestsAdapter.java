package ru.pad;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAssigningTestsAdapter extends RecyclerView.Adapter<RecyclerViewAssigningTestsHolder> {
    public final List<String> testNames;

    private final SparseBooleanArray selectedItems;

    private final RecyclerViewAssigningTestsAdapter.OnItemListener onItemListener;

    public RecyclerViewAssigningTestsAdapter(List<String> testNames, OnItemListener onItemListener) {
        this.testNames = testNames;
        this.onItemListener = onItemListener;
        selectedItems = new SparseBooleanArray();
    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }

        return items;
    }

    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    @NonNull
    @Override
    public RecyclerViewAssigningTestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_assignig_tests_item, parent, false);
        return new RecyclerViewAssigningTestsHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAssigningTestsHolder holder, int position) {
        holder.textViewTestName.setText(testNames.get(position));
        if (isSelected(position)) {
            holder.textViewTestName.setBackgroundColor(0xFFA0A0A0);
        } else {
            holder.textViewTestName.setBackgroundColor(0xFFF85E6F);
        }
    }

    @Override
    public int getItemCount() {
        return testNames.size();
    }

    public interface OnItemListener {
        void onItemClick(RecyclerViewAssigningTestsHolder holder);
    }
}
