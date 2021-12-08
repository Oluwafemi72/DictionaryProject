package algonquin.cst2335.dictionaryproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2335.dictionaryproject.fragment.DefinitionFragment;
import algonquin.cst2335.dictionaryproject.fragment.DictionaryFragment;
import algonquin.cst2335.dictionaryproject.fragment.SavedDefinitionsFragment;
import algonquin.cst2335.dictionaryproject.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {


    static MainActivity instance;

    Fragment activeFragment;
    DictionaryFragment homeFragment;


    static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

        }

        homeFragment = new DictionaryFragment();
        activateFragment(homeFragment);

        instance = this;

        Button btnRotate = findViewById(R.id.btn_rotate);
        btnRotate.setOnClickListener(view -> {
            rotateFragments();
        });
    }

    public static void loadFullDefinition(Dictionary.MyDictionary definition) {
        if (instance == null) {
            return;
        }
        instance.loadDefinition(definition);
    }

    public static void loadSavedWords() {
        if (instance == null) {
            return;
        }
        instance.loadSaved();
    }

    void activateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
        activeFragment = fragment;
    }

    void loadDefinition(Dictionary.MyDictionary definition) {
        DefinitionFragment fragment = DefinitionFragment.newInstance(definition);
        activateFragment(fragment);
    }

    void loadSaved() {
        SavedDefinitionsFragment fragment = SavedDefinitionsFragment.newInstance();
        activateFragment(fragment);
    }

    void rotateFragments() {
        if (activeFragment instanceof SearchFragment) {
            activeFragment = SavedDefinitionsFragment.newInstance();
        } else if (activeFragment instanceof SavedDefinitionsFragment) {
            activeFragment = DefinitionFragment.newInstance(null);
        } else {
            activeFragment = SearchFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, activeFragment)
                .commitNow();
    }


}