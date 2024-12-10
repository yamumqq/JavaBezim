package com.example.bezimyannoe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.paperdb.Paper;

public class UpdateItemActivity extends AppCompatActivity {

    ImageView image;
    private EditText itemTitle, itemPrice;
    private Button update, delete;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    int id;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        image = findViewById(R.id.image);
        itemTitle= findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);

        id = getIntent().getIntExtra("id", -1);
        itemTitle.setText(getIntent().getStringExtra("nameView"));
        double price = getIntent().getDoubleExtra("priceView", 0.00);
        itemPrice.setText(String.valueOf(price));
        image.setImageURI(Uri.parse(getIntent().getStringExtra("photoPath")));
        selectedImageUri = Uri.parse(getIntent().getStringExtra("photoPath"));

        ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        image.setImageURI(selectedImageUri);
                    }
                }
        );

        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });

        update.setOnClickListener(v -> {

            String title = itemTitle.getText().toString();
            double itemprice = Double.parseDouble(itemPrice.getText().toString());

            if (!title.isEmpty() ) {
                Item updatedItem = new Item(id, title, itemprice, selectedImageUri.toString());
                Paper.book().write(String.format("%d", id), updatedItem);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", id + 1);
                editor.apply();
                Intent intent = new Intent(UpdateItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(v -> {
            Paper.book().delete(String.format("%d", id));
            Intent intent = new Intent(UpdateItemActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
