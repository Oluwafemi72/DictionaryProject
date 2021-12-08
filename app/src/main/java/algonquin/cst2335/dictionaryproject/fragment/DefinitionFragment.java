package algonquin.cst2335.dictionaryproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.dictionaryproject.Dictionary;
import algonquin.cst2335.dictionaryproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DefinitionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefinitionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Dictionary.MyDictionary definition;

    Button btn_save;
    TextView txtWordDef;
    TextView txtDef;

    public DefinitionFragment(Dictionary.MyDictionary definition) {
        // Required empty public constructor
        super();
        this.definition = definition;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param definition
     * @return A new instance of fragment DefinitionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DefinitionFragment newInstance(Dictionary.MyDictionary definition) {
        DefinitionFragment fragment = new DefinitionFragment(definition);
        Bundle args = new Bundle();
//        args.putBundle("definition", definition);
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
        return inflater.inflate(R.layout.fragment_definition, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_save = view.findViewById(R.id.btn_save);
        onSaved();
        checkSaved();
        btn_save.setOnClickListener(view1 -> {
            toggleSave();
        });

        txtWordDef = view.findViewById(R.id.txtWordDef);
        txtDef = view.findViewById(R.id.txtDef);

        txtWordDef.setText(definition.word);
        txtDef.setText(definition.definition);

    }


    void toggleSave() {
        if (definition.isSaved) {
            definition.removeFavorite(getContext());
            Toast.makeText(getActivity(), "Remove favorite", Toast.LENGTH_SHORT).show();
        } else {
            definition.saveAsFavorite(getContext());
            Toast.makeText(getActivity(), "Add as favorite", Toast.LENGTH_SHORT).show();
        }
        onSaved();
    }

    void checkSaved() {
        definition.checkIsFavorite(getContext());
        btn_save.setText(definition.isSaved ? "Remove from favorites" : "Add as Favorite");
    }

    void onSaved() {
        btn_save.setText(definition.isSaved ? "Remove from favorites" : "Add as Favorite");
    }
}