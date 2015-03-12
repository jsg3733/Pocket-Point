package info498.group3.pocketpoint;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class NewCategory extends ActionBarActivity {

    private Button save;
    private EditText categoryField;
    private ImageView image;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap savedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayout backButton = (LinearLayout) findViewById(R.id.backButton);
        Button cancel = (Button)findViewById(R.id.btnCancel);
        backButton.setOnClickListener(back);
        cancel.setOnClickListener(back);


        save = (Button) findViewById(R.id.btnSave);
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
                    if(input.equals("")) {
                        save.setEnabled(false);
                    }else if(image.getVisibility() == View.VISIBLE){
                        save.setEnabled(true);
                    }
            }
        };

        categoryField.addTextChangedListener(chan);
        Button buttonLoadImage = (Button)findViewById(R.id.btnImport);

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
        Button takeimage = (Button)findViewById(R.id.btnTakePhoto);
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
                String categories = "";
                saveToInternalStorage(savedImage, categoryField.getText().toString());
                try {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("Categories")));
                    String inputString = inputReader.readLine();
                    while (inputString != null) {
                        categories += inputString + "\n";
                        inputString = inputReader.readLine();
                    }
                    Log.i("internalFile", "pass");
                } catch (IOException e) {
                    Log.i("internalFile", "fail");
                    e.printStackTrace();
                }


                try{
                    FileOutputStream fos = openFileOutput("Categories", Context.MODE_PRIVATE);
                    categories += categoryField.getText().toString();
                    fos.write(categories.getBytes());
                    fos.close();
                    Intent backToCategoryPage = new Intent(NewCategory.this, CategoryPage.class);
                    backToCategoryPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(backToCategoryPage);
                    finish();

                }catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });



    }




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

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            //pathy = targetUri;
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                savedImage = bitmap;
                image.setImageBitmap(bitmap);
                image.setVisibility(View.VISIBLE);
                if(!categoryField.getText().toString().equals("")) {
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
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 1, fos);
            Log.v("point", "shit no the fan");
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }
}
