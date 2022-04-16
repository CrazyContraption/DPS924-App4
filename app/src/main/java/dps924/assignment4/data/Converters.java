package dps924.assignment4.data;

import androidx.room.TypeConverter;

import org.json.JSONArray;

import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String json) {
        if (json == null)
            return null;
        ArrayList<String> list = new ArrayList<>();
        if (json.equals("") || json.length() == 0)
            return list;
        JSONArray temp = null;
        try {
            temp = new JSONArray(json);
            for (int index = 0; index < temp.length(); index++)
                list.add(temp.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        if (list == null)
            return null;
        if (list.size() == 0)
            return "";
        JSONArray temp = new JSONArray();
        temp.put(list.get(0));
        String json = null;
        try {
            json = temp.toString(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}