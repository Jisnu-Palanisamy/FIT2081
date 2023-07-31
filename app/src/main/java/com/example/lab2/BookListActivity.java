package com.example.lab2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.provider.BookViewModel;

public class BookListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private BookViewModel viewModel;
    MyRecyclerViewAdapter bookListAdapter;
    EditText filterTitle;
    Button filterButton, clearFilterButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        filterTitle = findViewById(R.id.filterTitle);
        filterButton = findViewById(R.id.btnFilter);
        clearFilterButton = findViewById(R.id.btnClearFilter);
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);
        viewModel.getAllBooks().observe(this, newListOfAddedBooks -> {
            bookListAdapter.setData(newListOfAddedBooks);
            bookListAdapter.notifyDataSetChanged();
        });

        recyclerView = findViewById(R.id.bookListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookListAdapter = new MyRecyclerViewAdapter();
        recyclerView.setAdapter(bookListAdapter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleValue = filterTitle.getText().toString();
                viewModel.getBooksByFilter(titleValue);
                Toast.makeText(getApplicationContext(), "Successfully filtered!", Toast.LENGTH_SHORT).show();
            }
        });

        clearFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterTitle.setText("");
                Toast.makeText(getApplicationContext(), "Successfully cleared!", Toast.LENGTH_SHORT).show();
                viewModel.getBooksByFilter("");
            }
        });
    }
}
