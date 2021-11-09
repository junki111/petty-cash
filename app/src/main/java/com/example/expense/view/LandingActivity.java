package com.example.expense.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.expense.adapter.ExpenseAdapter;
import com.example.expense.R;
import com.example.expense.data.entity.Expense;
import com.example.expense.viewmodel.ExpenseViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class LandingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Activity activity = this;
    private final Context context = this;
    private Button createFirstExpense;
    private Button createCategory;
    private ExpenseViewModel expenseViewModel;
    private List<Expense> expenseList;


    static final float END_SCALE = 0.7f;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout contentView;
    ImageView menuIcon;
    SearchView searchIcon;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    private ExpenseAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        contentView = findViewById(R.id.content);
        menuIcon = findViewById(R.id.menu_icon);
        searchIcon = (SearchView) findViewById(R.id.search_icon);
        searchIcon.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        expenseViewModel = ViewModelProviders.of(this).get(ExpenseViewModel.class);
        navigationDrawer();
        initRecyclerView();


        createFirstExpense = findViewById(R.id.createFirstExpense);
        createFirstExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, CreateExpenseActivity.class);
                startActivity(i);
            }
        });

        createCategory = findViewById(R.id.addCategory);
        createCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, Categories.class);
                startActivity(i);
            }
        });

    }


    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);


        expenseViewModel.getExpenseLiveList().observe(LandingActivity.this, new Observer<List<Expense>>() {
            @Override
            public void onChanged(@Nullable List<Expense> expense) {
                // Update UI
                expenseList = expense;


                if (expenseList != null) {
//                    if (expenseList.size() == 0)
//                        tvNotAvailableTransaction.setVisibility(View.VISIBLE);
//                    else
//                        tvNotAvailableTransaction.setVisibility(View.GONE);

                    adapter = new ExpenseAdapter(context, activity, expenseList);
                    adapter.notifyDataSetChanged();

                    layoutManager = new LinearLayoutManager(context);
                    recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);
                }

            }
        });
    }

    private void navigationDrawer() {
        //Show or Hide menu items
        //Menu menu = navigationView.getMenu();
        //menu.findItem(R.id.nav_logout).setVisible(false);


        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xoffset = drawerView.getWidth() * slideOffset;
                final float xoffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xoffset - xoffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.nav_home):
                break;
            case(R.id.nav_category):
                Intent intent1 = new Intent(activity, Categories.class);
                startActivity(intent1);
                break;
            case(R.id.nav_expense):
                Intent intent2 = new Intent(activity, CreateExpenseActivity.class);
                startActivity(intent2);
                break;
            case(R.id.nav_changePin):
                Intent intent3 = new Intent(activity, ChangePinActivity.class);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.nav_drawer_menu, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//
//        return true;
//    }
}