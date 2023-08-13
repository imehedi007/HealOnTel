package com.example.healontel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.annotations.Nullable;

import pl.droidsonroids.gif.GifImageView;

public class LoadingDialog {
    private Dialog progressDialog;
    GifImageView imageViewLoading;

    public LoadingDialog(Context context) {
        progressDialog = new Dialog(context);
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.setCancelable(false);
    }

//    public void show() {
//        //ImageView imageView = progressDialog.findViewById(R.id.imageViewLoading);
//
//        Glide.with(imageView)
//                .asGif()
//                .load(R.raw.hloading)
//                .into(new CustomTarget<GifDrawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
//                        imageView.setImageDrawable(resource);
//                        resource.start();
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                        // Empty method
//                    }
//                });
//
//        progressDialog.show();
   // }


    public void dismiss() {
        progressDialog.dismiss();
    }
}


