package info498.group3.pocketpoint;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// Is the Create New Word Page linked from the Word Page
public class NewWord extends ActionBarActivity {

    private Button save;
    private EditText wordField;
    private ImageView image;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap savedImage;

    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

    private static String audioFilePath;
    private static ImageView stopButton;
    private static ImageView playButton;
    private static ImageView recordButton;

    private boolean isRecording = false;
    private boolean recordingMade = false;

    private String category;

    private CountDownTimer countDownTimer;
    private Boolean playing;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        // gets rid of the notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent launchedMe = getIntent();
        category = launchedMe.getStringExtra("category");
        playing = false;

        //makes a onclick listener for the back arrow and cancel button
        LinearLayout backButton = (LinearLayout) findViewById(R.id.backButton);
        Button cancel = (Button)findViewById(R.id.btnCancel);
        // sets the onclick listener to the back function
        backButton.setOnClickListener(back);
        cancel.setOnClickListener(back);


        save = (Button) findViewById(R.id.btnSave);
        wordField = (EditText) findViewById(R.id.edtxtWordField);
        image = (ImageView) findViewById(R.id.imgPreview);

        TextWatcher chan = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                // if edittext field is empty save button is disabled
                if(input.equals("")) {
                    save.setEnabled(false);
                    // if edittext has text and image preview is visible then save button is enabled
                }else if(image.getVisibility() == View.VISIBLE && recordingMade){
                    save.setEnabled(true);
                }
            }
        };

        // sets textwatcher for word edittext
        wordField.addTextChangedListener(chan);
        Button buttonLoadImage = (Button)findViewById(R.id.btnImport);

        //import
        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finishPlayOrRecord();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

        //camera
        Button takeimage = (Button)findViewById(R.id.btnTakePhoto);
        takeimage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0){
                finishPlayOrRecord();
                dispatchTakePictureIntent();
            }
        });




        //save button click listener
        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPlayOrRecord();
                String words = "";
                /*String name = wordField.getText().toString();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/" + name + ".3gp";
                mediaRecorder.setOutputFile(path);*/
                List<String> wordList = new ArrayList<>();
                wordList.add("::");
                wordList.add("categories");
                wordList.add("savedpages");
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("Categories")));
                    String inputString = inputReader.readLine();
                    while (inputString != null) {
                        wordList.add(inputString.replaceAll("\\s+", "").replaceAll("'","").toLowerCase());
                        inputString = inputReader.readLine();
                    }
                    Log.i("internalFile", "pass");
                    inputReader.close();
                } catch (IOException e) {
                    Log.i("internalFile", "fail");
                    e.printStackTrace();
                }


                // reads through the words internal file and stores all lines to a string
                Boolean addWord = false;
                Boolean stillNeedToAddWord = true;
                String newWord = wordField.getText().toString();
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("Categories")));
                    String inputString = inputReader.readLine();
                    while (inputString != null) {
                        if(inputString.equals("::") && addWord) {
                            words += newWord + "\n";
                            Log.i("Testing", "Adding Word");
                            addWord = false;
                            stillNeedToAddWord = false;

                            File sourceFile = new File(audioFilePath);

                            String pathName = newWord.replaceAll("\\s+", "").replaceAll("'","").toLowerCase();
                            String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + pathName +".3gp";
                            File destFile = new File(path);


                        /*if (!destFile.exists()) {
                            try {
                                destFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }*/

                            try{
                                FileInputStream audioFis = new FileInputStream(sourceFile);
                                FileOutputStream audioFos = new FileOutputStream(destFile);
                                byte[] buf = new byte[1024];
                                int bytesRead;

                                while ((bytesRead = audioFis.read(buf)) > 0) {
                                    audioFos.write(buf, 0, bytesRead);
                                }
                                audioFis.close();
                                audioFos.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // saves the image internally
                            saveToInternalStorage(savedImage, pathName);

                        }
                        words += inputString + "\n";
                        if(inputString.toLowerCase().replaceAll("\\s+", "").replaceAll("'","").equals(
                                category.toLowerCase().replaceAll("\\s+", "").replaceAll("'",""))  && !addWord){
                            //words += wordField.getText().toString();
                            if(!wordList.contains((newWord.replaceAll("\\s+", "").replaceAll("'","").toLowerCase()))){
                                addWord = true;
                            }
                            Log.i("Testing", "Should be adding word");
                        }
                        inputString = inputReader.readLine();
                    }
                    Log.i("internalFile", "pass");
                    inputReader.close();
                } catch (IOException e) {
                    Log.i("internalFile", "fail");
                    e.printStackTrace();
                }

                // rewrites the category internal file with the new word added
                try{
                    FileOutputStream fos = openFileOutput("Categories", Context.MODE_PRIVATE);
                    // adds the new word to the end of the file
                    if(stillNeedToAddWord  && !wordList.contains(newWord.replaceAll("\\s+", "").replaceAll("'","").toLowerCase())) {
                        words += category + "\n";
                        words += "!" + "\n";
                        words += newWord + "\n";
                        words += "::";

                        Log.i("Testing Words", words);
                        //words += wordField.getText().toString();
                        fos.write(words.getBytes());
                        fos.close();

                        File sourceFile = new File(audioFilePath);

                        String pathName = newWord.replaceAll("\\s+", "").replaceAll("'","").toLowerCase();
                        String path =Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + pathName +".3gp";
                        File destFile = new File(path);


                        /*if (!destFile.exists()) {
                            try {
                                destFile.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }*/

                        try{
                            FileInputStream audioFis = new FileInputStream(sourceFile);
                            FileOutputStream audioFos = new FileOutputStream(destFile);
                            byte[] buf = new byte[1024];
                            int bytesRead;

                            while ((bytesRead = audioFis.read(buf)) > 0) {
                                audioFos.write(buf, 0, bytesRead);
                            }
                            audioFis.close();
                            audioFos.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // saves the image internally
                        saveToInternalStorage(savedImage, pathName);


                        // goes back to the main category page
                        Intent backToCategoryPage = new Intent(NewWord.this, CategoryPage.class);
                        // closes activities in the background
                        backToCategoryPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(backToCategoryPage);
                        finish();
                    }else if(stillNeedToAddWord) {
                        fos.write(words.getBytes());
                        fos.close();
                        Log.i("multiple words", "failed");
                        Toast.makeText(NewWord.this, "Cannot have the same user created names for Categories and Words", Toast.LENGTH_SHORT).show();
                    }else {
                        fos.write(words.getBytes());
                        fos.close();
                        // goes back to the main category page
                        Intent backToCategoryPage = new Intent(NewWord.this, CategoryPage.class);
                        // closes activities in the background
                        backToCategoryPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(backToCategoryPage);
                        finish();
                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });


        recordButton = (ImageView) findViewById(R.id.imgRecord);
        playButton = (ImageView) findViewById(R.id.imgPlay);
        stopButton = (ImageView) findViewById(R.id.imgStop);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    recordAudio(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playing = true;
                    playAudio(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio(v);
            }
        });

        if (!hasMicrophone()) {
            stopButton.setEnabled(false);
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
        } else {
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
        }


        audioFilePath =
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/categories.3gp";


    }

    // on click will finish the activity
    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            back();
        }
    };

    public void recordAudio (View view) throws IOException {
        
            isRecording = true;
            stopButton.setEnabled(true);
            playButton.setEnabled(false);
            recordButton.setEnabled(false);

            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                //String name = wordField.getText().toString().replaceAll("\\s+", "").replaceAll("'","").toLowerCase();
                //audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                //        + "/" + name + ".3gp";
                //File audio = new File(getApplicationContext().getExternalFilesDir(null), "/categories.3gp");
                mediaRecorder.setOutputFile(audioFilePath);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mediaRecorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaRecorder.start();
            countDownTimer = new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Toast.makeText(NewWord.this, "Recording Done", Toast.LENGTH_SHORT).show();
                //recordButton.setEnabled(false);
                recordButton.setEnabled(true);
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                recordingMade = true;
                if(!wordField.getText().toString().equals("") && image.getVisibility() == View.VISIBLE) {
                    save.setEnabled(true);
                }

            }};
            countDownTimer.start();
            /*recordingMade = true;
            if(!wordField.getText().toString().equals("") && image.getVisibility() == View.VISIBLE) {
                save.setEnabled(true);
            }*/
    }

    public void stopAudio (View view) {

        stopButton.setEnabled(false);
        playButton.setEnabled(true);

        if (isRecording)
        {
            //recordButton.setEnabled(false);
            recordButton.setEnabled(true);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            countDownTimer.cancel();
            recordingMade = true;
            if(!wordField.getText().toString().equals("") && image.getVisibility() == View.VISIBLE) {
                save.setEnabled(true);
            }
        } else {
            mediaPlayer.release();
            mediaPlayer = null;
            playing = false;
            recordButton.setEnabled(true);
            //countDownTimer.cancel();
        }
    }

    public void playAudio (View view) throws IOException {
        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.prepare();
        mediaPlayer.start();
        SoundtrackPlayerListener Music = new SoundtrackPlayerListener();
        Music.onCompletion(mediaPlayer);
    }

    private class SoundtrackPlayerListener implements MediaPlayer.OnCompletionListener{

        public void onCompletion(MediaPlayer mp) {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.reset();
                    playing = false;
                    Toast.makeText(NewWord.this, "Playing Done", Toast.LENGTH_SHORT).show();
                    stopButton.setEnabled(false);
                    playButton.setEnabled(true);
                    recordButton.setEnabled(true);
                    //finish();
                }
            });
        }
    }



    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_category, menu);
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

    //for gallery and picture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            savedImage = imageBitmap.createScaledBitmap(imageBitmap, 500, 500, false);
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(savedImage);
            if(!wordField.getText().toString().equals("") && recordingMade) {
                save.setEnabled(true);
            }
        }else if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            //pathy = targetUri;
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                savedImage = bitmap.createScaledBitmap(bitmap, 500, 500, false);
                image.setImageBitmap(savedImage);
                image.setVisibility(View.VISIBLE);
                if(!wordField.getText().toString().equals("")) {
                    save.setEnabled(true);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //for camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //works
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    //save to internal directory
    private String saveToInternalStorage(Bitmap bitmapImage, String pathname){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, pathname);
        FileOutputStream fos = null;
        try {
            Log.v("asdf", "it ran");
            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            //Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmapImage, 500, 500, false);
            //resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            //bitmapImage.compress(Bitmap.CompressFormat.JPEG, 1, fos);
            Log.v("point", "shit no the fan");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    @Override
    public void onBackPressed(){
        back();
    }

    public void back(){
        finishPlayOrRecord();
        finish();
    }

    public void finishPlayOrRecord(){
        if(playing) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            playing = false;
        }else if(isRecording) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            countDownTimer.cancel();
            isRecording = false;
        }
    }

}
