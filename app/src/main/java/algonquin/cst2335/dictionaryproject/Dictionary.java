package algonquin.cst2335.dictionaryproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Dictionary extends AppCompatActivity {

    Button search;
    EditText SearchText;
    RecyclerView rView;
    TextView SearchedWord;
    ArrayList<MyDictionary> MyDictionaryArrayList = new ArrayList<>();
    String word;
    String definition;
    MyAdapter theAdapter;
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    MyOpenHelper myOpenHelper;
    Toolbar myToolbar;


    static String AuthorizationToken = "7127899793ddbda166156411c9dd231cd8066bf3";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
             MenuInflater inflater = getMenuInflater();
             inflater.inflate(R.menu.main_activity_actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);



        myOpenHelper = new MyOpenHelper(this);

        final SQLiteDatabase TheDataBase = myOpenHelper.getWritableDatabase();

        myToolbar = findViewById(R.id.toolbar);
        search = findViewById(R.id.search_button);
        rView = findViewById(R.id.recycler_meanings);
        SearchText = findViewById(R.id.search_Text);
        SearchedWord = findViewById(R.id.searched_word);

        setSupportActionBar(myToolbar);

        sharedPreferences = getSharedPreferences("Shared_pref", MODE_PRIVATE);

        rView.setLayoutManager(new LinearLayoutManager(this));

        theAdapter = new MyAdapter(this, MyDictionaryArrayList);
        rView.setAdapter(theAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));

        queue = Volley.newRequestQueue(this);


        search.setOnClickListener(click -> {
            String WhatWasTyped = SearchText.getText().toString();
            String WhatWasSearched = SearchedWord.getText().toString();

            SearchText.setText("");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("WhatWasTyped", WhatWasTyped);
            editor.apply();
            Toast.makeText(Dictionary.this, "Searched word saved", Toast.LENGTH_SHORT).show();

//            populateRecycler(WhatWasTyped);

            fetchWordDefinitions(WhatWasTyped);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.COL_DEFINITION, WhatWasTyped);
            newRow.put(MyOpenHelper.COL_WORD, WhatWasSearched);

            TheDataBase.insert(MyOpenHelper.TABLE_NAME, null, newRow);

        });


        populateRecycler("Word");


    }

    void populateRecycler(String prefix) {
        MyDictionaryArrayList.clear();
        for (int i = 0; i < 50; i++) {
            MyDictionary dictionary = new MyDictionary(prefix + " - " + i, "Definition of " + prefix + " - " + 1);
            MyDictionaryArrayList.add(dictionary);
        }
        theAdapter.notifyDataSetChanged();

    }

    void fetchWordDefinitions(String word) {
        SearchedWord.setText("Fetching result");
        String url = "https://owlbot.info/api/v4/dictionary/" + word;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    Log.d("OWLBOT Response", response);
                    SearchedWord.setText("Done fetchng result");
                    Gson gson = new Gson();
                    APIResponse apiResponse = gson.fromJson(response, APIResponse.class);

                    MyDictionaryArrayList.clear();
                    for (OWLBotDictionary definition : apiResponse.definitions) {
                        MyDictionary dictionary = new MyDictionary(definition.type, definition.definition);

                        MyDictionaryArrayList.add(dictionary);
                    }
                    theAdapter.notifyDataSetChanged();
                }, error -> {
            SearchedWord.setText("Error while fetching result");
            Log.e("OWLBOT Error", error.getMessage());
            error.printStackTrace();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + AuthorizationToken);
                return params;
            }
        };
        queue.add(stringRequest);

    }

    public static class MyDictionary {
        public String word;
        public String definition;
        public String pronunciation;
        public String type;
        public boolean isSaved;

        static MyOpenHelper helperInstance;

        public MyDictionary(String word, String definition) {
            this.word = word;
            this.definition = definition;
        }

        static void initializeHelper(Context context) {
            if (helperInstance == null) {
                helperInstance = new MyOpenHelper(context);
//                final SQLiteDatabase TheDataBase = helperInstance.getWritableDatabase();
            }
        }

        static SQLiteDatabase getDbInstance(Context context) {
            initializeHelper(context);
            return helperInstance.getWritableDatabase();
        }


        public void saveAsFavorite(Context context) {
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.COL_DEFINITION, definition);
            newRow.put(MyOpenHelper.COL_WORD, word);

            long newRowId = getDbInstance(context).insert(MyOpenHelper.TABLE_NAME, null, newRow);
            Log("New word saved as Id: " + newRowId);
            isSaved = true;
        }

        static void Log(String message) {
            Log.d("MyDictionaryHelper", message);
        }

        public void removeFavorite(Context context) {
            String selection = MyOpenHelper.COL_WORD + " = ?";
            String[] selectionArgs = {word};

            int deleted = getDbInstance(context).delete(MyOpenHelper.TABLE_NAME, selection, selectionArgs);
            Log("Words deleted: " + deleted);
            isSaved = false;
        }

        public void checkIsFavorite(Context context) {
            initializeHelper(context);

            String[] projection = {
                    MyOpenHelper.COL_ID,
                    MyOpenHelper.COL_DEFINITION,
                    MyOpenHelper.COL_WORD
            };
            String selection = MyOpenHelper.COL_WORD + " = ?";
            String[] selectionArgs = {word};

            String sortOrder = MyOpenHelper.COL_WORD + " ASC";

            Cursor cursor = getDbInstance(context).query(
                    MyOpenHelper.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            isSaved = cursor.moveToNext();
            cursor.close();

        }

        public static ArrayList<MyDictionary> getAllSavedWords(Context context) {
            ArrayList<MyDictionary> dictionaries = new ArrayList<>();

            String[] projection = {
                    MyOpenHelper.COL_ID,
                    MyOpenHelper.COL_DEFINITION,
                    MyOpenHelper.COL_WORD
            };
            String selection = "";
            String[] selectionArgs = {};

            String sortOrder = MyOpenHelper.COL_WORD + " ASC";

            Cursor cursor = getDbInstance(context).query(
                    MyOpenHelper.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );


            while (cursor.moveToNext()) {
                String word = cursor.getString(cursor.getColumnIndexOrThrow(MyOpenHelper.COL_WORD));
                String definition = cursor.getString(cursor.getColumnIndexOrThrow(MyOpenHelper.COL_DEFINITION));

                MyDictionary dictionary = new MyDictionary(word, definition);
                dictionaries.add(dictionary);
            }
            cursor.close();


            return dictionaries;
        }
    }


    public static class OWLBotDictionary {
        public String type;
        public String definition;
        public String example;
        public String image_url;
        public String emoji;
    }

    public static class APIResponse {
        public ArrayList<OWLBotDictionary> definitions;
        public String word;
        public String pronunciation;
    }


}