package ru.pad;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Класс объекта холдера элемента списка спортсменов
 */
public class RecyclerViewSportsmenHolder extends RecyclerView.ViewHolder {
    public final Button buttonSportsmanNameSurnameEmail;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param itemView виджет для инициализации
     */
    public RecyclerViewSportsmenHolder(@NonNull View itemView) {
        super(itemView);
        buttonSportsmanNameSurnameEmail = itemView.findViewById(R.id.buttonSportsmanNameSurnameEmail);
    }

    /**
     * Связывает адаптер с холдером
     * @return объект этого класса холдера
     */
    public RecyclerViewSportsmenHolder linkAdapter() {
        return this;
    }
}
