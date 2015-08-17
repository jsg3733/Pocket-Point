package info498.group3.pocketpoint;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class KiddoPage extends ActionBarActivity {

    private List<Icon> iconBar;
    private int howManyInBar;
    private MediaPlayer mp;
    private Boolean playing;
    private List<String> smallAudioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent launchedMe = getIntent();
        howManyInBar = launchedMe.getIntExtra("howManyInBar", 1);
        Boolean nowThen = launchedMe.getBooleanExtra("nowThen", false);
        if(howManyInBar > 2) {
            setContentView(R.layout.activity_kiddo_page);
        }else if(!nowThen) {
            setContentView(R.layout.activity_kiddo_page_twoword);
        }else {
            setContentView(R.layout.activity_kiddo_page_nowthen);
        }

        // removes the notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mp = new MediaPlayer();
        playing = false;
        smallAudioList = new ArrayList<>(
                Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "catch", "throw", "new"));

        ImageView backButton = (ImageView) findViewById(R.id.btnBackButton);
        backButton.setOnLongClickListener(new ImageView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(playing) {
                    mp.stop();
                    mp.reset();
                }
                finish();
                return true;
            }
        });
        backButton.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(playing) {
                    mp.stop();
                    mp.reset();
                }
                finish();*/
                TextView backText = (TextView) findViewById(R.id.txtBack);
                backText.setVisibility(View.VISIBLE);
            }
        });

        iconBar = new ArrayList<Icon>();
        /*Intent launchedMe = getIntent();
        howManyInBar = launchedMe.getIntExtra("howManyInBar", 1);*/
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

                Bitmap storedimagepath = loadImageFromStorage(directory.getAbsolutePath(), iconTitle.replaceAll("\\s+", "").replaceAll("'","").toLowerCase());

                Icon current = new Icon((-1), iconTitle);
                current.setBitmap(storedimagepath);
                iconBar.add(current);
            }
        }

        /*LinearLayout iconOne = (LinearLayout) findViewById(R.id.iconOne);
        LinearLayout iconTwo = (LinearLayout) findViewById(R.id.iconTwo);
        LinearLayout iconThree = (LinearLayout) findViewById(R.id.iconThree);
        LinearLayout iconFour = (LinearLayout) findViewById(R.id.iconFour);*/


        if(iconBar.size() >= 1) {
            LinearLayout iconOne = (LinearLayout) findViewById(R.id.iconOne);
            TextView titleOne = (TextView) findViewById(R.id.txtTitleOne);
            titleOne.setText(iconBar.get(0).getTitle());
            ImageView imgOne = (ImageView) findViewById(R.id.imgIconOne);
            if (iconBar.get(0).getIcon() < 0) {
                imgOne.setImageBitmap(iconBar.get(0).getBitmap());
            } else {
                imgOne.setImageResource(iconBar.get(0).getIcon());
            }
            iconOne.setOnClickListener(sound);
        }
        if(iconBar.size() >= 2) {
            LinearLayout iconTwo = (LinearLayout) findViewById(R.id.iconTwo);
            TextView titleTwo = (TextView) findViewById(R.id.txtTitleTwo);
            titleTwo.setText(iconBar.get(1).getTitle());
            ImageView imgTwo = (ImageView) findViewById(R.id.imgIconTwo);
            if(iconBar.get(1).getIcon() < 0 ){
                imgTwo.setImageBitmap(iconBar.get(1).getBitmap());
            }else {
                imgTwo.setImageResource(iconBar.get(1).getIcon());
            }
            iconTwo.setVisibility(View.VISIBLE);
            iconTwo.setOnClickListener(sound);
        }
        if(iconBar.size() >= 3) {
            LinearLayout iconThree = (LinearLayout) findViewById(R.id.iconThree);
            TextView titleThree = (TextView) findViewById(R.id.txtTitleThree);
            titleThree.setText(iconBar.get(2).getTitle());
            ImageView imgThree = (ImageView) findViewById(R.id.imgIconThree);
            if(iconBar.get(2).getIcon() < 0 ){
                imgThree.setImageBitmap(iconBar.get(2).getBitmap());
            }else {
                imgThree.setImageResource(iconBar.get(2).getIcon());
            }
            iconThree.setVisibility(View.VISIBLE);
            iconThree.setOnClickListener(sound);
        }
        if(iconBar.size() >= 4) {
            LinearLayout iconFour = (LinearLayout) findViewById(R.id.iconFour);
            TextView titleFour = (TextView) findViewById(R.id.txtTitleFour);
            titleFour.setText(iconBar.get(3).getTitle());
            ImageView imgFour = (ImageView) findViewById(R.id.imgIconFour);
            if(iconBar.get(3).getIcon() < 0 ){
                imgFour.setImageBitmap(iconBar.get(3).getBitmap());
            }else {
                imgFour.setImageResource(iconBar.get(3).getIcon());
            }
            iconFour.setVisibility(View.VISIBLE);
            iconFour.setOnClickListener(sound);
        }

        /*iconOne.setOnClickListener(sound);
        iconTwo.setOnClickListener(sound);
        iconThree.setOnClickListener(sound);
        iconFour.setOnClickListener(sound);*/

    }

    private View.OnClickListener sound = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!playing) {
                int musicID = -1;
                Uri path = null;
                switch (v.getId()) {
                case R.id.iconOne:
                    if(iconBar.get(0).getIcon() > 0 ){
                        if(smallAudioList.contains(iconBar.get(0).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase())){
                            musicID = getResources().getIdentifier("sound_" + iconBar.get(0).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase(), "raw", getPackageName());
                        }else {
                            musicID = getResources().getIdentifier(iconBar.get(0).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase(), "raw", getPackageName());
                        }
                    }else {
                        String name  = iconBar.get(0).getTitle().replaceAll("\\s+", "").replaceAll("'","").toLowerCase();
                        path = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()
                                +  "/" + name + ".3gp");
                    }
                    break;
                case R.id.iconTwo:
                    if(iconBar.get(1).getIcon() > 0 ){
                        if(smallAudioList.contains(iconBar.get(1).getTitle().replaceAll("\\s+", "").replaceAll("'","").toLowerCase())){
                            musicID = getResources().getIdentifier("sound_" + iconBar.get(1).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase(), "raw", getPackageName());
                        }else {
                            musicID = getResources().getIdentifier(iconBar.get(1).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase(), "raw", getPackageName());
                        }
                    }else {
                        String name  = iconBar.get(1).getTitle().replaceAll("\\s+", "").replaceAll("'","").toLowerCase();
                        path = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()
                                +  "/" + name + ".3gp");
                    }
                    break;
                case R.id.iconThree:
                    if(iconBar.get(2).getIcon() > 0 ){
                        if(smallAudioList.contains(iconBar.get(2).getTitle().replaceAll("\\s+", "").replaceAll("'","").toLowerCase())){
                            musicID = getResources().getIdentifier("sound_" + iconBar.get(2).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase(), "raw", getPackageName());
                        }else {
                            musicID = getResources().getIdentifier(iconBar.get(2).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase(), "raw", getPackageName());
                        }
                    }else {
                        String name  = iconBar.get(2).getTitle().replaceAll("\\s+", "").replaceAll("'","").toLowerCase();
                        path = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()
                                +  "/" + name + ".3gp");
                    }
                    break;
                case R.id.iconFour:
                    if(iconBar.get(3).getIcon() > 0 ){
                        if(smallAudioList.contains(iconBar.get(3).getTitle().replaceAll("\\s+", "").replaceAll("'","").toLowerCase())){
                            musicID = getResources().getIdentifier("sound_" + iconBar.get(3).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase(), "raw", getPackageName());
                        }else {
                            musicID = getResources().getIdentifier(iconBar.get(3).getTitle().replaceAll("\\s+", "").replaceAll("'", "").toLowerCase(), "raw", getPackageName());
                        }
                    }else {
                        String name  = iconBar.get(3).getTitle().replaceAll("\\s+", "").replaceAll("'","").toLowerCase();
                        path = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()
                                +  "/" + name + ".3gp");
                    }
                    break;
                }
                if(musicID > 0) {
                    mp = MediaPlayer.create(KiddoPage.this, musicID);
                }else if(path != null){
                    mp = MediaPlayer.create(KiddoPage.this, path);
                }else {
                    mp = MediaPlayer.create(KiddoPage.this, R.raw.test);
                }
                mp.start();
                SoundtrackPlayerListener Music = new SoundtrackPlayerListener();
                Music.onCompletion(mp);
                playing = true;
            }

        }
    };

    private class SoundtrackPlayerListener implements MediaPlayer.OnCompletionListener{

        public void onCompletion(MediaPlayer mp) {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.reset();
                    playing = false;
                    //finish();
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kiddo_page, menu);
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

    @Override
    public void onBackPressed(){
        // doing nothing
    }
}

