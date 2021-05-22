package ru.geekbrains.lesson10.observe;

import ru.geekbrains.lesson10.data.Note;

/**
 * @author Zurbaevi Nika
 */
public interface Observer {
    void updateNote(Note note);
}
