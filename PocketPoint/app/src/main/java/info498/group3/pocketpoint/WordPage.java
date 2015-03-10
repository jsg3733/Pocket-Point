package info498.group3.pocketpoint;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


public class WordPage extends ActionBarActivity {

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent launchedMe = getIntent();
        category = launchedMe.getStringExtra("category");

        LinearLayout backButton = (LinearLayout) findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("category", category);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        GridFragment gridFragment = new GridFragment();
        gridFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.gridFragmentPlaceholder, gridFragment);
        fragmentTransaction.commit();


        TextView addNew = (TextView) findViewById(R.id.txtAddNew);
        addNew.setText(getString(R.string.new_word));
        LinearLayout newCategory = (LinearLayout) findViewById(R.id.addNew);
        newCategory.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(WordPage.this, NewCategory.class);
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
