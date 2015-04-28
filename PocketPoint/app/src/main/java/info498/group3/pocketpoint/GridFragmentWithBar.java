package info498.group3.pocketpoint;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GridFragmentWithBar extends Fragment {

    private GridView gridView;
    private List<String> names;
    private String topic;
    private int howManyInBar;
    private Boolean iconBarVisible;
    //private final List<String> foods = new ArrayList<>(Arrays.asList("Apple", "Banana", "Bread", "Cake", "Cheese", "Cracker",
    //"Egg", "Juice", "Milk", "Pizza", "Stix", "Water"));

    private TextView titleOne;
    private TextView titleTwo;
    private TextView titleThree;
    private TextView titleFour;
    private ImageView imgOne;
    private ImageView imgTwo;
    private ImageView imgThree;
    private ImageView imgFour;
    private LinearLayout iconOne;
    private LinearLayout iconTwo;
    private LinearLayout iconThree;
    private LinearLayout iconFour;
    private List<Icon> iconBar;



    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View gridFragmentWithBar = inflater.inflate(R.layout.gridview_fragment_with_bar, container, false);

        // takes in the topic/category that was passed in
        Bundle info = this.getArguments();
        topic = info.getString("category");
        howManyInBar = 0;
        iconBarVisible = false;
        Log.i("FragmentLoad", topic);

        // array list of icon titles
        names = new ArrayList<>();


        // is reading through pre-stored files to get the titles of objects
        try{
            String file = "food.txt";
            // if is not food will also do for categories and activities because only
            // have these images at the moment
            if(topic.equals("Categories") || topic.equals("Activities")) {
                file = topic.toLowerCase() + ".txt";
            }
            // brings the file into a inputstream
            AssetManager am = gridFragmentWithBar.getContext().getAssets();
            InputStream is = am.open(file);

            // turns the inputstream of the file into a bufferedReader and reads the first line
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();

            // while line read is not null will keep adding lines to title list
            while(line != null) {
                names.add(line);
                line = br.readLine();

            }
            // catch exception if file is not found
        }catch (IOException e) {
            Log.i("GridFragment", "File is not being found");
        }

        // is the array list of icons that have all the information needed for gridview
        final List<Icon> icons = new ArrayList<>();

        //internal category file
        if(topic.equals("Categories")) {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(gridFragmentWithBar.getContext().openFileInput("Categories")));
                String inputString = inputReader.readLine();
                while (inputString != null) {
                    //getting image
                    ContextWrapper cw = new ContextWrapper(gridFragmentWithBar.getContext());
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    Bitmap storedimagepath = loadImageFromStorage(directory.getAbsolutePath(), inputString.trim());

                    //set icon to negative value
                    Icon bitmapIcon = new Icon(-1, inputString);
                    //set bitmap on icon class
                    bitmapIcon.setBitmap(storedimagepath);
                    //add to icon list
                    icons.add(bitmapIcon);


                    //icons.add(new Icon(R.drawable.ic_launcher, inputString));
                    inputString = inputReader.readLine();
                }
                Log.i("internalFile", "pass");

            } catch (IOException e) {
                Log.i("internalFile", "fail");
                e.printStackTrace();
            }
        }




        //final List<Icon> icons = new ArrayList<>();

        //if in mywords category pull from internal mywords file
        if(topic.equals("MYWORDS")) {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(gridFragmentWithBar.getContext().openFileInput("Words")));
                String inputString = inputReader.readLine();
                while (inputString != null) {
                    //getting image
                    ContextWrapper cw = new ContextWrapper(gridFragmentWithBar.getContext());
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                    Bitmap storedimagepath = loadImageFromStorage(directory.getAbsolutePath(), inputString.trim());

                    //set icon to negative value
                    Icon bitmapIcon = new Icon(-1, inputString);
                    //set bitmap on icon class
                    bitmapIcon.setBitmap(storedimagepath);
                    //add to icon list
                    icons.add(bitmapIcon);


                    //icons.add(new Icon(R.drawable.ic_launcher, inputString));
                    inputString = inputReader.readLine();
                }
                Log.i("internalFile", "pass");

            } catch (IOException e) {
                Log.i("internalFile", "fail");
                e.printStackTrace();
            }

        }else {  // for getting all words if not in MYWORDS section

            // goes through all pre-stored icons based on names
            for (int i = 0; i < names.size(); i++) {
                if (topic.equals("Categories") || topic.equals("Activities")) {
                    // makes name into one word
                    String name = names.get(i).replaceAll("\\s+", "").toLowerCase();
                    // gets the resource id for the object based on drawable name
                    int resID = getResources().getIdentifier(topic.toLowerCase() + "_" + name, "drawable", getActivity().getPackageName());
                    // adds each icon to icon list
                    icons.add(new Icon(resID, names.get(i)));
                } else {
                    // makes name into one word
                    String name = names.get(i).replaceAll("\\s+", "").toLowerCase();
                    // gets the resource id for the object based on drawable name
                    int resID = getResources().getIdentifier("food_" + name, "drawable", getActivity().getPackageName());
                    // adds each icon to icon list
                    icons.add(new Icon(resID, names.get(i)));
                }

            }
        }
        Collections.sort(icons, new IconComparator());
        // creates the gridview with all the icons by calling customGridAdapter
        gridView = (GridView) gridFragmentWithBar.findViewById(R.id.myGridView);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(gridFragmentWithBar.getContext(), R.layout.gridview_cell, icons);
        gridView.setAdapter(gridAdapter);
        iconBar = new ArrayList<>();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // is the title of the icon based on the position clicked for testing
                Log.i("ITEM_CLICKED", icons.get(position).getTitle());
                // if topic is equal to category then on click will take to wordPage intent
                if(topic.equals("Categories")) {
                    Intent nextActivity = new Intent(getActivity(), WordPage.class);
                    //nextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    nextActivity.putExtra("category", icons.get(position).getTitle());
                    startActivity(nextActivity);
                }else {
                    // sets category section to topic
                    // if want to see word clicked then have it icons.get(poistion).getTitle()
                    TextView label = (TextView) getActivity().findViewById(R.id.txtCategory);
                    label.setText(topic);
                    Boolean notRepeatedImage = true;
                    if (!iconBarVisible){
                        LinearLayout linLayoutIconBar = (LinearLayout) getActivity().findViewById(R.id.iconBar);
                        ViewGroup.LayoutParams params = linLayoutIconBar.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        iconBarVisible = true;
                    }

                    switch (howManyInBar) {
                        case 0:
                            titleOne = (TextView) getActivity().findViewById(R.id.txtTitleOne);
                            imgOne = (ImageView) getActivity().findViewById(R.id.imgIconOne);
                            iconOne = (LinearLayout) getActivity().findViewById(R.id.iconOne);
                            iconOne.setVisibility(View.VISIBLE);
                            howManyInBar++;
                            iconBar.add(icons.get(position));
                            titleOne.setText(icons.get(position).getTitle());
                            if(icons.get(position).getIcon() < 0 ) {
                                imgOne.setImageBitmap(icons.get(position).getBitmap());
                            }else {
                                imgOne.setImageResource(icons.get(position).getIcon());
                            }
                            break;
                        case 1:
                            for(int i = 0; i < iconBar.size(); i++){
                                if(iconBar.get(i).getTitle().toLowerCase().equals(icons.get(position).getTitle().toLowerCase())){
                                    notRepeatedImage = false;
                                }
                            }
                            if(notRepeatedImage) {
                                titleTwo = (TextView) getActivity().findViewById(R.id.txtTitleTwo);
                                imgTwo = (ImageView) getActivity().findViewById(R.id.imgIconTwo);
                                iconTwo = (LinearLayout) getActivity().findViewById(R.id.iconTwo);
                                iconTwo.setVisibility(View.VISIBLE);
                                howManyInBar++;
                                iconBar.add(icons.get(position));
                                titleTwo.setText(icons.get(position).getTitle());
                                if (icons.get(position).getIcon() < 0) {
                                    imgTwo.setImageBitmap(icons.get(position).getBitmap());
                                } else {
                                    imgTwo.setImageResource(icons.get(position).getIcon());
                                }
                                break;
                            }
                        case 2:
                            for(int i = 0; i < iconBar.size(); i++){
                                if(iconBar.get(i).getTitle().toLowerCase().equals(icons.get(position).getTitle().toLowerCase())){
                                    notRepeatedImage = false;
                                }
                            }
                            if(notRepeatedImage) {
                                titleThree = (TextView) getActivity().findViewById(R.id.txtTitleThree);
                                imgThree = (ImageView) getActivity().findViewById(R.id.imgIconThree);
                                iconThree = (LinearLayout) getActivity().findViewById(R.id.iconThree);
                                iconThree.setVisibility(View.VISIBLE);
                                howManyInBar++;
                                iconBar.add(icons.get(position));
                                titleThree.setText(icons.get(position).getTitle());
                                if (icons.get(position).getIcon() < 0) {
                                    imgThree.setImageBitmap(icons.get(position).getBitmap());
                                } else {
                                    imgThree.setImageResource(icons.get(position).getIcon());
                                }
                                break;
                            }
                        case 3:
                        case 4:
                            for(int i = 0; i < iconBar.size(); i++){
                                if(iconBar.get(i).getTitle().toLowerCase().equals(icons.get(position).getTitle().toLowerCase())){
                                    notRepeatedImage = false;
                                }
                            }
                            if(notRepeatedImage) {
                                titleFour = (TextView) getActivity().findViewById(R.id.txtTitleFour);
                                imgFour = (ImageView) getActivity().findViewById(R.id.imgIconFour);
                                iconFour = (LinearLayout) getActivity().findViewById(R.id.iconFour);
                                iconFour.setVisibility(View.VISIBLE);
                                if (howManyInBar == 3) {
                                    howManyInBar++;
                                } else {
                                    iconBar.remove(3);
                                }
                                iconBar.add(icons.get(position));
                                titleFour.setText(icons.get(position).getTitle());
                                if (icons.get(position).getIcon() < 0) {
                                    imgFour.setImageBitmap(icons.get(position).getBitmap());
                                } else {
                                    imgFour.setImageResource(icons.get(position).getIcon());
                                }
                                break;
                            }
                    }
                }


            }
        });

        // Is setting up the onClick from when the checkMark is clicked
        LinearLayout checkMark = (LinearLayout) gridFragmentWithBar.findViewById(R.id.checkMark);
        checkMark.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Checkmark", "Was clicked");
            }
        });

        // finding the imageviews for the X's in the corners of the icons within the iconBar
        ImageView removeIconOne = (ImageView) gridFragmentWithBar.findViewById(R.id.btnRemoveOne);
        ImageView removeIconTwo = (ImageView) gridFragmentWithBar.findViewById(R.id.btnRemoveTwo);
        ImageView removeIconThree = (ImageView) gridFragmentWithBar.findViewById(R.id.btnRemoveThree);
        ImageView removeIconFour = (ImageView) gridFragmentWithBar.findViewById(R.id.btnRemoveFour);
        // setting the onClick listener for all of the X's
        removeIconOne.setOnClickListener(removeIcon);
        removeIconTwo.setOnClickListener(removeIcon);
        removeIconThree.setOnClickListener(removeIcon);
        removeIconFour.setOnClickListener(removeIcon);


        return gridFragmentWithBar;
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
                        /*case 0:
                            break;*/
                        case 1:
                            iconOne.setVisibility(View.INVISIBLE);
                            LinearLayout linLayoutIconBar = (LinearLayout) getActivity().findViewById(R.id.iconBar);
                            ViewGroup.LayoutParams params = linLayoutIconBar.getLayoutParams();
                            params.height = 0;
                            linLayoutIconBar.setLayoutParams(params);
                            iconBarVisible = false;
                            break;
                        case 2:
                            titleOne.setText(titleTwo.getText());
                            if(iconBar.get(1).getIcon() < 0){
                                imgOne.setImageBitmap(iconBar.get(1).getBitmap());
                            }else {
                                imgOne.setImageResource(iconBar.get(1).getIcon());
                            }
                            //imgOne.setId(imgTwo.getId());
                            iconTwo.setVisibility(View.INVISIBLE);
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
                            iconThree.setVisibility(View.INVISIBLE);
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
                            iconFour.setVisibility(View.INVISIBLE);
                            break;
                    }
                    howManyInBar--;
                    iconBar.remove(0);
                    Log.i("Remove", "Icon One");
                    break;
                case R.id.btnRemoveTwo:
                    switch (howManyInBar) {
                        case 2:
                            iconTwo.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            titleTwo.setText(titleThree.getText());
                            if(iconBar.get(2).getIcon() < 0){
                                imgTwo.setImageBitmap(iconBar.get(2).getBitmap());
                            }else {
                                imgTwo.setImageResource(iconBar.get(2).getIcon());
                            }
                            iconThree.setVisibility(View.INVISIBLE);
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
                            iconFour.setVisibility(View.INVISIBLE);
                            break;
                    }
                    howManyInBar--;
                    iconBar.remove(1);
                    Log.i("Remove", "Icon Two");
                    break;
                case R.id.btnRemoveThree:
                    switch (howManyInBar) {
                        case 3:
                            iconThree.setVisibility(View.INVISIBLE);
                            break;
                        case 4:
                            titleThree.setText(titleFour.getText());
                            if(iconBar.get(3).getIcon() < 0){
                                imgThree.setImageBitmap(iconBar.get(3).getBitmap());
                            }else {
                                imgThree.setImageResource(iconBar.get(3).getIcon());
                            }
                            iconFour.setVisibility(View.INVISIBLE);
                            break;
                    }
                    howManyInBar--;
                    iconBar.remove(2);
                    Log.i("Remove", "Icon Three");
                    break;
                case R.id.btnRemoveFour:
                    howManyInBar--;
                    iconBar.remove(3);
                    iconFour.setVisibility(View.INVISIBLE);
                    Log.i("Remove", "Icon Four");
                    break;
            }
        }
    };

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
