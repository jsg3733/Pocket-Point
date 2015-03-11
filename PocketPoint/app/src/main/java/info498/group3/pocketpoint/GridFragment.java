package info498.group3.pocketpoint;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GridFragment extends Fragment {


    private GridView gridView;
    private List<String> names;
    //private final List<String> foods = new ArrayList<>(Arrays.asList("Apple", "Banana", "Bread", "Cake", "Cheese", "Cracker",
            //"Egg", "Juice", "Milk", "Pizza", "Stix", "Water"));

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View gridFragment = inflater.inflate(R.layout.gridview_fragment, container, false);

        Bundle info = this.getArguments();
        String topic = info.getString("category");
        Log.i("FragmentLoad", topic);

        names = new ArrayList<>();


        try{
            String file = topic.toLowerCase() + ".txt";
            AssetManager am = gridFragment.getContext().getAssets();
            InputStream is = am.open(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();

            while(line != null) {
                names.add(line);
                line = br.readLine();

            }
        }catch (IOException e) {
            Log.i("GridFragment", "File is not being found");
        }







        final List<Icon> icons = new ArrayList<>();

        for(int i=0; i < names.size(); i++) {
            //String hello = "hello";
            //icons.add(new Icon(R.drawable.ic_launcher, items[i]));  // array version
            //icons.add(new Icon(R.drawable.ic_launcher, words.get(i))); //arraylist version for categories
            //if(foods.get(i).equals("Apple")) {

            String name = names.get(i).replaceAll("\\s+","").toLowerCase();
            int resID = getResources().getIdentifier(topic.toLowerCase() + "_" + name, "drawable", getActivity().getPackageName());
            icons.add(new Icon(resID, names.get(i)));
            //icons.add(new Icon(R.drawable.food_apple, foods.get(i))); //If know the exact name
            //}else {
            //icons.add(new Icon(R.drawable.ic_launcher, foods.get(i)));  //generic image
            //}
        }
        /*for(int i=0; i < 3; i++) {
            icons.add(new Icon(R.drawable.ic_launcher, "hello"));
        }*/

        gridView = (GridView) gridFragment.findViewById(R.id.myGridView);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(gridFragment.getContext(), R.layout.gridview_cell, icons);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ITEM_CLICKED", icons.get(position).getTitle());
                Intent nextActivity = new Intent(getActivity(), WordPage.class);
                //nextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                nextActivity.putExtra("category", icons.get(position).getTitle());
                startActivity(nextActivity);

            }
        });



        return gridFragment;
    }
}
