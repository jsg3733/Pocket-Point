package info498.group3.pocketpoint;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ArrangePage extends ActionBarActivity {

    ArrayList<Icon> iconBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_page);

        // With serializable and passing extra
        //Intent launchedMe = getIntent();
        //iconBar = (ArrayList<Icon>)launchedMe.getSerializableExtra("iconBar");
        //Log.i("iconOne", iconBar.get(0).getTitle());
        Bundle b = getIntent().getExtras();
        if (b != null) {
            iconBar = (ArrayList<Icon>)b.getSerializable("iconBar");
        }
        Log.i("iconOne", iconBar.get(0).getTitle());
        TextView titleOne = (TextView) findViewById(R.id.txtTitleOne);
        titleOne.setText(iconBar.get(0).getTitle());
        ImageView imgOne = (ImageView) findViewById(R.id.imgIconOne);
        if(iconBar.get(0).getIcon() < 0 ){
            imgOne.setImageBitmap(iconBar.get(0).getBitmap());
        }else {
            imgOne.setImageResource(iconBar.get(0).getIcon());
        }
        if(iconBar.size() > 1) {
            TextView titleTwo = (TextView) findViewById(R.id.txtTitleTwo);
            titleTwo.setText(iconBar.get(1).getTitle());
            ImageView imgTwo = (ImageView) findViewById(R.id.imgIconTwo);
            if(iconBar.get(1).getIcon() < 0 ){
                imgTwo.setImageBitmap(iconBar.get(1).getBitmap());
            }else {
                imgTwo.setImageResource(iconBar.get(1).getIcon());
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_arrange_page, menu);
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
