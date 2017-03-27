package com.noah.photonext.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.noah.photonext.R;
import com.noah.photonext.activity.AdjustmentActivity;
import com.noah.photonext.activity.AutoContrastActivity;
import com.noah.photonext.activity.AutoFixActivity;
import com.noah.photonext.activity.BlurActivity;
import com.noah.photonext.activity.CropActivity;
import com.noah.photonext.activity.DoubleActivity;
import com.noah.photonext.activity.RotateActivity;
import com.noah.photonext.custom.PhotoList;
import com.noah.photonext.model.AdjustmentToolObject;
import com.noah.photonext.model.DoubleThumbnailObject;
import com.noah.photonext.model.EditToolObject;
import com.noah.photonext.model.EffectToolObject;
import com.noah.photonext.model.LayoutObject;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by HuyLV-CT on 03-Nov-16.
 */

public class Utils {
    public static final String INTENT_KEY_PICK_ONE = "INTENT_KEY_PICK_ONE";
    public static final String INTENT_KEY_PICK_POS = "INTENT_KEY_PICK_POS";
    public static final int REQUEST_CODE_PICK_ONE = 101;
    public static final String INTENT_KEY_IMAGE_PATH = "INTENT_KEY_IMAGE_PATH";
    public static final String INTENT_KEY_PICK_ONE_EDIT = "INTENT_KEY_PICK_ONE_EDIT";
    public static final int REQUEST_CODE_CROP = 102;
    public static final int REQUEST_CODE_ROTATE = 103;
    public static final int REQUEST_CODE_AUTOFIX = 105;
    public static final int REQUEST_CODE_ADJUSTMENT = 104;

