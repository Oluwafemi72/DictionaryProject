package algonquin.cst2335.dictionaryproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import algonquin.cst2335.dictionaryproject.fragment.DefinitionFragment;
import algonquin.cst2335.dictionaryproject.fragment.SavedDefinitionsFragment;
import algonquin.cst2335.dictionaryproject.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {

    Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            activeFragment = SearchFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, activeFragment )
                    .commitNow();
        }

        Button btnRotate = findViewById(R.id.btn_rotate);
        btnRotate.setOnClickListener(view -> {
     rotateFragments();
        });
    }

    void rotateFragments () {
        if (activeFragment instanceof SearchFragment) {
            activeFragment = SavedDefinitionsFragment.newInstance();
        } else if (activeFragment instanceof SavedDefinitionsFragment){
            activeFragment = DefinitionFragment.newInstance();
        } else {
            activeFragment = SearchFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, activeFragment)
                .commitNow();
    }

}