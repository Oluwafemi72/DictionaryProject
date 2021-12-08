package algonquin.cst2335.dictionaryproject.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import algonquin.cst2335.dictionaryproject.Dictionary;
import algonquin.cst2335.dictionaryproject.MainActivity;
import algonquin.cst2335.dictionaryproject.MyAdapter;
import algonquin.cst2335.dictionaryproject.MyOpenHelper;
import algonquin.cst2335.dictionaryproject.R;

public class DictionaryFragment extends Fragment {


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DictionaryFragment newInstance() {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    Button search;
    EditText SearchText;

    TextView SearchedWord;
    String word;
    String definition;
    RecyclerView rView;
    MyAdapter theAdapter;
    ArrayList<Dictionary.MyDictionary> MyDictionaryArrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    RequestQueue queue;
    MyOpenHelper myOpenHelper;


    static String AuthorizationToken = "7127899793ddbda166156411c9dd231cd8066bf3";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_dictionary, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        myOpenHelper = new MyOpenHelper(getActivity());

        final SQLiteDatabase TheDataBase = myOpenHelper.getWritableDatabase();


        search = view.findViewById(R.id.search_button);
        rView = view.findViewById(R.id.recycler_meanings);
        SearchText = view.findViewById(R.id.search_Text);
        SearchedWord = view.findViewById(R.id.searched_word);

        sharedPreferences = getActivity().getSharedPreferences("Shared_pref", MODE_PRIVATE);

        rView.setLayoutManager(new LinearLayoutManager(getActivity()));

        theAdapter = new MyAdapter(getActivity(), MyDictionaryArrayList);
        theAdapter.setClickListener(position -> {
            Toast.makeText(getActivity(), position + " position clicked", Toast.LENGTH_SHORT).show();
            Dictionary.MyDictionary dictionary = MyDictionaryArrayList.get(position);
            MainActivity.loadFullDefinition(dictionary);
        });
        rView.setAdapter(theAdapter);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));


        view.findViewById(R.id.load_saved_button).setOnClickListener(view1 -> {
            MainActivity.loadSavedWords();
        });

        queue = Volley.newRequestQueue(getActivity());


        search.setOnClickListener(click -> {
            String WhatWasTyped = SearchText.getText().toString();
            String WhatWasSearched = SearchedWord.getText().toString();

            SearchText.setText("");
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("WhatWasTyped", WhatWasTyped);
            editor.apply();
            Toast.makeText(getActivity(), "Searched word saved", Toast.LENGTH_SHORT).show();

//            populateRecycler(WhatWasTyped);

            fetchWordDefinitions(WhatWasTyped);


        });


        populateRecycler("Word");


    }


    void populateRecycler(String prefix) {
        MyDictionaryArrayList.clear();
        for (int i = 0; i < 50; i++) {
            Dictionary.MyDictionary dictionary = new Dictionary.MyDictionary(prefix + " - " + i, "Definition of " + prefix + " - " + 1);
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
                    Dictionary.APIResponse apiResponse = gson.fromJson(response, Dictionary.APIResponse.class);

                    MyDictionaryArrayList.clear();
                    for (Dictionary.OWLBotDictionary definition : apiResponse.definitions) {
                        Dictionary.MyDictionary dictionary = new Dictionary.MyDictionary(word, definition.definition);
                        dictionary.type = definition.type;
                        dictionary.pronunciation = apiResponse.pronunciation;
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


}
