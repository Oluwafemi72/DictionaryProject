package algonquin.cst2335.dictionaryproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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


    static String AuthorizationToken = "7127899793ddbda166156411c9dd231cd8066bf3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        search = findViewById(R.id.search_button);
        rView = findViewById(R.id.recycler_meanings);
        SearchText = findViewById(R.id.search_Text);
        SearchedWord = findViewById(R.id.searched_word);

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

    public class MyDictionary {
        String words;
        String definition;

        public MyDictionary(String words, String definition) {
            this.words = words;
            this.definition = definition;
        }
    }


    class OWLBotDictionary {
        String type;
        String definition;
        String example;
        String image_url;
        String emoji;
    }

    class APIResponse {
        ArrayList<OWLBotDictionary> definitions;
        String word;
        String pronunciation;
    }


}