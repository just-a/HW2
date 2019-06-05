package com.example.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.items.ItemListContent;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemInfoFragment extends Fragment {

    private ItemListContent.Item mDisplayedItem;

    public ItemInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();
        Intent intent = activity.getIntent();

        if(intent !=null) {
            ItemListContent.Item recivedItem = intent.getParcelableExtra(MainActivity.itemExtra);
            if(recivedItem != null) {
                displayItem(recivedItem);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_info, container, false);
    }


    public void displayItem(ItemListContent.Item item){
        FragmentActivity activity = getActivity();

        TextView itemInfoName = activity.findViewById(R.id.ItemInfoName);
        TextView itemInfoLocalization = activity.findViewById(R.id.ItemInfoLocalization);
        TextView itemInfoDescription = activity.findViewById(R.id.ItemInfoDescription);
        final ImageView itemInfoImage = activity.findViewById(R.id.ItemInfoImage);

        itemInfoName.setText(item.name);
        itemInfoLocalization.setText(item.localization);
        itemInfoDescription.setText(item.description);
        if(item.picPath != null && !item.picPath.isEmpty()){
            if(item.picPath.contains("nr1")) {
                Drawable itemDrawable;
                switch(item.picPath){
                    case "nr2":
                        itemDrawable = activity.getResources().getDrawable(R.drawable.nr2);
                        break;
                    case "nr3":
                        itemDrawable = activity.getResources().getDrawable(R.drawable.nr3);
                        break;
                    case "nr4":
                        itemDrawable = activity.getResources().getDrawable(R.drawable.nr4);
                        break;
                    case "nr5":
                        itemDrawable = activity.getResources().getDrawable(R.drawable.nr5);
                        break;
                    case "nr6":
                        itemDrawable =activity.getResources().getDrawable(R.drawable.nr6);
                        break;
                    case "nr7":
                        itemDrawable = activity.getResources().getDrawable(R.drawable.nr7);
                        break;
                    case "nr8":
                        itemDrawable = activity.getResources().getDrawable(R.drawable.nr8);
                        break;
                    default:
                        itemDrawable = activity.getResources().getDrawable(R.drawable.nr1);

                }
                itemInfoImage.setImageDrawable(itemDrawable);
            } else {
                Handler handler = new Handler();
                itemInfoImage.setVisibility(View.INVISIBLE);
                handler. postDelayed(new Runnable() {
                    @Override
                    public void run(){
                        itemInfoImage.setVisibility(View.VISIBLE);
                        Bitmap cameraImage = PicUtils.decodePic(mDisplayedItem.picPath,
                                itemInfoImage.getWidth(),
                                itemInfoImage.getHeight());
                        itemInfoImage.setImageBitmap(cameraImage);
                    }
                }, 200);
            }
        } else{
            itemInfoImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.nr1));
        }
        mDisplayedItem = item;
    }
}
