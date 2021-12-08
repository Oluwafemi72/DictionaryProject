package algonquin.cst2335.dictionaryproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class wordListFragments extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View activity_dictionary = inflater.inflate(R.layout.activity_dictionary,container,false);
        RecyclerView theList = activity_dictionary.findViewById(R.id.recycler_meanings);
        Button searchButton =activity_dictionary.findViewById(R.id.search_button);
        EditText searchField = activity_dictionary.findViewById(R.id.search_Text);
        return activity_dictionary;
    }
}
