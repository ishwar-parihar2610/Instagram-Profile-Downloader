package com.example.instagramprofiledownloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.instagramprofiledownloader.databinding.ActivityMainBinding;
import com.example.instagramprofiledownloader.databinding.ActivityMainBindingImpl;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static String TAG = "MainActivity";
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.buttonView.setOnClickListener(v -> {
            if (validation(binding.urlEdit.getText().toString())) {
                getImageFromUrl(binding.urlEdit.getText().toString());
            } else {
                Toast.makeText(this, "Please Enter Url", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonCopy.setOnClickListener(v->{
            ClipboardManager clipboardManager= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData pasteData=clipboardManager.getPrimaryClip();
            ClipData.Item item=pasteData.getItemAt(0);
            String paste=item.getText().toString();
            binding.urlEdit.setText(paste);

        });
    }


    private boolean validation(String url) {
        if (url.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }








    private void getImageFromUrl(String imageUrl) {
        if (imageUrl.contains("?utm_source=ig_web_copy_link")) {
            String partToRemove = "?utm_source=ig_web_copy_link";
            image = imageUrl.replace(partToRemove, "");
        } else if (imageUrl.contains("?utm_source=ig_web_button_share_sheet")) {
            String partToRemove = "?utm_source=ig_web_button_share_sheet";
            image = imageUrl.replace(partToRemove, "");
        } else if (imageUrl.contains("?utm_medium=share_sheet")) {
            String partToRemove = "?utm_medium=share_sheet";
            image = imageUrl.replace(partToRemove, "");
        } else if (imageUrl.contains("?utm_medium=copy_link")) {
            String partToRemove = "?utm_medium=copy_link";
            image = imageUrl.replace(partToRemove, "");
        } else {
            image = imageUrl;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, image + "?__a=1", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("graphql");
                    JSONObject userObject=jsonObject.getJSONObject("user");
                    String ImageUrl = userObject.getString("profile_pic_url_hd");


                    if (!imageUrl.isEmpty()){
                        String fullName=userObject.getString("full_name");
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,ImageDownloadActivity.class);
                        intent.putExtra("url",ImageUrl);
                        intent.putExtra("user_name",fullName);
                        startActivity(intent);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
                Toast.makeText(MainActivity.this, "Failed:"+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}