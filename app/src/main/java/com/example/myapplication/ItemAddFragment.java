package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.myapplication.items.ItemListContent;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemAddFragment extends Fragment {

    public static final int REQUEST_IMAGE_CAPTURE = 1; // request code for image capture
    private String mCurrentPhotoPath; // String used to save the path of the picture


    private boolean takePic;

    public ItemAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();

        activity.findViewById(R.id.displayFragment).setVisibility(View.INVISIBLE);

        Intent received_intent = activity.getIntent();
        takePic = received_intent.getBooleanExtra(MainActivity.takePic,false);

        if(takePic){
            TakePicture();
        }
        
        getActivity().findViewById(R.id.displayFragment).setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_add, container, false);
    }

    private void TakePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (IOException ex){

            }

            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), getString(R.string.myFileprovider), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFIleName = ("ItemPic" + ItemListContent.ITEMS.size()+1 )+ timeStamp + " ";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile( imageFIleName, ".jpg", storageDir );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        FragmentActivity holdingActivity = getActivity();
        if (holdingActivity != null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
                ((AddItemActivity) holdingActivity).setImgPicPath(mCurrentPhotoPath);
            } else {
                ((AddItemActivity) holdingActivity).PicTakenCancelled();
            }
        }
    }
}
