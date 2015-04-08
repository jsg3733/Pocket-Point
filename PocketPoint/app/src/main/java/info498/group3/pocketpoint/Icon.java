package info498.group3.pocketpoint;


import android.graphics.Bitmap;


// Is the icons that store title and images in either bitmap or drawable
public class Icon {
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
