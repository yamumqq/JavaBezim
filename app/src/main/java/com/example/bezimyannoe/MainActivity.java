package com.example.bezimyannoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        recyclerView = findViewById(R.id.recyclerView); // Update to use RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab_add_book);
        fab.setOnClickListener(v -> {
            id = sharedPreferences.getInt("id", 0) + 1;
            editor.putInt("id", id);
            editor.apply();
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivity(intent);
        });

        Paper.init(this);
        adapter = new ItemAdapter(getItems());
        recyclerView.setAdapter(adapter);
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        List<String> keys = new ArrayList<>(Paper.book().getAllKeys());
        for (String key : keys) {
            Item item = Paper.book().read(key, null);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }


    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

        private List<Item> items;

        public ItemAdapter(List<Item> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false); // Create item_layout.xml
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            Item item = items.get(position);
            holder.titleTextView.setText(item.getTitle());
            holder.priceTextView.setText(String.valueOf(item.getPrice()));
            holder.image.setImageURI(Uri.parse(item.getPhotoPath()));// Assuming you have a price field in Item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, UpdateItemActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("photoPath", item.getPhotoPath());
                intent.putExtra("nameView", item.getTitle());
                intent.putExtra("priceView", item.getPrice());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView priceTextView;
            ImageView image;

            public ItemViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                titleTextView = itemView.findViewById(R.id.itemName); // Update IDs
                priceTextView = itemView.findViewById(R.id.itemPrice); // Update IDs
            }
        }
    }
}