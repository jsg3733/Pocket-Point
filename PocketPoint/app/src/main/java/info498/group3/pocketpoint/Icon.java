package info498.group3.pocketpoint;


import android.graphics.Bitmap;

public class Icon {
    private int icon;
    private String title;
    private Bitmap bitmap;

    public Icon(int icon, String title) {
        super();
        this.icon = icon;
        this.title = title;
    }

    public int getIcon(){
        return icon;
    }

    public String getTitle(){
        return title;
    }

    public void setBitmap(Bitmap newbitmap) {bitmap = newbitmap;}

    public Bitmap getBitmap (){return bitmap;}

}
