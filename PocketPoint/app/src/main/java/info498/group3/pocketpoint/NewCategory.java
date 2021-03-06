package info498.group3.pocketpoint;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// Is the Create New Category Page linked from the Category Page
public class NewCategory extends ActionBarActivity {

    private ImageView save;
    private EditText categoryField;
    private ImageView image;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap savedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        // gets rid of the notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //makes a onclick listener for the back arrow and cancel button
        LinearLayout backButton = (LinearLayout) findViewById(R.id.backButton);
        ImageView cancel = (ImageView)findViewById(R.id.btnCancel);
        // sets the onclick listener to the back function
        backButton.setOnClickListener(back);
        cancel.setOnClickListener(back);


        save = (ImageView) findViewById(R.id.btnSave);
        categoryField = (EditText) findViewById(R.id.edtxtCategoryField);
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
                        save.setVisibility(View.INVISIBLE);
                    // if edittext has text and image preview is visible then save button is enabled
                    }else if(image.getVisibility() == View.VISIBLE){
                        save.setVisibility(View.VISIBLE);
                    }
            }
        };

        // sets textwatcher for category edittext
        categoryField.addTextChangedListener(chan);
        ImageView buttonLoadImage = (ImageView)findViewById(R.id.btnImport);

        //import
        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }});

        //camera
        ImageView takeimage = (ImageView)findViewById(R.id.btnTakePhoto);
        takeimage.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0){
                dispatchTakePictureIntent();
            }
        });

        //save button click listener
        save.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> categoryList = new ArrayList<String>();
                categoryList.add("::");
                categoryList.add("categories");
                categoryList.add("savedpages");
                categoryList.add("addnew");
                String categories = "";
                // reads through the category internal file and stores all lines to a string
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("Categories")));
                    String inputString = inputReader.readLine();
                    while (inputString != null) {
                        categories += inputString + "\n";
                        categoryList.add(inputString.replaceAll("\\s+", "").replaceAll("'","").toLowerCase());
                        inputString = inputReader.readLine();
                    }
                    Log.i("internalFile", "pass");
                    inputReader.close();
                } catch (IOException e) {
                    Log.i("internalFile", "fail");
                    e.printStackTrace();
                }

                try{
                    // reads the app category file and makes sure do not repeat the preloaded category names
                    // brings the file into a inputstream
                    AssetManager am = getApplicationContext().getAssets();
                    InputStream is = am.open("categories.txt");

                    // turns the inputstream of the file into a bufferedReader and reads the first line
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();

                    // while line read is not null will keep adding lines to title list
                    while(line != null) {
                        categoryList.add(line.replaceAll("\\s+", "").replaceAll("'","").toLowerCase());
                        line = br.readLine();
                    }
                    // catch exception if file is not found
                }catch (IOException e) {
                    Log.i("New Category", "File is not being found");
                }


                // rewrites the category internal file with the new category added
                try{
                    FileOutputStream fos = openFileOutput("Categories", Context.MODE_PRIVATE);
                    // adds the new category to the end of the file
                    String newCategory = categoryField.getText().toString();
                    if(!categoryList.contains(newCategory.replaceAll("\\s+", "").replaceAll("'","").toLowerCase())) {
                        categories += newCategory + "\n";
                        categories += "=" + "\n";
                        categories += "::";
                        fos.write(categories.getBytes());
                        fos.close();
                        // saves the image internally
                        saveToInternalStorage(savedImage, newCategory.replaceAll("\\s+", "").replaceAll("'","").toLowerCase());
                        // goes back to the main category page
                        Intent backToCategoryPage = new Intent(NewCategory.this, CategoryPage.class);
                        // closes activities in the background
                        backToCategoryPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(backToCategoryPage);
                        finish();
                    }else {
                        fos.write(categories.getBytes());
                        fos.close();
                        Toast.makeText(NewCategory.this, "Cannot have the same user created names for Categories and Words", Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });



    }




    // on click will finish the activity
    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            finish();
        }
    };


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
            if(!categoryField.getText().toString().equals("")) {
                save.setVisibility(View.VISIBLE);
            }
        }else if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            //pathy = targetUri;
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                savedImage = bitmap.createScaledBitmap(bitmap, 500, 500, false);
                //savedImage = bitmap;
                image.setImageBitmap(savedImage);
                image.setVisibility(View.VISIBLE);
                if(!categoryField.getText().toString().equals("")) {
                    save.setVisibility(View.VISIBLE);
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
            // bitmapImage.compress(Bitmap.CompressFormat.JPEG, 1, fos);
            Log.v("point", "shit no the fan");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }
}
