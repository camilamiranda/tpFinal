package org.miranda.projet;

import org.miranda.Item;

public class CurrentItem {

    public static Item item = null;
    public static void setCurrentItem(Item i){
        item = i;
    }
}
