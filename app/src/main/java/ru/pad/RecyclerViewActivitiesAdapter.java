package ru.pad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ru.pad.objects.Note;

public class RecyclerViewActivitiesAdapter extends RecyclerView.Adapter<RecyclerViewActivitiesHolder> {
    private final ArrayList<String> itemNames, testUrls;

    private final ArrayList<Note> notes;

    private final String itemType, sportsmanUid, selectedDayDate, role;

    private final Context context;

    public RecyclerViewActivitiesAdapter(
            ArrayList<String> itemNames,
            ArrayList<String> testUrls,
            ArrayList<Note> notes,
            String itemType,
            String sportsmanUid,
            String selectedDayDate,
            String role,
            Context context
    ) {
        this.itemNames = itemNames;
        this.testUrls = testUrls;
        this.notes = notes;
        this.itemType = itemType;
        this.sportsmanUid = sportsmanUid;
        this.selectedDayDate = selectedDayDate;
        this.role = role;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewActivitiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_activities_item, parent, false);
        return new RecyclerViewActivitiesHolder(view).linkAdapter();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewActivitiesHolder holder, int position) {
        holder.buttonActivityName.setText(itemNames.get(position));
        holder.buttonActivityName.setOnClickListener(unused1 -> {
            if (itemType.equals("notes")) {
                Intent noteWritingActivity = new Intent(context, NoteWritingActivity.class);
                noteWritingActivity.putExtra("sportsmanUid", sportsmanUid);
                noteWritingActivity.putExtra("role", role);
                noteWritingActivity.putExtra("selectedDayDate", selectedDayDate);
                noteWritingActivity.putExtra("noteName", itemNames.get(position));
                noteWritingActivity.putExtra("moodScore", notes.get(position).getMoodScore());
                noteWritingActivity.putExtra("situationText", notes.get(position).getSituationText());
                noteWritingActivity.putExtra("thoughtsText", notes.get(position).getThoughtsText());
                noteWritingActivity.putExtra("emotionsText", notes.get(position).getEmotionsText());
                noteWritingActivity.putExtra("reactionsText", notes.get(position).getReactionsText());
                noteWritingActivity.putExtra("commentText", notes.get(position).getCommentText());
                context.startActivity(noteWritingActivity);
            }
            else if ((itemType.equals("completedTests"))
                    || (itemType.equals("availableTests") && role.equals("Спортсмен"))
            ) {
                Intent testViewActivity = new Intent(context, TestViewActivity.class);
                testViewActivity.putExtra("sportsmanUid", sportsmanUid);
                testViewActivity.putExtra("testName", itemNames.get(position));
                testViewActivity.putExtra("testUrl", testUrls.get(position));
                testViewActivity.putExtra("testType", itemType);
                context.startActivity(testViewActivity);
            }
            else if ((itemType.equals("availableTests") || itemType.equals("assignedTests"))
                    && (role.equals("Психолог"))
            ) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_delete_assigned_test);
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams wlp = dialogWindow.getAttributes();
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialogWindow.setAttributes(wlp);
                Button buttonCancelDelete = dialog.findViewById(R.id.buttonCancelDelete);
                buttonCancelDelete.setOnClickListener(unused2 -> {
                    dialog.dismiss();
                });
                Button buttonConfirmDelete = dialog.findViewById(R.id.buttonConfirmDelete);
                buttonConfirmDelete.setOnClickListener(unused3 -> {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dbUser = database.getReference("Users/" + sportsmanUid);
                    dbUser
                            .child("activity")
                            .child(selectedDayDate)
                            .child("availableTests")
                            .child(itemNames.get(position))
                            .removeValue();
                    dialog.dismiss();
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemNames.size();
    }
}
