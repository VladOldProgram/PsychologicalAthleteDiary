package ru.pad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import ru.pad.objects.Note;

/**
 * Класс объекта активности написания заметок спортсменом
 */
public class NoteWritingActivity extends AppCompatActivity {
    private String sportsmanUid, role, selectedDayDate, noteName, situationText, thoughtsText,
            emotionsText, reactionsText, commentText;

    private int moodScore;

    private DatabaseReference dbUser;

    /**
     * Отображает эту активность на экране, вызывает методы <b>getUserData()</b>, <b>initWidgets()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getUserData()
     * @see #initWidgets()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_writing);
        getUserData();
        initWidgets();

        /*
          Если пользователь - спортсмен,
          то перегрузка метода обработки нажатия back: возвращение к активности профиля спортсмена
         */
        if (role.equals("Спортсмен")) {
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent sportsmanProfileActivity = new Intent(NoteWritingActivity.this, SportsmanProfileActivity.class);
                    sportsmanProfileActivity.putExtra("sportsmanUid", sportsmanUid);
                    startActivity(sportsmanProfileActivity);
                    overridePendingTransition(0, 0);
                    finish();
                }
            };
            getOnBackPressedDispatcher().addCallback(this, callback);
        }
    }

    /**
     * Сохраняет в текущем контексте данные, переданные из предыдущей активности
     */
    protected void getUserData() {
        /*
          Получение данных, переданных из предыдущей активности
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
            role = extras.getString("role");
            selectedDayDate = extras.getString("selectedDayDate");
            noteName = extras.getString("noteName");
            moodScore = extras.getInt("moodScore");
            situationText = extras.getString("situationText");
            thoughtsText = extras.getString("thoughtsText");
            emotionsText = extras.getString("emotionsText");
            reactionsText = extras.getString("reactionsText");
            commentText = extras.getString("commentText");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUser = database.getReference("Users/" + sportsmanUid);
    }

    /**
     * Инициализирует виджеты активности
     */
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility", "NonConstantResourceId"})
    protected void initWidgets() {
        SeekBar seekBarMood = findViewById(R.id.seekBarMood);
        TextView textViewMood = findViewById(R.id.textViewMood);

        EditText editTextSituation = findViewById(R.id.editTextSituation);
        EditText editTextThoughts = findViewById(R.id.editTextThoughts);
        EditText editTextEmotions = findViewById(R.id.editTextEmotions);
        EditText editTextReactions = findViewById(R.id.editTextReactions);

        /*
          Установление возможности пролистывания содержимого полей ввода заметки
         */
        editTextSituation.setMovementMethod(ScrollingMovementMethod.getInstance());
        editTextThoughts.setMovementMethod(ScrollingMovementMethod.getInstance());
        editTextEmotions.setMovementMethod(ScrollingMovementMethod.getInstance());
        editTextReactions.setMovementMethod(ScrollingMovementMethod.getInstance());

        editTextSituation.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
        editTextThoughts.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
        editTextEmotions.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
        editTextReactions.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        Button buttonSaveNote = findViewById(R.id.buttonSaveNote);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        /*
          Если это не просмотр созданной раннее заметки
         */
        if (noteName == null) {
            /*
              То инициализировать поля заметки стандартно
             */
            textViewMood.setText("Настроение: 0/10");
            seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    textViewMood.setText("Настроение: " + progress + "/10");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });

            /*
              Привязка к кнопке функции сохранения заметки
             */
            buttonSaveNote.setOnClickListener(unused1 -> {
                if (editTextSituation.getText().toString().equals("")
                        && editTextThoughts.getText().toString().equals("")
                        && editTextEmotions.getText().toString().equals("")
                        && editTextReactions.getText().toString().equals("")
                ) {
                    Toast.makeText(
                            NoteWritingActivity.this,
                            "Нужно заполнить хотя бы одно поле",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                /*
                  Название заметки составляется из слова "заметка" и локального времени сохранения заметки
                 */
                String currentTime = LocalTime.now().toString();
                int i = currentTime.lastIndexOf(".");
                currentTime = currentTime.substring(0, i);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                Note note = new Note(
                        seekBarMood.getProgress(),
                        editTextSituation.getText().toString(),
                        editTextThoughts.getText().toString(),
                        editTextEmotions.getText().toString(),
                        editTextReactions.getText().toString(),
                        ""
                );

                /*
                  Запрос к БД на сохранение заметки
                 */
                dbUser
                        .child("activity")
                        .child(LocalDate.now().format(formatter))
                        .child("notes")
                        .child("Заметка " + currentTime)
                        .setValue(note)
                        .addOnSuccessListener(unused2 -> {
                            /*
                              Обнуление полей заметки
                             */
                            seekBarMood.setProgress(0);
                            textViewMood.setText("Настроение: 0/10");
                            String hint = "Введите текст...";
                            editTextSituation.setText("");
                            editTextSituation.setHint(hint);
                            editTextSituation.setScrollY(0);
                            editTextThoughts.setText("");
                            editTextThoughts.setHint(hint);
                            editTextThoughts.setScrollY(0);
                            editTextEmotions.setText("");
                            editTextEmotions.setHint(hint);
                            editTextEmotions.setScrollY(0);
                            editTextReactions.setText("");
                            editTextReactions.setHint(hint);
                            editTextReactions.setScrollY(0);

                            Toast.makeText(
                                    NoteWritingActivity.this,
                                    "Заметка сохранена",
                                    Toast.LENGTH_SHORT
                            ).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(
                                    NoteWritingActivity.this,
                                    "Произошла ошибка при сохранении заметки (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
                        });
            });

            bottomNavigationView.setSelectedItemId(R.id.notes);
            /*
              Привязка к кнопкам меню функций перехода в выбранную активность
             */
            bottomNavigationView.setOnItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.calendar:
                        Intent calendarViewActivity = new Intent(NoteWritingActivity.this, CalendarViewActivity.class);
                        calendarViewActivity.putExtra("sportsmanUid", sportsmanUid);
                        calendarViewActivity.putExtra("role", "Спортсмен");
                        startActivity(calendarViewActivity);
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        finish();
                        return true;
                    case R.id.notes:
                        return true;
                    case R.id.profile:
                        Intent sportsmanProfileActivity = new Intent(NoteWritingActivity.this, SportsmanProfileActivity.class);
                        sportsmanProfileActivity.putExtra("sportsmanUid", sportsmanUid);
                        startActivity(sportsmanProfileActivity);
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        finish();
                        return true;
                }
                return false;
            });
            bottomNavigationView.setVisibility(View.VISIBLE);
        /*
          Иначе - это просмотр существующей заметки
         */
        } else {
            /*
              Спрятать seekBarMood и отобразить оценку настроения спортсмена
             */
            seekBarMood.setVisibility(View.GONE);
            textViewMood.setText("Настроение: " + moodScore + "/10");

            /*
              Запретить редактирование полей ввода просматриваемой заметки
             */
            editTextSituation.setFocusable(false);
            editTextThoughts.setFocusable(false);
            editTextEmotions.setFocusable(false);
            editTextReactions.setFocusable(false);

            /*
              Отобразить текстовые записи спортсмена
             */
            TextView textViewSituation = findViewById(R.id.textViewSituation);
            editTextSituation.setText(situationText);
            TextView textViewThoughts = findViewById(R.id.textViewThoughts);
            editTextThoughts.setText(thoughtsText);
            TextView textViewEmotions = findViewById(R.id.textViewEmotions);
            editTextEmotions.setText(emotionsText);
            TextView textViewReactions = findViewById(R.id.textViewReactions);
            editTextReactions.setText(reactionsText);

            /*
              Спрятать пустые текстовые поля заметки
             */
            if (editTextSituation.getText().toString().equals("")) {
                textViewSituation.setVisibility(View.GONE);
                editTextSituation.setVisibility(View.GONE);
            }
            if (editTextThoughts.getText().toString().equals("")) {
                textViewThoughts.setVisibility(View.GONE);
                editTextThoughts.setVisibility(View.GONE);
            }
            if (editTextEmotions.getText().toString().equals("")) {
                textViewEmotions.setVisibility(View.GONE);
                editTextEmotions.setVisibility(View.GONE);
            }
            if (editTextReactions.getText().toString().equals("")) {
                textViewReactions.setVisibility(View.GONE);
                editTextReactions.setVisibility(View.GONE);
            }

            TextView textViewComment = findViewById(R.id.textViewComment);
            EditText editTextComment = findViewById(R.id.editTextComment);
            editTextComment.setText(commentText);
            /*
              Для психолога спрятать поле комментария, если оно пустое
              (у спортсмена возможность редактировать есть всегда)
             */
            if (editTextComment.getText().toString().equals("")
                    && role.equals("Психолог")
            ) {
                textViewComment.setVisibility(View.GONE);
                editTextComment.setVisibility(View.GONE);
            } else {
                textViewComment.setVisibility(View.VISIBLE);
                editTextComment.setVisibility(View.VISIBLE);
            }
            /*
              Установление возможности пролистывания содержимого поля ввода комментария заметки
             */
            editTextComment.setMovementMethod(ScrollingMovementMethod.getInstance());
            editTextComment.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            });

            /*
              Спрятать навигационное меню
             */
            bottomNavigationView.setVisibility(View.GONE);

            /*
              Если пользователь - психолог, то спрятать кнопку сохранения заметки
             */
            if (role.equals("Психолог")) {
                editTextComment.setFocusable(false);
                buttonSaveNote.setVisibility(View.GONE);
            } else {
                /*
                  Иначе - привязка к кнопке функции сохранения заметки
                 */
                buttonSaveNote.setOnClickListener(unused1 -> {
                    /*
                      Запрос к БД на сохранение комментария спортсмена к заметке
                     */
                    dbUser
                            .child("activity")
                            .child(selectedDayDate)
                            .child("notes")
                            .child(noteName)
                            .child("commentText")
                            .setValue(editTextComment.getText().toString())
                            .addOnSuccessListener(unused2 -> {
                                Toast.makeText(
                                        NoteWritingActivity.this,
                                        "Комментарий к заметке сохранен",
                                        Toast.LENGTH_SHORT
                                ).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(
                                        NoteWritingActivity.this,
                                        "Произошла ошибка при сохранении комментария к заметке (" + e.getMessage() + ")",
                                        Toast.LENGTH_LONG
                                ).show();
                            });
                });
            }
        }
    }
}
