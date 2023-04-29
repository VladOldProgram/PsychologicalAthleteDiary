package ru.pad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

import ru.pad.objects.Note;

public class RecyclerViewActivitiesAdapter extends RecyclerView.Adapter<RecyclerViewActivitiesHolder> {
    private final ArrayList<String> itemNames, testUrls;

    private final ArrayList<Note> notes;

    private final String itemType;

    private final String sportsmanUid;

    private final String role;

    private final Context context;

    public RecyclerViewActivitiesAdapter(
            ArrayList<String> itemNames,
            ArrayList<String> testUrls,
            ArrayList<Note> notes,
            String itemType,
            String sportsmanUid,
            String role,
            Context context
    ) {
        this.itemNames = itemNames;
        this.testUrls = testUrls;
        this.notes = notes;
        this.itemType = itemType;
        this.sportsmanUid = sportsmanUid;
        this.role = role;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewActivitiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_tests_item, parent, false);
        return new RecyclerViewActivitiesHolder(view).linkAdapter();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewActivitiesHolder holder, int position) {
        holder.buttonActivityName.setText(itemNames.get(position));
        holder.buttonActivityName.setOnClickListener(view -> {
            if (itemType.equals("notes")) {
                // TODO: открытие заметки
            }
            else if ((itemType.equals("completedTests"))
                    || (itemType.equals("availableTests")
                        && role.equals("Спортсмен"))
            ) {
                Intent testViewActivity = new Intent(context, TestViewActivity.class);
                testViewActivity.putExtra("sportsmanUid", sportsmanUid);
                testViewActivity.putExtra("testUrl", testUrls.get(position));
                context.startActivity(testViewActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemNames.size();
    }
}
