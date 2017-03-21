package com.noah.photonext.model;

/**
 * Created by huylv on 15-Mar-17.
 */

public class EditToolObject {
    public int iconId;
    public String text;
    public Class gotoClass;

    public EditToolObject(int iconId, String text, Class c) {
        this.iconId = iconId;
        this.text = text;
        gotoClass = c;
    }


}
