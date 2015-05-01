package info498.group3.pocketpoint;

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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ArrangePage extends ActionBarActivity {

    List<Icon> iconBar;
    private int howManyInBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_page);

        // With serializable and passing extra (problem with Bitmap images)
        //Intent launchedMe = getIntent();
        //iconBar = (ArrayList<Icon>)launchedMe.getSerializableExtra("iconBar");
        //Log.i("iconOne", iconBar.get(0).getTitle());

        // Serializable passing as bundle (problem with Bitmap images)
        /*Bundle b = getIntent().getExtras();
        if (b != null) {
            iconBar = (ArrayList<Icon>)b.getSerializable("iconBar");
        }*/

        iconBar = new ArrayList<Icon>();
        Intent launchedMe = getIntent();
        howManyInBar = launchedMe.getIntExtra("howManyInBar", 1);
        List<String> iconNumber = new ArrayList<String>(
                Arrays.asList("iconOne", "iconTwo", "iconThree", "iconFour"));
        for(int i = 0; i < howManyInBar; i++) {
            String iconNum = iconNumber.get(i);
            String iconTitle = launchedMe.getStringExtra(iconNum + "Title");
            int imgInt = launchedMe.getIntExtra(iconNum + "ImageInt", 2);
            if(imgInt == 0) {
                int iconInt = launchedMe.getIntExtra(iconNum + "Img", 0);
                Icon current = new Icon(iconInt, iconTitle);
                iconBar.add(current);
            }else {

                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                Bitmap storedimagepath = loadImageFromStorage(directory.getAbsolutePath(), iconTitle.trim());



                //Bitmap imgBitmap = launchedMe.getParcelableExtra(iconNum + "Img");
                Icon current = new Icon((-1), iconTitle);
                current.setBitmap(storedimagepath);
                //current.setBitmap(imgBitmap);
                iconBar.add(current);
            }
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

    //loading image from storage
    //return associated bitmap
    private Bitmap loadImageFromStorage(String path, String filename)
    {

        try {
            File f=new File(path, filename);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            Log.v("nahblah", "it didn't run");
            e.printStackTrace();
        }

        return null;
    }
}
