package ru.pad;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Класс объекта адаптера виджета recyclerview списка тестов для назначения психологом
 */
public class RecyclerViewAssigningTestsAdapter extends RecyclerView.Adapter<RecyclerViewAssigningTestsHolder> {
    private final ArrayList<String> testNames;

    private final SparseBooleanArray selectedItems;

    private final RecyclerViewAssigningTestsAdapter.OnItemListener onItemListener;

    /**
     * Конструктор - создание нового объекта с заданными свойствами
     * @param testNames массив названий тестов для назначения
     * @param onItemListener прослушиватель событий элемента списка тестов
     */
    public RecyclerViewAssigningTestsAdapter(ArrayList<String> testNames, OnItemListener onItemListener) {
        this.testNames = testNames;
        this.onItemListener = onItemListener;
        selectedItems = new SparseBooleanArray();
    }

    /**
     * Переключает текущее состояние (выбран/не выбран) для элемента списка тестов на позиции <b>position</b>
     * @param position позиция элемента в наборе элементов адаптера
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    /**
     * @return массив позиций выбранных элементов списка тестов
     */
    public ArrayList<Integer> getSelectedItems() {
        ArrayList<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }

        return items;
    }

    /**
     * @param position позиция элемента в наборе элементов адаптера
     * @return true, если элемент выбран, иначе - false
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Добавляет новый элемент в список тестов
     * @param parent группа виджетов, в которую будет добавлен новый виджет после того, как он будет привязан к позиции адаптера
     * @param viewType тип нового виджета
     * @return новый объект холдера элемента списка тестов
     */
    @NonNull
    @Override
    public RecyclerViewAssigningTestsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_assignig_tests_item, parent, false);
        return new RecyclerViewAssigningTestsHolder(view, onItemListener);
    }

    /**
     * Устанавливает название теста для нового элемента списка тестов,
     * красит элемент в серый, если он выбран, иначе - в розовый
     * @param holder холдер элемента списка тестов
     * @param position позиция элемента в наборе элементов адаптера
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAssigningTestsHolder holder, int position) {
        holder.textViewTestName.setText(testNames.get(position));
        if (isSelected(position)) {
            holder.textViewTestName.setBackgroundColor(0xFFA0A0A0);
        } else {
            holder.textViewTestName.setBackgroundColor(0xFFF85E6F);
        }
    }

    /**
     * @return количество элементов списка тестов
     */
    @Override
    public int getItemCount() {
        return testNames.size();
    }

    /**
     * Интерфейс для обработки событий элемента списка тестов в другом модуле
     */
    public interface OnItemListener {
        void onItemClick(RecyclerViewAssigningTestsHolder holder);
    }
}
