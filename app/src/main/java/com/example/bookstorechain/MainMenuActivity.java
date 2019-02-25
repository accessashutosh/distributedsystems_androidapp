package com.example.bookstorechain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity
{
    Button manage_booksButton;
    Button manage_branchButton;
    Button manage_inventoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        manage_booksButton = (Button) findViewById(R.id.manage_books);
        manage_branchButton = (Button) findViewById(R.id.manage_branch);
        manage_inventoryButton = (Button) findViewById(R.id.manage_inventory);

        manage_booksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageBookActivity.class));
            }
        });

        manage_branchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageBranchActivity.class));
            }
        });

        manage_inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getApplicationContext(), ManageBranchActivity.class));
            }
        });
    }
}