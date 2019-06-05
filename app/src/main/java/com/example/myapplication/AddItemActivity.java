package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.myapplication.items.ItemListContent;


public class AddItemActivity extends AppCompatActivity {

    private String itemPicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        itemPicPath = "";
    }

    public void setImgPicPath (String val){
        itemPicPath = val;
    }

    public void PicTakenCancelled (){
        Intent data = new Intent();
        setResult(RESULT_CANCELED, data);
        finish();
    }

    public void addClick(View view) {
        Intent data = new Intent();

        EditText itemNameEditTxt = findViewById(R.id.ItemAddName);
        EditText itemLocalizationEditTxt = findViewById(R.id.ItemAddLocalization);
        EditText itemDescriptionEditTxt = findViewById(R.id.ItemAddDescription);
        String itemName = itemNameEditTxt.getText().toString();
        String itemLocalization = itemLocalizationEditTxt.getText().toString();
        String itemDescription = itemDescriptionEditTxt.getText().toString();

        //default values
        if(itemName.isEmpty())
            itemName =getString(R.string.name);
        if(itemLocalization.isEmpty())
            itemLocalization = getString(R.string.localization);
        if(itemDescription.isEmpty())
            itemDescription = getString(R.string.description);


        ItemListContent.addItem(new ItemListContent.Item("Item." + ItemListContent.ITEMS.size() +1,
                itemName,
                itemLocalization,
                itemDescription,
                itemPicPath));

        itemNameEditTxt.setText("");
        itemLocalizationEditTxt.setText("");
        itemDescriptionEditTxt.setText("");

        //hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        setResult(RESULT_OK, data);
        finish();
    }
}
