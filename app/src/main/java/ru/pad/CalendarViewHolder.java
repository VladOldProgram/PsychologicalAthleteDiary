package ru.pad;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Класс объекта холдера элемента сетки календаря
 */
public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView dayOfMonth;

    public final ImageView imageViewAvailableTestMark, imageViewCompletedTestMark,
            imageViewNotCompletedTestMark, imageViewPlannedTestMark, imageViewNoteMark;

    private final CalendarAdapter.OnItemListener onItemListener;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param itemView виджет для инициализации
     * @param onItemListener прослушиватель событий элемента сетки календаря
     */
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        dayOfMonth = itemView.findViewById(R.id.textViewCellDay);
        imageViewAvailableTestMark = itemView.findViewById(R.id.imageViewAvailableTestMark);
        imageViewCompletedTestMark = itemView.findViewById(R.id.imageViewCompletedTestMark);
        imageViewNotCompletedTestMark = itemView.findViewById(R.id.imageViewNotCompletedTestMark);
        imageViewPlannedTestMark = itemView.findViewById(R.id.imageViewAssignedTestMark);
        imageViewNoteMark = itemView.findViewById(R.id.imageViewNoteMark);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    /**
     * По нажатию на конкретный элемент сетки календаря вызывается метод <b>onItemClick</b>
     * @param view виджет, на который осуществлено нажатие
     * @see CalendarAdapter
     */
    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAbsoluteAdapterPosition(), dayOfMonth.getText().toString());
    }
}
