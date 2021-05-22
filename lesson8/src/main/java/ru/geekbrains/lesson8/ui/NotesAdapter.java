package ru.geekbrains.lesson8.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.geekbrains.lesson8.Note;
import ru.geekbrains.lesson8.R;

/**
 * @author Zurbaevi Nika
 */
public class NotesAdapter extends RecyclerView.Adapter<ru.geekbrains.lesson8.ui.NotesAdapter.ViewHolder> {
    private Note[] notes;
    private MyClickListener myClickListener;

    public NotesAdapter(Note[] notes) {
        this.notes = notes;
    }

    public void setOnItemClickListener(MyClickListener itemClickListener) {
        myClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ru.geekbrains.lesson8.ui.NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ru.geekbrains.lesson8.ui.NotesAdapter.ViewHolder holder, int position) {
        holder.getItemLayout().setBackgroundColor(notes[position].getColor());
        holder.getTitleTextView().setText(notes[position].getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                Locale.getDefault());
        holder.getDateTextView().setText(formatter.format(notes[position].getCreationDate().getTime()));
    }

    @Override
    public int getItemCount() {
        return notes.length;
    }

    public interface MyClickListener {
        void onItemClick(int position, Note note);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private LinearLayout itemLayout;
        private TextView titleTextView;
        private TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            itemLayout = itemView.findViewById(R.id.element_of_recycler_view);
            titleTextView = itemView.findViewById(R.id.first_tv_of_item);
            dateTextView = itemView.findViewById(R.id.second_tv_of_item);
            itemLayout.setOnClickListener(v -> {
                int position = getAdapterPosition();
                myClickListener.onItemClick(position, notes[position]);
            });
        }

        public LinearLayout getItemLayout() {
            return itemLayout;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }
    }
}
