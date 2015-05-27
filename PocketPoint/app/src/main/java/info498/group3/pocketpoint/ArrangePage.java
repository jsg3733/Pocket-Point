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
import android.view.ViewGroup;
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
    private TextView titleOne;
    private TextView titleTwo;
    private TextView titleThree;
    private TextView titleFour;
    private ImageView imgOne;
    private ImageView imgTwo;
    private ImageView imgThree;
    private ImageView imgFour;

    private ImageView removeIconOne;
    private ImageView removeIconTwo;
    private ImageView removeIconThree;
    private ImageView removeIconFour;

    private String topic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_page);

        // removes the notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


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
        topic = launchedMe.getStringExtra("category");
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

        LinearLayout backButton = (LinearLayout) findViewById(R.id.backButton);
        backButton.setOnClickListener(new LinearLayout.OnClickListener() {
            public void onClick(View v) {
                Log.i("HowManyinBar", "" + howManyInBar);
                Intent categoryPage = new Intent();
                if(topic.equals("Categories")) {
                    categoryPage = new Intent(ArrangePage.this, CategoryPage.class);
                }else {
                    categoryPage = new Intent(ArrangePage.this, WordPage.class);
                }
                List<String> iconNumber = new ArrayList<String>(
                    Arrays.asList("iconOne", "iconTwo", "iconThree", "iconFour"));
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
                    categoryPage.putExtra("category", topic);
                if(howManyInBar > 0){
                    categoryPage.putExtra("showBar", true);
                }
                //categoryPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(categoryPage);
                finish();

            }
        });

        // finding the imageviews for the X's in the corners of the icons within the iconBar
        removeIconOne = (ImageView) findViewById(R.id.btnRemoveOne);
        removeIconTwo = (ImageView) findViewById(R.id.btnRemoveTwo);
        removeIconThree = (ImageView) findViewById(R.id.btnRemoveThree);
        removeIconFour = (ImageView) findViewById(R.id.btnRemoveFour);
        // setting the onClick listener for all of the X's
        removeIconOne.setOnClickListener(removeIcon);
        removeIconTwo.setOnClickListener(removeIcon);
        removeIconThree.setOnClickListener(removeIcon);
        removeIconFour.setOnClickListener(removeIcon);


        Log.i("iconOne", iconBar.get(0).getTitle());
        if(iconBar.size() >= 1) {
            titleOne = (TextView) findViewById(R.id.txtTitleOne);
            titleOne.setText(iconBar.get(0).getTitle());
            imgOne = (ImageView) findViewById(R.id.imgIconOne);
            if (iconBar.get(0).getIcon() < 0) {
                imgOne.setImageBitmap(iconBar.get(0).getBitmap());
            } else {
                imgOne.setImageResource(iconBar.get(0).getIcon());
            }
        }
        if(iconBar.size() >= 2) {
            titleTwo = (TextView) findViewById(R.id.txtTitleTwo);
            titleTwo.setText(iconBar.get(1).getTitle());
            imgTwo = (ImageView) findViewById(R.id.imgIconTwo);
            if(iconBar.get(1).getIcon() < 0 ){
                imgTwo.setImageBitmap(iconBar.get(1).getBitmap());
            }else {
                imgTwo.setImageResource(iconBar.get(1).getIcon());
            }
            removeIconTwo.setVisibility(View.VISIBLE);
        }
        if(iconBar.size() >= 3) {
            titleThree = (TextView) findViewById(R.id.txtTitleThree);
            titleThree.setText(iconBar.get(2).getTitle());
            imgThree = (ImageView) findViewById(R.id.imgIconThree);
            if(iconBar.get(2).getIcon() < 0 ){
                imgThree.setImageBitmap(iconBar.get(2).getBitmap());
            }else {
                imgThree.setImageResource(iconBar.get(2).getIcon());
            }
            removeIconThree.setVisibility(View.VISIBLE);
        }
        if(iconBar.size() >= 4) {
            titleFour = (TextView) findViewById(R.id.txtTitleFour);
            titleFour.setText(iconBar.get(3).getTitle());
            imgFour = (ImageView) findViewById(R.id.imgIconFour);
            if(iconBar.get(3).getIcon() < 0 ){
                imgFour.setImageBitmap(iconBar.get(3).getBitmap());
            }else {
                imgFour.setImageResource(iconBar.get(3).getIcon());
            }
            removeIconFour.setVisibility(View.VISIBLE);
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

    // Is called when an X is clicked
    // Will remove that icon from the bar and shift the ones from
    // the right over to the left
    private View.OnClickListener removeIcon = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnRemoveOne:
                    switch (howManyInBar){
                        case 1:
                            //iconOne.setVisibility(View.INVISIBLE);
                            /*titleOne.setVisibility(View.INVISIBLE);
                            imgOne.setVisibility(View.INVISIBLE);
                            removeIconOne.setVisibility(View.INVISIBLE);*/
                            makeInvisible(1);
                            break;
                        case 2:
                            titleOne.setText(titleTwo.getText());
                            if(iconBar.get(1).getIcon() < 0){
                                imgOne.setImageBitmap(iconBar.get(1).getBitmap());
                            }else {
                                imgOne.setImageResource(iconBar.get(1).getIcon());
                            }
                            //iconTwo.setVisibility(View.INVISIBLE);
                            /*titleTwo.setVisibility(View.INVISIBLE);
                            imgTwo.setVisibility(View.INVISIBLE);
                            removeIconTwo.setVisibility(View.INVISIBLE);*/
                            makeInvisible(2);
                            break;
                        case 3:
                            titleOne.setText(titleTwo.getText());
                            if(iconBar.get(1).getIcon() < 0){
                                imgOne.setImageBitmap(iconBar.get(1).getBitmap());
                            }else {
                                imgOne.setImageResource(iconBar.get(1).getIcon());
                            }
                            titleTwo.setText(titleThree.getText());
                            if(iconBar.get(2).getIcon() < 0){
                                imgTwo.setImageBitmap(iconBar.get(2).getBitmap());
                            }else {
                                imgTwo.setImageResource(iconBar.get(2).getIcon());
                            }
                           //iconThree.setVisibility(View.INVISIBLE);
                            makeInvisible(3);
                            break;
                        case 4:
                            titleOne.setText(titleTwo.getText());
                            if(iconBar.get(1).getIcon() < 0){
                                imgOne.setImageBitmap(iconBar.get(1).getBitmap());
                            }else {
                                imgOne.setImageResource(iconBar.get(1).getIcon());
                            }
                            titleTwo.setText(titleThree.getText());
                            if(iconBar.get(2).getIcon() < 0){
                                imgTwo.setImageBitmap(iconBar.get(2).getBitmap());
                            }else {
                                imgTwo.setImageResource(iconBar.get(2).getIcon());
                            }
                            titleThree.setText(titleFour.getText());
                            if(iconBar.get(3).getIcon() < 0){
                                imgThree.setImageBitmap(iconBar.get(3).getBitmap());
                            }else {
                                imgThree.setImageResource(iconBar.get(3).getIcon());
                            }
                            //iconFour.setVisibility(View.INVISIBLE);
                            makeInvisible(4);
                            break;
                    }
                    howManyInBar--;
                    iconBar.remove(0);
                    Log.i("Remove", "Icon One");
                    break;
                case R.id.btnRemoveTwo:
                    switch (howManyInBar) {
                        case 2:
                           // iconTwo.setVisibility(View.INVISIBLE);
                            makeInvisible(2);
                            break;
                        case 3:
                            titleTwo.setText(titleThree.getText());
                            if(iconBar.get(2).getIcon() < 0){
                                imgTwo.setImageBitmap(iconBar.get(2).getBitmap());
                            }else {
                                imgTwo.setImageResource(iconBar.get(2).getIcon());
                            }
                            //iconThree.setVisibility(View.INVISIBLE);
                            makeInvisible(3);
                            break;
                        case 4:
                            titleTwo.setText(titleThree.getText());
                            if(iconBar.get(2).getIcon() < 0){
                                imgTwo.setImageBitmap(iconBar.get(2).getBitmap());
                            }else {
                                imgTwo.setImageResource(iconBar.get(2).getIcon());
                            }
                            titleThree.setText(titleFour.getText());
                            if(iconBar.get(3).getIcon() < 0){
                                imgThree.setImageBitmap(iconBar.get(3).getBitmap());
                            }else {
                                imgThree.setImageResource(iconBar.get(3).getIcon());
                            }
                            //iconFour.setVisibility(View.INVISIBLE);
                            makeInvisible(4);
                            break;
                    }
                    howManyInBar--;
                    iconBar.remove(1);
                    Log.i("Remove", "Icon Two");
                    break;
                case R.id.btnRemoveThree:
                    switch (howManyInBar) {
                        case 3:
                            //iconThree.setVisibility(View.INVISIBLE);
                            makeInvisible(3);
                            break;
                        case 4:
                            titleThree.setText(titleFour.getText());
                            if(iconBar.get(3).getIcon() < 0){
                                imgThree.setImageBitmap(iconBar.get(3).getBitmap());
                            }else {
                                imgThree.setImageResource(iconBar.get(3).getIcon());
                            }
                            //iconFour.setVisibility(View.INVISIBLE);
                            makeInvisible(4);
                            break;
                    }
                    howManyInBar--;
                    iconBar.remove(2);
                    Log.i("Remove", "Icon Three");
                    break;
                case R.id.btnRemoveFour:
                    howManyInBar--;
                    iconBar.remove(3);
                    //iconFour.setVisibility(View.INVISIBLE);
                    makeInvisible(4);
                    Log.i("Remove", "Icon Four");
                    break;
            }
        }
    };

    private void makeInvisible(int num){
        if(num == 1){
            titleOne.setVisibility(View.INVISIBLE);
            imgOne.setVisibility(View.INVISIBLE);
            removeIconOne.setVisibility(View.INVISIBLE);
        }else if(num == 2) {
            titleTwo.setVisibility(View.INVISIBLE);
            imgTwo.setVisibility(View.INVISIBLE);
            removeIconTwo.setVisibility(View.INVISIBLE);
        }else if(num == 3) {
            titleThree.setVisibility(View.INVISIBLE);
            imgThree.setVisibility(View.INVISIBLE);
            removeIconThree.setVisibility(View.INVISIBLE);
        }else {
            titleFour.setVisibility(View.INVISIBLE);
            imgFour.setVisibility(View.INVISIBLE);
            removeIconFour.setVisibility(View.INVISIBLE);
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
