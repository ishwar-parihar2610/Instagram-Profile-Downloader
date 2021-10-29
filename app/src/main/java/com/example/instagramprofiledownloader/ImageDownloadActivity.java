package com.example.instagramprofiledownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Util.Util;
import com.example.instagramprofiledownloader.databinding.ActivityImageDownloadBinding;

public class ImageDownloadActivity extends AppCompatActivity {
    ActivityImageDownloadBinding binding;
    String url;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_image_download);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.userImage.setVisibility(View.GONE);
        if (getIntent().getStringExtra("url")!=null){
            url=getIntent().getStringExtra("url");
            binding.progressBar.setVisibility(View.GONE);
            binding.userImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(getIntent().getStringExtra("url")).into(binding.userImage);
            userName=getIntent().getStringExtra("user_name");
            binding.userName.setText("Full Name:"+userName);
            binding.downloadBtn.setOnClickListener(v -> {
                if (haveStoragePermission()) {
                    downloadImage(ImageDownloadActivity.this, url);
                }
            });

        }




    }
    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return true;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }
    public void downloadImage(Context context,String imageUrl){
        Util.download(imageUrl,context,"InstaImage.png");

    }




}