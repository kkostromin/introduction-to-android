package ru.geekbrains.lesson11.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import ru.geekbrains.lesson11.MainActivity;
import ru.geekbrains.lesson11.R;
import ru.geekbrains.lesson11.data.Note;
import ru.geekbrains.lesson11.observe.Publisher;

/**
 * @author Zurbaevi Nika
 */
public class NoteFragment extends Fragment {

    public static final String CURRENT_NOTE = "currentNote";
    private Note note;
    private Publisher publisher;

    private EditText titleText;
    private EditText contentText;
    private Button mButtonSave;
    private TextView dateOfCreationText;
    private String dateOfCreation;
    private boolean isNewNote = false;

    public static ru.geekbrains.lesson11.ui.NoteFragment newInstance(Note note) {
        ru.geekbrains.lesson11.ui.NoteFragment fragment = new ru.geekbrains.lesson11.ui.NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public static ru.geekbrains.lesson11.ui.NoteFragment newInstance() {
        return new NoteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(CURRENT_NOTE);
        }
        if (note == null) {
            isNewNote = true;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        initView(view);
        if (note != null) {
            dateOfCreation = note.getCreationDate();
            populateView(view);
        }
        if (isNewNote) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                    Locale.getDefault());
            dateOfCreation = String.format("%s: %s", "Data created",
                    formatter.format(Calendar.getInstance().getTime()));
            populateView(view);
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        note = collectNote();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }

    private Note collectNote() {
        String title = Objects.requireNonNull(this.titleText.getText()).toString();
        String content = Objects.requireNonNull(this.contentText.getText()).toString();
        if (isNewNote) {
            isNewNote = false;
        }
        if (note != null) {
            Note answer = new Note(title, content, dateOfCreation);
            answer.setId(note.getId());
            return answer;
        } else {
            return new Note(title, content, dateOfCreation);
        }
    }

    private void initView(View view) {
        titleText = view.findViewById(R.id.edit_title_id);
        contentText = view.findViewById(R.id.edit_description_id);
        dateOfCreationText = view.findViewById(R.id.note_date_of_creation);
    }

    private void populateView(View view) {
        if (isNewNote) {
            dateOfCreationText.setText(dateOfCreation);
        } else {
            dateOfCreationText.setText(note.getCreationDate());
            titleText.setText(note.getTitle());
            contentText.setText(note.getContent());
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem addNote = menu.findItem(R.id.menu_add_note);
        MenuItem search = menu.findItem(R.id.menu_search);
        addNote.setVisible(false);
        search.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}