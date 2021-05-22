package ru.geekbrains.lesson10.data;

/**
 * @author Zurbaevi Nika
 */
public interface NotesSourceInterface {

    NotesSourceInterface init(NotesSourceResponse notesSourceResponse);

    Note getNote(int position);

    int size();

    void deleteNote(int position);

    void changeNote(int position, Note note);

    void addNote(Note note);
}
