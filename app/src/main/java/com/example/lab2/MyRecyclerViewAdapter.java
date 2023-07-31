package com.example.lab2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2.provider.bookItem;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    List<bookItem>listOfAddedBooks = new ArrayList<>();

    public MyRecyclerViewAdapter(){}

    public void setData(List<bookItem> listOfAddedBooks) {
        this.listOfAddedBooks = listOfAddedBooks;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.myText1.setText(listOfAddedBooks.get(position).getBookID());
        holder.myText2.setText(listOfAddedBooks.get(position).getBookTitle());
        holder.myText3.setText(listOfAddedBooks.get(position).getBookISBN());
        holder.myText4.setText(listOfAddedBooks.get(position).getBookAuthor());
        holder.myText5.setText(listOfAddedBooks.get(position).getBookDesc());
        holder.myText6.setText(listOfAddedBooks.get(position).getBookPrice());
        final int fPosition = position;
    }

    @Override
    public int getItemCount() {
        return listOfAddedBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView myText1;
        public TextView myText2;
        public TextView myText3;
        public TextView myText4;
        public TextView myText5;
        public TextView myText6;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            myText1 = itemView.findViewById(R.id.book_id);
            myText2 = itemView.findViewById(R.id.book_title);
            myText3 = itemView.findViewById(R.id.book_ISBN);
            myText4 = itemView.findViewById(R.id.book_author);
            myText5 = itemView.findViewById(R.id.book_desc);
            myText6 = itemView.findViewById(R.id.book_price);
        }
    }
}
