package muchbeer.raum.com.gcertnotekeeper.internet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import muchbeer.raum.com.gcertnotekeeper.DetailBookActivity;
import muchbeer.raum.com.gcertnotekeeper.R;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    ArrayList<Book> books;

    public BookAdapter(ArrayList<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.book_list_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvAuthors;
        TextView tvDate;
        TextView tvPublisher;

        public BookViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvAuthors= (TextView) itemView.findViewById(R.id.tvAuthors);
            tvDate = (TextView) itemView.findViewById(R.id.tvPublishedDate);
            tvPublisher =(TextView) itemView.findViewById(R.id.tvPublisher);
itemView.setOnClickListener(this);

        }


        public void bind (Book book) {
            tvTitle.setText(book.title);
           // String authors="";
                    /*   for (String author:book.authors) {
                authors+=author;
                int i;
                i++;
                if(i<book.authors.length) {
                    authors+=", ";
                }
            }*/
            tvAuthors.setText(book.authors);
            tvDate.setText(book.publishedDate);
            tvPublisher.setText(book.publisher);

        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            //gets the book from the arrayList
            Book selectedBook = books.get(position);

            Intent intent = new Intent(view.getContext(), DetailBookActivity.class);
            intent.putExtra("Book", selectedBook);

            view.getContext().startActivity(intent);
        }
    }
}
