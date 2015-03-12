package info498.group3.pocketpoint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileNotFoundException;


public class NewWord extends ActionBarActivity {

    private Button save;
    private EditText wordField;
    private ImageView image;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        LinearLayout backButton = (LinearLayout) findViewById(R.id.backButton);
        Button cancel = (Button)findViewById(R.id.btnCancel);
        backButton.setOnClickListener(back);
        cancel.setOnClickListener(back);

        //final Button save = (Button) findViewById(R.id.btnSave);
        save = (Button) findViewById(R.id.btnSave);
        //final EditText categoryField = (EditText) findViewById(R.id.edtxtCategoryField);
        wordField = (EditText) findViewById(R.id.edtxtWordField);
        //final ImageView image = (ImageView) findViewById(R.id.imgPreview);
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

        wordField.addTextChangedListener(chan);
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
    }

    private View.OnClickListener back = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            image.setVisibility(View.VISIBLE);
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
                image.setImageBitmap(bitmap);
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
}
