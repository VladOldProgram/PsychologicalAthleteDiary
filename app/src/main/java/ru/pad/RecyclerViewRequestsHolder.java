package ru.pad;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Класс объекта холдера элемента списка заявок
 */
public class RecyclerViewRequestsHolder extends RecyclerView.ViewHolder {
    public final TextView textViewSportsmanNameSurnameEmail;

    private RecyclerViewRequestsAdapter recyclerViewRequestsAdapter;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param itemView виджет для инициализации
     * @param psychologistUid user id психолога
     */
    public RecyclerViewRequestsHolder(@NonNull View itemView, String psychologistUid) {
        super(itemView);
        textViewSportsmanNameSurnameEmail = itemView.findViewById(R.id.textViewSportsmanNameSurname);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUsers = database.getReference("Users");

        /*
          Привязка к кнопке функции отклонения психологом заявки спортсмена
         */
        itemView.findViewById(R.id.buttonDeclineRequest).setOnClickListener(view -> {
            String sportsmanUid = recyclerViewRequestsAdapter.sportsmenNameSurnameEmail.get(getAbsoluteAdapterPosition())
                    .split(" ")[2]
                    .replace(".", "•")
                    .replace("(", "")
                    .replace(")", "");
            /*
              Запрос к БД на удаление заявки спортсмена из списка заявок
             */
            AtomicBoolean noException = new AtomicBoolean(true);
            dbUsers
                    .child(psychologistUid)
                    .child("requests")
                    .child(sportsmanUid)
                    .removeValue()
                    .addOnFailureListener(e -> {
                        noException.set(false);
                    });
            /*
              Запрос к БД на установление значения declined статусу заявки спортсмена
             */
            dbUsers
                    .child(sportsmanUid)
                    .child("psychologist")
                    .setValue("declined")
                    .addOnFailureListener(e -> {
                        noException.set(false);
                    });
            /*
              Запрос к БД на установление значения false статусу заявки спортсмена
             */
            dbUsers
                    .child(sportsmanUid)
                    .child("requestAccepted")
                    .setValue(false)
                    .addOnFailureListener(e -> {
                        noException.set(false);
                    });
            if (noException.get()) {
                recyclerViewRequestsAdapter.sportsmenNameSurnameEmail.remove(getAbsoluteAdapterPosition());
                recyclerViewRequestsAdapter.notifyItemRemoved(getAbsoluteAdapterPosition());
            }
        });

        /*
          Привязка к кнопке функции принятия психологом заявки спортсмена
         */
        itemView.findViewById(R.id.buttonAcceptRequest).setOnClickListener(view -> {
            String sportsmanUid = recyclerViewRequestsAdapter.sportsmenNameSurnameEmail.get(getAbsoluteAdapterPosition())
                    .split(" ")[2]
                    .replace(".", "•")
                    .replace("(", "")
                    .replace(")", "");
            /*
              Запрос к БД на удаления заявки спортсмена из списка заявок
             */
            AtomicBoolean noException = new AtomicBoolean(true);
            dbUsers
                    .child(psychologistUid)
                    .child("requests")
                    .child(sportsmanUid)
                    .removeValue()
                    .addOnFailureListener(e -> {
                        noException.set(false);
                    });
            /*
              Запрос к БД на добавление спортсмена в список спортсменов психолога
             */
            dbUsers
                    .child(psychologistUid)
                    .child("sportsmen")
                    .child(sportsmanUid)
                    .setValue(sportsmanUid.replace("•", "."))
                    .addOnFailureListener(e -> {
                        noException.set(false);
                    });
            /*
              Запрос к БД на добавление психолога спортсмену
             */
            dbUsers
                    .child(sportsmanUid)
                    .child("psychologist")
                    .setValue(psychologistUid.replace("•", "."))
                    .addOnFailureListener(e -> {
                        noException.set(false);
                    });
            /*
              Запрос к БД на установление значения true статусу заявки спортсмена
             */
            dbUsers
                    .child(sportsmanUid)
                    .child("requestAccepted")
                    .setValue(true)
                    .addOnFailureListener(e -> {
                        noException.set(false);
                    });
            if (noException.get()) {
                recyclerViewRequestsAdapter.sportsmenNameSurnameEmail.remove(getAbsoluteAdapterPosition());
                recyclerViewRequestsAdapter.notifyItemRemoved(getAbsoluteAdapterPosition());
            }
        });
    }

    /**
     * Связывает адаптер с холдером
     * @param recyclerViewRequestsAdapter адаптер списка заявок
     * @return объект этого класса холдера
     */
    public RecyclerViewRequestsHolder linkAdapter(RecyclerViewRequestsAdapter recyclerViewRequestsAdapter) {
        this.recyclerViewRequestsAdapter = recyclerViewRequestsAdapter;
        return this;
    }
}
