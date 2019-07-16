package muchbeer.raum.com.gcertnotekeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.squareup.picasso.Picasso;

import muchbeer.raum.com.gcertnotekeeper.databinding.ActivityDetailBookBinding;
import muchbeer.raum.com.gcertnotekeeper.internet.Book;

public class DetailBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        Book book= getIntent().getParcelableExtra("Book");
        ActivityDetailBookBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_detail_book);

        binding.setBook(book);


    }
}
