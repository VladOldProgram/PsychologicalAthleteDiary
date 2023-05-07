package ru.pad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Класс объекта адаптера виджета recyclerview спортсменов
 */
public class RecyclerViewSportsmenAdapter extends RecyclerView.Adapter<RecyclerViewSportsmenHolder> {
    public final ArrayList<String> sportsmenNameSurnameEmail;

    private final String psychologistUid;

    private final Context context;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param sportsmenNameSurnameEmail список имен-фамилий-email спортсменов
     * @param psychologistUid user id психолога
     * @param context контекст приложения, где создавать список
     */
    public RecyclerViewSportsmenAdapter(ArrayList<String> sportsmenNameSurnameEmail, String psychologistUid, Context context) {
        this.sportsmenNameSurnameEmail = sportsmenNameSurnameEmail;
        this.psychologistUid = psychologistUid;
        this.context = context;
    }

    /**
     * Добавляет новый элемент в список спортсменов
     * @param parent группа виджетов, в которую будет добавлен новый виджет после того, как он будет привязан к позиции адаптера
     * @param viewType тип нового виджета
     * @return новый объект холдера элемента списка спортсменов
     */
    @NonNull
    @Override
    public RecyclerViewSportsmenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_sportsmen_item, parent, false);
        return new RecyclerViewSportsmenHolder(view).linkAdapter();
    }

    /**
     * Реализует открытие психологом активности профиля выбранного из списка спортсмена
     * @param holder холдер элемента списка спортсменов
     * @param position позиция элемента в наборе элементов адаптера
     */
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

    /**
     * @return количество элементов в списке спортсменов
     */
    @Override
    public int getItemCount() {
        return sportsmenNameSurnameEmail.size();
    }
}
