package ru.geekbrains.lesson11.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ru.geekbrains.lesson11.DeleteDialogFragment;
import ru.geekbrains.lesson11.MainActivity;
import ru.geekbrains.lesson11.Navigation;
import ru.geekbrains.lesson11.OnDeleteDialogListener;
import ru.geekbrains.lesson11.R;
import ru.geekbrains.lesson11.data.NotesSourceFirebase;
import ru.geekbrains.lesson11.data.NotesSourceInterface;
import ru.geekbrains.lesson11.observe.Publisher;

/**
 * @author Zurbaevi Nika
 */
public class ListOfNotesFragment extends Fragment {

    private NotesSourceInterface data;
    private ru.geekbrains.lesson11.ui.NotesAdapter adapter;
    private RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToFirstPosition;

    public static ru.geekbrains.lesson11.ui.ListOfNotesFragment newInstance() {
        return new ru.geekbrains.lesson11.ui.ListOfNotesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_notes, container, false);
        recyclerView = view.findViewById(R.id.notes_recycler_view);
        data = new NotesSourceFirebase().init(notesData -> adapter.notifyDataSetChanged());
        initRecyclerView(recyclerView, data);
        setHasOptionsMenu(true);
        adapter.setDataSource(data);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    private void initRecyclerView(RecyclerView recyclerView, NotesSourceInterface data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ru.geekbrains.lesson11.ui.NotesAdapter(this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration
                (requireContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull
                (ContextCompat.getDrawable(requireContext(), R.drawable.separator)));
        recyclerView.addItemDecoration(itemDecoration);

        if (moveToFirstPosition && data.size() > 0) {
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }
        adapter.setOnItemClickListener((position, note) -> {
            navigation.addFragment(ru.geekbrains.lesson11.ui.NoteFragment.newInstance(data.getNote(position)),
                    true);
            publisher.subscribe(note1 -> {
                data.changeNote(position, note1);
                adapter.notifyItemChanged(position);
            });
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        if (item.getItemId() == R.id.menu_delete_note) {
            DeleteDialogFragment deleteDlgFragment = new DeleteDialogFragment();
            deleteDlgFragment.setCancelable(false);
            deleteDlgFragment.setOnDialogListener(new OnDeleteDialogListener() {
                @Override
                public void onDelete() {
                    data.deleteNote(position);
                    adapter.notifyItemRemoved(position);
                    deleteDlgFragment.dismiss();
                }

                @Override
                public void onCancelDelete() {
                    deleteDlgFragment.dismiss();
                }
            });
            deleteDlgFragment.show(requireActivity().getSupportFragmentManager(),
                    "DeleteFragmentTag");
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem search = menu.findItem(R.id.menu_search);
        MenuItem addNote = menu.findItem(R.id.menu_add_note);
        search.setVisible(true);
        addNote.setOnMenuItemClickListener(item -> {
            navigation.addFragment(ru.geekbrains.lesson11.ui.NoteFragment.newInstance(), true);
            publisher.subscribe(note -> {
                data.addNote(note);
                adapter.notifyItemInserted(data.size() - 1);
                moveToFirstPosition = true;
            });
            return true;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}