package com.example.multi_notepad;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class EditView  {
    private static final String TAG = "EditView";
    private SharedPreferences prefs;
    public EditView(Activity activity) {
        super();
        Log.d(TAG, "EditView");
        prefs = activity.getSharedPreferences("MY_PREFS_KEY", Context.MODE_PRIVATE);
    }

    void save (String key, String text) {
        Log.d(TAG, "save: " + key + ":" + text);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, text);
        editor.apply();
    }
    String getValue(String key) {
        String text = prefs.getString(key, "");
        Log.d(TAG, "save: " + key + ":" + text);
        return text;
    }
    void clearAll() {
        Log.d(TAG, "clearAll: ");
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
    void removeValue(String key) {
        Log.d(TAG, "removeValue: " + key);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }
//    @Override
//    public View onCreateView(
//            LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState
//    ) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_first, container, false);
//    }
//
//    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(EditView.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
//    }
}