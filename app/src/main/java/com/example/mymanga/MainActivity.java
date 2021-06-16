package com.example.mymanga;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayDeque;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private final Deque<Integer> integerDeque = new ArrayDeque<>(3);
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        integerDeque.push(R.id.bn_explorer);

        loadFragment(new ExplorerFragment());
        bottomNavigationView.setSelectedItemId(R.id.bn_explorer);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (integerDeque.contains(id)) {
                if (id == R.id.bn_explorer) {
                    if (integerDeque.size() != 1) {
                        if (flag) {
                            integerDeque.addFirst(R.id.bn_explorer);
                            flag = false;
                        }
                    }
                }
                integerDeque.remove(id);
            }
            integerDeque.push(id);
            loadFragment(getFragment(menuItem.getItemId()));
            return true;
        });
    }

    private Fragment getFragment(int itemId) {
        if (itemId == R.id.bn_library) {
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            return new LibraryFragment();
        } else if (itemId == R.id.bn_explorer) {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
            return new ExplorerFragment();
        } else if (itemId == R.id.bn_profile) {
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
            return new ProfileFragment();
        }
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        return new ExplorerFragment();
    }

    @Override
    public void onBackPressed() {
        integerDeque.pop();
        if (!integerDeque.isEmpty()) {
            loadFragment(getFragment(integerDeque.peek()));
        } else {
            finish();
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment, fragment.getClass().getSimpleName())
                .commit();
    }
}