package com.noah.photonext.process;

import android.graphics.Bitmap;

/**
 * Created by huylv on 17-Mar-17.
 */

public interface ITransformation {
    Bitmap perform(Bitmap inp);
}

