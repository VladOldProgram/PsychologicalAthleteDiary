package ru.pad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Класс объекта адаптера виджета recyclerview заявок
 */
public class RecyclerViewRequestsAdapter extends RecyclerView.Adapter<RecyclerViewRequestsHolder> {
    public final ArrayList<String> sportsmenNameSurnameEmail;

    private final String psychologistUid;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param sportsmenNameSurnameEmail список имен-фамилий-email спортсменов
     * @param psychologistUid user id психолога
     */
    public RecyclerViewRequestsAdapter(ArrayList<String> sportsmenNameSurnameEmail, String psychologistUid) {
        this.sportsmenNameSurnameEmail = sportsmenNameSurnameEmail;
        this.psychologistUid = psychologistUid;
    }

    /**
     * Добавляет новый элемент в список заявок
     * @param parent группа виджетов, в которую будет добавлен новый виджет после того, как он будет привязан к позиции адаптера
     * @param viewType тип нового виджета
     * @return новый объект холдера элемента списка спортсменов
     */
    @NonNull
    @Override
    public RecyclerViewRequestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_requests_item, parent, false);
        return new RecyclerViewRequestsHolder(view, psychologistUid).linkAdapter(this);
    }

    /**
     * Устанавливает текст для нового элемента списка заявок
     * @param holder холдер элемента списка заявок
     * @param position позиция элемента в наборе элементов адаптера
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewRequestsHolder holder, int position) {
        holder.textViewSportsmanNameSurnameEmail.setText(sportsmenNameSurnameEmail.get(position));
    }

    /**
     * @return количество элементов в списке заявок
     */
    @Override
    public int getItemCount() {
        return sportsmenNameSurnameEmail.size();
    }
}
