package ua.edu.ethnographyresearch.repository.entity;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        return new Gson().fromJson(value, ArrayList.class);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        return new Gson().toJson(list);
    }
}
