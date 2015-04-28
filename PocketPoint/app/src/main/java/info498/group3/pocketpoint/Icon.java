package info498.group3.pocketpoint;


import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Comparator;


// Is the icons that store title and images in either bitmap or drawable
public class Icon implements Serializable{
    private int icon;
    private String title;
    private Bitmap bitmap;

    // stores the icon value and title
    public Icon(int icon, String title) {
        // icon is -1 for bitmap
        // icon is the resID for drawables
        super();
        this.icon = icon;
        this.title = title;
    }



    // -1 means is a bitmap and not a drawable resID
    public int getIcon(){
        return icon;
    }

    public String getTitle(){
        return title;
    }

    public void setBitmap(Bitmap newbitmap) {bitmap = newbitmap;}

    // returns the bitmap for interpretation
    public Bitmap getBitmap (){return bitmap;}

}

class  IconComparator implements Comparator<Icon> {

    @Override
    public int compare(Icon iconOne, Icon iconTwo) {
        /*if (iconOne.getTitle().equals(iconTwo.getTitle()) && iconOne.getIcon() < 0){
            return 1;
        }else if(iconOne.getTitle().equals(iconTwo.getTitle()) && iconOne.getIcon() < 0) {
            return -1;
        }else {
            return iconOne.getTitle().compareToIgnoreCase(iconTwo.getTitle());
        }*/
        if (iconOne.getTitle().equalsIgnoreCase(iconTwo.getTitle())  && iconOne.getTitle().compareTo(iconTwo.getTitle()) != 0) {
            return iconOne.getTitle().compareTo(iconTwo.getTitle());
        }else if(iconOne.getTitle().equalsIgnoreCase(iconTwo.getTitle()) && iconOne.getIcon() < 0){
            return 1;
        }else if(iconOne.getTitle().equals(iconTwo.getTitle()) && iconOne.getIcon() < 0) {
            return -1;
        }else {
            return iconOne.getTitle().compareToIgnoreCase(iconTwo.getTitle());
        }
    }
}
