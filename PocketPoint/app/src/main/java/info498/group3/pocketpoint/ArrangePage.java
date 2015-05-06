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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ArrangePage extends ActionBarActivity {

    private List<Icon> iconBar;
    private int howManyInBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_page);

        // removes the notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayout backButton = (LinearLayout) findViewById(R.id.backButton);
        backButton.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

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
        if(iconBar.size() >= 1) {
            TextView titleOne = (TextView) findViewById(R.id.txtTitleOne);
            titleOne.setText(iconBar.get(0).getTitle());
            ImageView imgOne = (ImageView) findViewById(R.id.imgIconOne);
            if (iconBar.get(0).getIcon() < 0) {
                imgOne.setImageBitmap(iconBar.get(0).getBitmap());
            } else {
                imgOne.setImageResource(iconBar.get(0).getIcon());
            }
        }
        if(iconBar.size() >= 2) {
            TextView titleTwo = (TextView) findViewById(R.id.txtTitleTwo);
            titleTwo.setText(iconBar.get(1).getTitle());
            ImageView imgTwo = (ImageView) findViewById(R.id.imgIconTwo);
            if(iconBar.get(1).getIcon() < 0 ){
                imgTwo.setImageBitmap(iconBar.get(1).getBitmap());
            }else {
                imgTwo.setImageResource(iconBar.get(1).getIcon());
            }
        }
        if(iconBar.size() >= 3) {
            TextView titleThree = (TextView) findViewById(R.id.txtTitleThree);
            titleThree.setText(iconBar.get(2).getTitle());
            ImageView imgThree = (ImageView) findViewById(R.id.imgIconThree);
            if(iconBar.get(2).getIcon() < 0 ){
                imgThree.setImageBitmap(iconBar.get(2).getBitmap());
            }else {
                imgThree.setImageResource(iconBar.get(2).getIcon());
            }
        }
        if(iconBar.size() >= 4) {
            TextView titleFour = (TextView) findViewById(R.id.txtTitleFour);
            titleFour.setText(iconBar.get(3).getTitle());
            ImageView imgFour = (ImageView) findViewById(R.id.imgIconFour);
            if(iconBar.get(3).getIcon() < 0 ){
                imgFour.setImageBitmap(iconBar.get(3).getBitmap());
            }else {
                imgFour.setImageResource(iconBar.get(3).getIcon());
            }
        }

        Button done = (Button) findViewById(R.id.btnDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kiddoPage = new Intent(ArrangePage.this, KiddoPage.class);
                List<String> iconNumber = new ArrayList<String>(
                        Arrays.asList("iconOne", "iconTwo", "iconThree", "iconFour"));
                for(int i = 0; i < howManyInBar; i++) {
                    Icon current = iconBar.get(i);
                    String iconNum = iconNumber.get(i);

                    kiddoPage.putExtra(iconNum + "Title",  current.getTitle());
                    if(current.getIcon() < 0 ) {
                        kiddoPage.putExtra(iconNum + "ImageInt", 1);
                        //arrange.putExtra(iconNum + "Img", current.getBitmap());
                    }else {
                        kiddoPage.putExtra(iconNum + "ImageInt", 0);
                        kiddoPage.putExtra(iconNum + "Img", current.getIcon());
                    }
                }
                kiddoPage.putExtra("howManyInBar", howManyInBar);

                startActivity(kiddoPage);
            }
        });

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
