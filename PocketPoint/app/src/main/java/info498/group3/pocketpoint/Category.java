package info498.group3.pocketpoint;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * Created by iguest on 5/31/15.
 */
public class Category {

    private Icon category;
    private List<Icon> iconList;

    public Category(Icon category, List<Icon> iconList) {
        super();
        this.category = category;
        this.iconList = iconList;
    }

    public Icon getIcon(int position){
        return iconList.get(position);
    }

    public Icon getCategory(){
        return category;
    }

    public int getIconListSize (){
        return iconList.size();
    }

}

class  CategoryComparator implements Comparator<Category> {

    @Override
    public int compare(Category categoryOne, Category categoryTwo) {
        if (categoryOne.getCategory().getTitle().equalsIgnoreCase(categoryTwo.getCategory().getTitle())  && categoryOne.getCategory().getTitle().compareTo(categoryTwo.getCategory().getTitle()) != 0) {
            return categoryOne.getCategory().getTitle().compareTo(categoryTwo.getCategory().getTitle());
        }else if(categoryOne.getCategory().getTitle().equalsIgnoreCase(categoryTwo.getCategory().getTitle()) && categoryOne.getCategory().getIcon() < 0){
            return 1;
        }else if(categoryOne.getCategory().getTitle().equals(categoryTwo.getCategory().getTitle()) && categoryOne.getCategory().getIcon() < 0) {
            return -1;
        }else {
            return categoryOne.getCategory().getTitle().compareToIgnoreCase(categoryTwo.getCategory().getTitle());
        }
    }
}
