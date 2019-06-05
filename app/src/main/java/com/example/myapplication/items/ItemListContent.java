package com.example.myapplication.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Item> ITEMS = new ArrayList<Item>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Item> ITEM_MAP = new HashMap<String, Item>();



    public static void addItem(Item item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void removeItem(int position) {
        String itemId = ITEMS.get(position).id;
        ITEMS.remove(position);
        ITEM_MAP.remove(itemId);
    }

    public static void clearList() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }



    /**
     * A dummy item representing a piece of name.
     */
    public static class Item implements Parcelable {
        public final String id;
        public final String name;
        public final String localization;
        public final String description;
        public String picPath;

        //Redundant
        public Item(String id, String name, String localization, String description) {
            this.id = id;
            this.name = name;
            this.localization = localization;
            this.description = description;
            this.picPath = "";
        }

        public Item(String id, String name, String localization, String description, String picPath) {
            this.id = id;
            this.name = name;
            this.localization = localization;
            this.description = description;
            this.picPath = picPath;
        }

        protected Item(Parcel in) {
            id = in.readString();
            name = in.readString();
            localization = in.readString();
            description = in.readString();
            picPath = in.readString();
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(localization);
            dest.writeString(description);
            dest.writeString(picPath);
        }
    }
}