    public static final int BRIGHTNESS_ALPHA = 1;
    public static final int REQUEST_CODE_BLUR = 106;
    public static final int REQUEST_CODE_AUTOCONTRAST = 107;
    public static final int REQUEST_CODE_PICK_DOUBLE = 108;
    public static final String INTENT_KEY_DOUBLE_PATH = "INTENT_KEY_DOUBLE_PATH";
    public static final String INTENT_KEY_PICK_DOUBLE = "INTENT_KEY_PICK_DOUBLE";
    public static final String BUNDLE_FRAGMENT_KEY = "BUNDLE_FRAGMENT_KEY";
    public static final String INTENT_KEY_EFFECT_GROUP = "INTENT_KEY_EFFECT_GROUP";
    public static final String CAMERA_PICTURE_PREFIX = "pic";
    public static final int CAMERA_TAKE_A_PICTURE = 1001;
    public static final int DOUBLE_NORMAL = 0, DOUBLE_DARKEN = 1, DOUBLE_MULTIPLY = 2, DOUBLE_LIGHTEN = 3, DOUBLE_SCREEN = 4;
    private static final String APP_CACHE_FOLDER = "cacheFolder";
    public static int numOfPhoto = 0;
    public static PhotoList currentPhotos = new PhotoList();
    public static String PREPATH = "file://";
    public static int canvasWidth, canvasHeight;
    public static int frameWidth, frameHeight, originCollageWidth, originCollageHeidht, currentCollageWidth, currentCollageHeight;
    public static String FolderName = "Pnext";
    public static Bitmap currentBitmap;
    public static ArrayList<Bitmap> historyBitmaps = new ArrayList<>();
    public static int currentHistoryPos = 0;
    public static Xfermode[] xfermodes = {
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };


//    public static ArrayList<Integer> origW;
//    public static ArrayList<Integer> origH;
//    public static ArrayList<Integer> actH;

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap getBlurBackground() {
        Bitmap bm = historyBitmaps.get(currentHistoryPos);
        Bitmap dst = bm.copy(bm.getConfig(), true);
        Mat m = new Mat();
        org.opencv.android.Utils.bitmapToMat(bm, m);
        Imgproc.GaussianBlur(m, m, new Size(91, 91), 0);
        org.opencv.android.Utils.matToBitmap(m, dst);
        return dst;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(String.class);
                Object exifInstance = exifConstructor.newInstance(src);
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", String.class, int.class);
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, tagOrientation, 1);
            }
        } catch (ClassNotFoundException | SecurityException | NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return orientation;
    }

    public static Bitmap fixRotateBitmap(String src) {
        Bitmap bitmap = BitmapFactory.decodeFile(src);
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static void showAlertDialog(final Activity context, String title, String message, DialogInterface.OnClickListener onPositive) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, onPositive)
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();


        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static void createAlertDialog(Context c, String message) {
        AlertDialog.Builder b = new AlertDialog.Builder(c);
        b.setMessage(message);
        b.create().show();
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = Math.round(dpWidth / 120);
        return noOfColumns;
    }

    public static ArrayList<LayoutObject> createListt() {
        ArrayList<LayoutObject> layoutList = new ArrayList<>();
        layoutList.add(new LayoutObject(R.mipmap.f2s1, 2, 1, true));
        layoutList.add(new LayoutObject(R.mipmap.f2s2, 2, 2, true));
        layoutList.add(new LayoutObject(R.mipmap.f2s3, 2, 3, true));
        layoutList.add(new LayoutObject(R.mipmap.f2s4, 2, 4, true));
        layoutList.add(new LayoutObject(R.mipmap.f2s5, 2, 5, true));
        layoutList.add(new LayoutObject(R.mipmap.f3s1, 3, 1, true));
        layoutList.add(new LayoutObject(R.mipmap.f3s2, 3, 2, true));
        layoutList.add(new LayoutObject(R.mipmap.f3s3, 3, 3, true));
        layoutList.add(new LayoutObject(R.mipmap.f3s4, 3, 4, true));
        layoutList.add(new LayoutObject(R.mipmap.f4s1, 4, 1, true));
        layoutList.add(new LayoutObject(R.mipmap.f4s2, 4, 2, true));
        layoutList.add(new LayoutObject(R.mipmap.f4s3, 4, 3, true));
        layoutList.add(new LayoutObject(R.mipmap.f4s4, 4, 4, true));
        layoutList.add(new LayoutObject(R.mipmap.f4s5, 4, 5, true));
        layoutList.add(new LayoutObject(R.mipmap.f4s6, 4, 6, true));
        layoutList.add(new LayoutObject(R.mipmap.f4s7, 4, 7, false));
        return layoutList;
    }

    public static ArrayList<EditToolObject> createEditToolList(Resources r) {
        ArrayList<EditToolObject> editToolObjects = new ArrayList<>();
        editToolObjects.add(new EditToolObject(R.mipmap.ic_tools_crop, r.getString(R.string.crop), CropActivity.class));
        editToolObjects.add(new EditToolObject(R.mipmap.ic_tools_rotate, r.getString(R.string.rotate), RotateActivity.class));
        editToolObjects.add(new EditToolObject(R.mipmap.ic_tools_double, r.getString(R.string.doublee), DoubleActivity.class));
        editToolObjects.add(new EditToolObject(R.mipmap.ic_tools_adjustment, r.getString(R.string.adjustment), AdjustmentActivity.class));
        editToolObjects.add(new EditToolObject(R.mipmap.ic_tools_autofix, r.getString(R.string.autofix), AutoFixActivity.class));
        editToolObjects.add(new EditToolObject(R.mipmap.ic_tools_autocontrast, r.getString(R.string.auto_contrast), AutoContrastActivity.class));
        editToolObjects.add(new EditToolObject(R.mipmap.ic_tools_blur, r.getString(R.string.blur), BlurActivity.class));
        return editToolObjects;
    }

    public static ArrayList<AdjustmentToolObject> createAdjustmentToolList(Resources r) {
        ArrayList<AdjustmentToolObject> e = new ArrayList<>();
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_exposure, R.mipmap.ic_tools_adjustment_exposure_ac, r.getString(R.string.adjustment_exposure)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_temperature, R.mipmap.ic_tools_adjustment_temperature_ac, r.getString(R.string.adjustment_temperature)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_contrast, R.mipmap.ic_tools_adjustment_contrast_ac, r.getString(R.string.adjustment_contrast)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_brightness, R.mipmap.ic_tools_adjustment_brightness_ac, r.getString(R.string.adjustment_brightness)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_vibrance, R.mipmap.ic_tools_adjustment_vibrance_ac, r.getString(R.string.adjustment_vibrance)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_highlights, R.mipmap.ic_tools_adjustment_highlights_ac, r.getString(R.string.adjustment_highlights)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_shadows, R.mipmap.ic_tools_adjustment_shadows_ac, r.getString(R.string.adjustment_shadows)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_saturation, R.mipmap.ic_tools_adjustment_saturation_ac, r.getString(R.string.adjustment_saturation)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_lightness, R.mipmap.ic_tools_adjustment_lightness_ac, r.getString(R.string.adjustment_lightness)));
        e.add(new AdjustmentToolObject(R.mipmap.ic_tools_adjustment_hue, R.mipmap.ic_tools_adjustment_hue_ac, r.getString(R.string.adjustment_hue)));
        return e;
    }

    public static ArrayList<DoubleThumbnailObject> createDoubleList(Resources r) {
        ArrayList<DoubleThumbnailObject> e = new ArrayList<>();
        e.add(new DoubleThumbnailObject(null, r.getString(R.string.double_normal)));
        e.add(new DoubleThumbnailObject(null, r.getString(R.string.double_darken)));
        e.add(new DoubleThumbnailObject(null, r.getString(R.string.double_multiply)));
        return e;
    }

    public static ArrayList<EffectToolObject> createEffectToolList(Resources r) {
        ArrayList<EffectToolObject> e = new ArrayList<>();

        return e;
    }

    public static ArrayList<EffectToolObject> createEffectGroupList(Resources r) {
        ArrayList<EffectToolObject> e = new ArrayList<>();
        e.add(new EffectToolObject("Sepia"));
        return e;
    }

    public static File getCacheDirectory() {
        File file = new File(Environment.getExternalStorageDirectory(),
                APP_CACHE_FOLDER);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("cxz", "CACHE FOLDER not create");
            }
        }

        return file;
    }

    public static File createImageFile(String prefix, File dir, String extension) {
        String id = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        if (isExternalStorageWritable()) {
            try {
                File file = new File(dir, prefix + id + extension);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                return file;
            } catch (IOException ex) {
                Log.e("cxz", "error 3");
            }
        }
        return null;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static void showT(Context c, String me) {
        Toast.makeText(c, me, Toast.LENGTH_SHORT).show();
    }
}
