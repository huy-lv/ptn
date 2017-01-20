package com.noah.photonext.custom;

import com.noah.photonext.adapter.Photo;

import java.util.ArrayList;

/**
 * Created by HuyLV-CT on 05-Dec-16.
 */

public class PhotoList extends ArrayList<Photo> {
    public PhotoList(){
        super();
    }
    public int realSize(){
        int i=0;
        for(Photo p : this){
            if(p.sdcardPath != null){
                i++;
            }
        }
        return i;
    }
}
