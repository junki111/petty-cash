package com.example.expense.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.expense.R;
import com.google.android.material.navigation.NavigationView;

public class ViewExpense extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Activity activity = this;
    static final float END_SCALE = 0.7f;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout contentView;
    ImageView menuIcon;

    TextView viewMerchantName;
    TextView viewExpenseAmount;
    TextView viewExpenseDate;
    TextView viewExpenseDescription;
    TextView viewExpenseCategory;

    String merchantName;
    String expenseDate;
    String expenseAmount;
    String expenseDescription;
    String expenseCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expense);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        contentView = findViewById(R.id.content);
        menuIcon = findViewById(R.id.menu_icon);

        viewMerchantName = findViewById(R.id.merchantNameView);
        viewExpenseDate = findViewById(R.id.expenseDateView);
        viewExpenseAmount = findViewById(R.id.expenseAmountView);
        viewExpenseDescription = findViewById(R.id.expenseDescriptionView);
        viewExpenseCategory = findViewById(R.id.expenseCategoryView);
        navigationDrawer();
        getData();
    }

    private void getData() {
        Intent i = new Intent(getIntent());

        merchantName = i.getStringExtra("mName");
        expenseDate = i.getStringExtra("eDate");
        expenseAmount = i.getStringExtra("eAmount");
        expenseDescription = i.getStringExtra("eDescription");
        expenseCategory = i.getStringExtra("eCategory");

        setData();
    }

    private void setData() {
        viewMerchantName.setText(merchantName);
        viewExpenseDate.setText(expenseDate);
        viewExpenseAmount.setText(expenseAmount);
        viewExpenseDescription.setText(expenseDescription);
        viewExpenseCategory.setText(expenseCategory);
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_expense);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.nav_home):
                Intent intent = new Intent(activity, LandingActivity.class);
                startActivity(intent);
                break;
            case(R.id.nav_category):
                Intent intent1 = new Intent(activity, Categories.class);
                startActivity(intent1);
                break;
            case(R.id.nav_expense):
                break;
            case(R.id.nav_changePin):
                Intent intent3 = new Intent(activity, ChangePinActivity.class);
                startActivity(intent3);
                break;
        }
        return true;
    }

    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}