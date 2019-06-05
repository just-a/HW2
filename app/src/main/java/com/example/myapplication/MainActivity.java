package com.example.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import com.example.myapplication.items.ItemListContent;



public class MainActivity extends AppCompatActivity
        implements
        ItemFragment.OnListFragmentInteractionListener,
        DeleteDialog.OnFragmentInteractionListener {


    public static final String itemExtra = "itemExtra";
    private int currentItemPosition = -1;
    public static final int REQUEST_ADD = 1;

    private ItemListContent.Item currentItem;
    private final String CURRENT_ITEM_KEY = "CurrentItem";

    public static final String takePic = "takePic";

    private final String ITEMS_JSON_FILE = "items.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restorFromJson();

        if(savedInstanceState != null) {
            currentItem = savedInstanceState.getParcelable(CURRENT_ITEM_KEY);
        }

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent AddIntent = new Intent(getApplicationContext(), AddItemActivity.class);
                startActivityForResult(AddIntent, REQUEST_ADD);
            }
        });

        FloatingActionButton fabCam = findViewById(R.id.fabCam);
        fabCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Camera action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent AddIntent = new Intent(getApplicationContext(), AddItemActivity.class);
                AddIntent.putExtra(takePic,true);
                startActivityForResult(AddIntent, REQUEST_ADD);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void displayItemInFragment(ItemListContent.Item item) {
        ItemInfoFragment itemInfoFragment = ((ItemInfoFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment));
        if(itemInfoFragment != null) {
            itemInfoFragment.displayItem(item);
        }
    }

    private void startSecondActivity(ItemListContent.Item item, int position) {
        Intent intent = new Intent (this, ItemInfoActivity.class);
        intent.putExtra(itemExtra, item);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_ADD){
                //update Fragment
                ((ItemFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.itemFragment))).notifyDataChange();
            }
        }
        else if(resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),getText(R.string.cancel_msg), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteDialog() {
        DeleteDialog.newInstance().show(getSupportFragmentManager(), getString(R.string.delete_dialog_tag));
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if(currentItemPosition != -1 && currentItemPosition < ItemListContent.ITEMS.size()){
            ItemListContent.removeItem(currentItemPosition);
            ((ItemFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.itemFragment))).notifyDataChange();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        View v = findViewById(R.id.itemFragment);
        if(v != null) {
            Snackbar.make(v, getString(R.string.cancel_msg), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.retry_msg), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDeleteDialog();
                        }
                    }).show();
        }
    }

    @Override
    public void onListFragmentClickInteraction(ItemListContent.Item item, int position, boolean delete) {
        if (delete) {
            showDeleteDialog();
            currentItemPosition = position;
        }
        else {
            currentItem = item;
            Toast.makeText(this, getString(R.string.item_selected_msg), Toast.LENGTH_SHORT).show();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                displayItemInFragment(item);
            } else {
                startSecondActivity(item, position);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(currentItem != null)
            outState.putParcelable(CURRENT_ITEM_KEY,currentItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(currentItem != null)
                displayItemInFragment(currentItem);
        }
    }

    @Override
    protected void onDestroy() {
        saveItemsToJson();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        saveItemsToJson();
        super.onStop();
    }

    private void saveItemsToJson(){
        Gson gson = new Gson();
        String listJson = gson.toJson(ItemListContent.ITEMS);
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(ITEMS_JSON_FILE, MODE_PRIVATE);
            FileWriter writer = new FileWriter(outputStream.getFD());
            writer.write(listJson);
            writer.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void restorFromJson () {
        FileInputStream inputStream;
        int DEFAULT_BUFFER_SIZE = 10000;
        Gson gson = new Gson();
        String readJson;

        try {
            inputStream = openFileInput(ITEMS_JSON_FILE);
            FileReader reader = new FileReader(inputStream.getFD());
            char[] buf = new char[DEFAULT_BUFFER_SIZE];
            int n;
            StringBuilder builder = new StringBuilder();
            while ((n = reader.read(buf)) >= 0) {
                String tmp = String.valueOf(buf);
                String substring = (n<DEFAULT_BUFFER_SIZE) ? tmp.substring(0, n) : tmp;
                builder.append(substring);
            }
            reader.close();
            readJson = builder.toString();
            Type collectionType = new TypeToken<List<ItemListContent.Item>>() {
            }.getType();
            List<ItemListContent.Item> o = gson.fromJson(readJson, collectionType);
            if(o != null){
                ItemListContent.clearList();
                for(ItemListContent.Item item : o) {
                    ItemListContent.addItem(item);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}