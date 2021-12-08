package algonquin.cst2335.dictionaryproject.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import algonquin.cst2335.dictionaryproject.Dictionary;
import algonquin.cst2335.dictionaryproject.MainActivity;
import algonquin.cst2335.dictionaryproject.MyAdapter;
import algonquin.cst2335.dictionaryproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedDefinitionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedDefinitionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    RecyclerView rView;
    MyAdapter theAdapter;
    ArrayList<Dictionary.MyDictionary> MyDictionaryArrayList = new ArrayList<>();


    public SavedDefinitionsFragment() {
        // Required empty public constructor
    }


    public static SavedDefinitionsFragment newInstance() {
        SavedDefinitionsFragment fragment = new SavedDefinitionsFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_definitions, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rView = view.findViewById(R.id.recycler_meanings);

        rView.setLayoutManager(new LinearLayoutManager(getActivity()));

        theAdapter = new MyAdapter(getActivity(), MyDictionaryArrayList);
        theAdapter.setClickListener(position -> {
            Toast.makeText(getActivity(), position + " position clicked", Toast.LENGTH_SHORT).show();
            Dictionary.MyDictionary dictionary = MyDictionaryArrayList.get(position);
            MainActivity.loadFullDefinition(dictionary);
        });
        rView.setAdapter(theAdapter);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));


        loadSavedDefinitions();
    }

    void loadSavedDefinitions() {
        MyDictionaryArrayList.clear();
        MyDictionaryArrayList.addAll(Dictionary.MyDictionary.getAllSavedWords(getContext()));
        theAdapter.notifyDataSetChanged();
    }


}