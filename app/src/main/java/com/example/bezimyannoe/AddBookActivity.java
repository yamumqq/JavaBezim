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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import io.paperdb.Paper;

public class AddBookActivity extends AppCompatActivity {

    ImageView image;
    private EditText itemTitle, itemPrice;
    private Button add;
    SharedPreferences sharedPreferences;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Paper.init(this);
        itemTitle = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        image = findViewById(R.id.image);
        add = findViewById(R.id.add);
        sharedPreferences = getSharedPreferences("id", Context.MODE_PRIVATE);

        ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        image.setImageURI(selectedImageUri);
                    }
                }
        );



        add.setOnClickListener(v -> {
            String title = itemTitle.getText().toString();
            String priceStr = itemPrice.getText().toString();
            int id = sharedPreferences.getInt("id", -1);

            if (title.isEmpty() || priceStr.isEmpty() || id == -1 || selectedImageUri == null) {
                Toast.makeText(this, "Заполни все поля и выберите фотокарточку", Toast.LENGTH_SHORT).show();
                return;
            }

            try{
                double price = Double.parseDouble(priceStr);
                Item item = new Item(id, title, price, selectedImageUri.toString());
                Paper.book().write(String.format("%d", id), item);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id", id + 1);
                editor.apply();
                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                startActivity(intent);
            }catch (NumberFormatException e){
                Toast.makeText(this, "Цена введена неправильно", Toast.LENGTH_SHORT).show();
            }
        });

        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });
    }
}