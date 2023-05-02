package ru.pad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerViewRequestsHolder extends RecyclerView.ViewHolder {
    public final TextView textViewSportsmanNameSurnameEmail;

    private RecyclerViewRequestsAdapter recyclerViewRequestsAdapter;

    public RecyclerViewRequestsHolder(@NonNull View itemView, String psychologistUid) {
        super(itemView);
        textViewSportsmanNameSurnameEmail = itemView.findViewById(R.id.textViewSportsmanNameSurname);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUsers = database.getReference("Users");

        itemView.findViewById(R.id.buttonDeclineRequest).setOnClickListener(view -> {
            String sportsmanUid = recyclerViewRequestsAdapter.items.get(getAbsoluteAdapterPosition())
                    .split(" ")[2]
                    .replace(".", "•")
                    .replace("(", "")
                    .replace(")", "");
            dbUsers
                    .child(psychologistUid)
                    .child("requests")
                    .child(sportsmanUid)
                    .removeValue();
            dbUsers
                    .child(sportsmanUid)
                    .child("psychologist")
                    .setValue("declined");
            dbUsers
                    .child(sportsmanUid)
                    .child("requestAccepted")
                    .setValue(false);
            recyclerViewRequestsAdapter.items.remove(getAbsoluteAdapterPosition());
            recyclerViewRequestsAdapter.notifyItemRemoved(getAbsoluteAdapterPosition());
        });

        itemView.findViewById(R.id.buttonAcceptRequest).setOnClickListener(view -> {
            String sportsmanUid = recyclerViewRequestsAdapter.items.get(getAbsoluteAdapterPosition())
                    .split(" ")[2]
                    .replace(".", "•")
                    .replace("(", "")
                    .replace(")", "");
            dbUsers
                    .child(psychologistUid)
                    .child("requests")
                    .child(sportsmanUid)
                    .removeValue();
            dbUsers
                    .child(psychologistUid)
                    .child("sportsmen")
                    .child(sportsmanUid)
                    .setValue(sportsmanUid.replace("•", "."));
            dbUsers
                    .child(sportsmanUid)
                    .child("psychologist")
                    .setValue(psychologistUid.replace("•", "."));
            dbUsers
                    .child(sportsmanUid)
                    .child("requestAccepted")
                    .setValue(true);
            recyclerViewRequestsAdapter.items.remove(getAbsoluteAdapterPosition());
            recyclerViewRequestsAdapter.notifyItemRemoved(getAbsoluteAdapterPosition());
        });
    }

    public RecyclerViewRequestsHolder linkAdapter(RecyclerViewRequestsAdapter recyclerViewRequestsAdapter) {
        this.recyclerViewRequestsAdapter = recyclerViewRequestsAdapter;
        return this;
    }
}
