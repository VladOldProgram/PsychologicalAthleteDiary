package ru.pad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Класс объекта холдера элемента списка тестов для назначения психологом
 */
public class RecyclerViewAssigningTestsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView textViewTestName;

    private final RecyclerViewAssigningTestsAdapter.OnItemListener onItemListener;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param itemView виджет для инициализации
     * @param onItemListener прослушиватель событий элемента списка тестов
     */
    public RecyclerViewAssigningTestsHolder(@NonNull View itemView, RecyclerViewAssigningTestsAdapter.OnItemListener onItemListener) {
        super(itemView);
        textViewTestName = itemView.findViewById(R.id.textViewTestName);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    /**
     * По нажатию на конкретный элемент списка тестов вызывается метод <b>onItemClick</b>
     * @param view виджет, на который осуществлено нажатие
     * @see CalendarAdapter
     */
    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(this);
    }
}
