package com.example.books;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Book> books;

    public BookListAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BookListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.ViewHolder holder, int position) {

        Book book = books.get(position);

        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthors());
        holder.publisher.setText(book.getPublisher());
        holder.date.setText(book.getPublishedDate());

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView author;
        public TextView publisher;
        public TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            publisher = (TextView) itemView.findViewById(R.id.publisher);
            date = (TextView) itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Book book = books.get(position);
            Intent detail = new Intent(context, BookDetailActivity.class);
            detail.putExtra("Book", book);
            context.startActivity(detail);
        }
    }
}
