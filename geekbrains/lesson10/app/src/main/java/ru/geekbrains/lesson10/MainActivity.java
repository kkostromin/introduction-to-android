package ru.geekbrains.lesson10;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

import ru.geekbrains.lesson10.observe.Publisher;
import ru.geekbrains.lesson10.ui.ListOfNotesFragment;

/**
 * @author Zurbaevi Nika
 */
public class MainActivity extends AppCompatActivity {
    private ru.geekbrains.lesson10.Navigation navigation;
    private Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = new ru.geekbrains.lesson10.Navigation(getSupportFragmentManager());
        initToolbar();
        getNavigation().addFragment(ListOfNotesFragment.newInstance(), false);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public ru.geekbrains.lesson10.Navigation getNavigation() {
        return navigation;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(ru.geekbrains.lesson10.MainActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        MenuItem sort = menu.findItem(R.id.menu_sort);
        sort.setOnMenuItemClickListener(item -> {
            Toast.makeText(ru.geekbrains.lesson10.MainActivity.this, R.string.menu_sort, Toast.LENGTH_SHORT).show();
            return true;
        });
        MenuItem send = menu.findItem(R.id.menu_send);
        send.setOnMenuItemClickListener(item -> {
            Toast.makeText(ru.geekbrains.lesson10.MainActivity.this, R.string.menu_send, Toast.LENGTH_SHORT).show();
            return true;
        });
        MenuItem addPhoto = menu.findItem(R.id.menu_add_photo);
        addPhoto.setOnMenuItemClickListener(item -> {
            Toast.makeText(ru.geekbrains.lesson10.MainActivity.this, R.string.menu_add_photo, Toast.LENGTH_SHORT).show();
            return true;
        });
        return true;
    }
}