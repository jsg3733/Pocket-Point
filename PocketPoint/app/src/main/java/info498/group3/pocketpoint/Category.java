package info498.group3.pocketpoint;

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

}
