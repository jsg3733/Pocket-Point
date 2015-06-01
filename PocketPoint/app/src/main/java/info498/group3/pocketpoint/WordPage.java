package info498.group3.pocketpoint;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This is the page that is loaded after a category is selected
// Word Page
public class WordPage extends ActionBarActivity implements GridFragmentWithBar.multipleCategories {

    // category name
    private String category;
    private List<Icon> iconBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        // removes the notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // gets the information for the category from the intent that launced it
        Intent launchedMe = getIntent();
        category = launchedMe.getStringExtra("category");

        // changes the text to show what to do on the page
        TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.wordPageDesc);

        iconBar = new ArrayList<Icon>();

        // changes the text to show the category that you are within
        //TextView txtCategory = (TextView) findViewById(R.id.txtCategory);
        //txtCategory.setText(category);

        // makes the back button visible so can click to finish the current activity
        LinearLayout backButton = (LinearLayout) findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                if(findViewById(R.id.iconBar).getVisibility() == View.VISIBLE) {
                    Intent categoryPage = new Intent(WordPage.this, CategoryPage.class);
                    List<String> iconNumber = new ArrayList<String>(
                            Arrays.asList("iconOne", "iconTwo", "iconThree", "iconFour"));
                    GridFragmentWithBar gridFragmentWithBar = new GridFragmentWithBar();
                    //List<Icon> iconBar = gridFragmentWithBar.getIconBar();
                    //int howManyInBar = iconBar.size();
                    int howManyInBar = 0;
                    if(findViewById(R.id.iconFour).getVisibility() == View.VISIBLE) {
                        howManyInBar = 4;
                    }else if(findViewById(R.id.iconThree).getVisibility() == View.VISIBLE) {
                        howManyInBar = 3;
                    }else if(findViewById(R.id.iconTwo).getVisibility() == View.VISIBLE) {
                        howManyInBar = 2;
                    }else if(findViewById(R.id.iconOne).getVisibility() == View.VISIBLE) {
                        howManyInBar = 1;
                    }
                    for(int i = 0; i < howManyInBar; i++) {
                        Icon current = iconBar.get(i);
                        String iconNum = iconNumber.get(i);

                        categoryPage.putExtra(iconNum + "Title",  current.getTitle());
                        if(current.getIcon() < 0 ) {
                            categoryPage.putExtra(iconNum + "ImageInt", 1);
                            //arrange.putExtra(iconNum + "Img", current.getBitmap());
                        }else {
                            categoryPage.putExtra(iconNum + "ImageInt", 0);
                            categoryPage.putExtra(iconNum + "Img", current.getIcon());
                        }
                    }
                    categoryPage.putExtra("howManyInBar", howManyInBar);
                    if(howManyInBar > 0){
                        categoryPage.putExtra("showBar", true);
                    }
                    categoryPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(categoryPage);
                }
                finish();

            }
        });

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

        // changes the text for the addnew field to Add New Word
        TextView addNew = (TextView) findViewById(R.id.txtAddNew);
        addNew.setText(getString(R.string.new_word));
        // sets on click listener for the add new word button that will go to that intent on click
        LinearLayout newCategory = (LinearLayout) findViewById(R.id.addNew);
        newCategory.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(WordPage.this, NewWord.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });






    }


    public void gridWithBar(String topic){
        Bundle bundle = new Bundle();
        bundle.putString("category", topic);

        GridFragmentWithBar gridFragmentWithBar = new GridFragmentWithBar();
        gridFragmentWithBar.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.gridFragmentPlaceholder, gridFragmentWithBar);
        fragmentTransaction.commit();
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


    public void addIcon(Icon icon){
        iconBar.add(icon);
        //iconBar.add(new Icon(0, "hello"));
    }

    public void removeIcon(int position){
        iconBar.remove(position);
    }
}
