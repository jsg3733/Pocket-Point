package info498.group3.pocketpoint;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// This is the first page that is loaded on app opening
// Category Page
public class CategoryPage extends ActionBarActivity {

    // category name
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        // removes the notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // sets the category to all category for reference and logs
        category = "Categories";
        Log.i("Test", category);

        //puts the category into a bundle
        Bundle bundle = new Bundle();
        bundle.putString("category", category);


        // calls the GridFragment and passes it the bundle that has category name
        // places it in the linear layout called gridFragmentPlaceholder
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GridFragmentWithBar gridFragmentWithBar = new GridFragmentWithBar();
        gridFragmentWithBar.setArguments(bundle);
        fragmentTransaction.add(R.id.gridFragmentPlaceholder, gridFragmentWithBar);
        fragmentTransaction.commit();

        // sets on click listener for the add new category button that will go to that intent on click
        LinearLayout newCategory = (LinearLayout) findViewById(R.id.addNew);
        newCategory.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CategoryPage.this, NewCategory.class);
                startActivity(intent);

            }
        });




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
