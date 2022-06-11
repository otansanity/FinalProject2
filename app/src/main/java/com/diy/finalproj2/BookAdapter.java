package com.diy.finalproj2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    View view;
    Context context;
    ArrayList<BookList> list;

    public BookAdapter() {
    }

    public BookAdapter(Context context, ArrayList<BookList> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {

        BookList bookList = list.get(position);
        holder.bookTitle.setText(bookList.getBookTitle());
        holder.bookAuthor.setText(bookList.getBookAuthor());
        holder.bookPages.setText("Pages: "+bookList.getBookPages());
        holder.manufacturedBy.setText("Made in: "+bookList.getManufacturedBy());

        Glide.with(context).load(list.get(position).getBookImage()).into(holder.bookImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, updateBook.class);
                intent.putExtra("bookId", bookList.getBookId());
                intent.putExtra("bookImage", bookList.getBookImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle, bookAuthor, cellNumber,bookPages,  manufacturedBy;
        ImageView bookImage;
        CardView cardViewClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookPages = itemView.findViewById(R.id.book_pages);
            manufacturedBy = itemView.findViewById(R.id.manufactured_txt);
            bookImage = itemView.findViewById(R.id.bookIMG);
        }
    }
}
