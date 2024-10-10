package com.example.sacarolha.util.handlers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageHandler {

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public static Uri saveBitmapToFile(Context context, Bitmap bitmap) throws IOException {

        File cachePath = new File(context.getCacheDir(), "images");
        cachePath.mkdirs();
        File file = new File(cachePath, "image.png");
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();

        return FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
    }

    public static void shareImage(Context context, Uri imageUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);

        // Allow the user to choose any app that can handle the intent
        Intent chooser = Intent.createChooser(intent, "Share Image");
        context.startActivity(chooser);
    }

}
