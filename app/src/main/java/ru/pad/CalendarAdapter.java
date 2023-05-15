package ru.pad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Класс объекта адаптера виджета recyclerview календаря
 */
public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;

    private final ArrayList<int[]> marksOfMonth;

    private final OnItemListener onItemListener;

    private final String nextMonthYear;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param daysOfMonth массив номеров дней месяца
     * @param marksOfMonth массив меток дней месяца
     * @param onItemListener прослушиватель событий элемента сетки календаря
     * @param nextMonthYear выбранная дата для отображения в календаре
     */
    public CalendarAdapter(ArrayList<String> daysOfMonth, ArrayList<int[]> marksOfMonth, OnItemListener onItemListener, String nextMonthYear) {
        this.daysOfMonth = daysOfMonth;
        this.marksOfMonth = marksOfMonth;
        this.onItemListener = onItemListener;
        this.nextMonthYear = nextMonthYear;
    }

    /**
     * Добавляет новый элемент в сетку календарь
     * @param parent группа виджетов, в которую будет добавлен новый виджет после того, как он будет привязан к позиции адаптера
     * @param viewType тип нового виджета
     * @return новый объект холдера элемента сетки календаря
     */
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.16666);
        return new CalendarViewHolder(view, onItemListener);
    }

    /**
     * Устанавливает номер и метки дня для нового элемента сетки календаря, красит текущий день в серый цвет
     * @param holder холдер элемента сетки календаря
     * @param position позиция элемента в наборе элементов адаптера
     */
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.dayOfMonth.setText(daysOfMonth.get(position));

        int[] marksWidths = marksOfMonth.get(position);
        holder.imageViewAvailableTestMark.getLayoutParams().width = marksWidths[0];
        holder.imageViewAvailableTestMark.requestLayout();
        holder.imageViewCompletedTestMark.getLayoutParams().width = marksWidths[1];
        holder.imageViewCompletedTestMark.requestLayout();
        holder.imageViewNotCompletedTestMark.getLayoutParams().width = marksWidths[2];
        holder.imageViewNotCompletedTestMark.requestLayout();
        holder.imageViewPlannedTestMark.getLayoutParams().width = marksWidths[3];
        holder.imageViewPlannedTestMark.requestLayout();
        holder.imageViewNoteMark.getLayoutParams().width = marksWidths[4];
        holder.imageViewNoteMark.requestLayout();

        if (nextMonthYear.equals(LocalDate.now().getMonth().toString() + LocalDate.now().getYear())
                && holder.dayOfMonth.getText().toString().equals(String.valueOf(LocalDate.now().getDayOfMonth()))) {
            holder.itemView.setBackgroundColor(0xFFE3E3E3);
        }
    }

    /**
     * @return количество элементов сетки календаря
     */
    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    /**
     * Интерфейс для обработки событий элемента сетки календаря в другом модуле
     */
    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }
}
