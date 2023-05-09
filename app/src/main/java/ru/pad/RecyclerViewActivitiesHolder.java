package ru.pad;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Класс объекта холдера элемента списка раздела активностей выбранного дня
 */
public class RecyclerViewActivitiesHolder extends RecyclerView.ViewHolder {
    public final Button buttonActivityName;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param itemView виджет для инициализации
     */
    public RecyclerViewActivitiesHolder(@NonNull View itemView) {
        super(itemView);
        buttonActivityName = itemView.findViewById(R.id.buttonActivityName);
    }

    /**
     * Связывает адаптер с холдером
     * @return объект этого класса холдера
     */
    public RecyclerViewActivitiesHolder linkAdapter() {
        return this;
    }
}
