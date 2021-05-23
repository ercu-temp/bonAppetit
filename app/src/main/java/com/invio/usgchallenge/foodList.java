package com.invio.usgchallenge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class foodList extends AppCompatActivity {
    private ArrayList<String> mealNames, mealThumbs, idMeals;
    private foodListAdapter myFoodListAdapter;
    private TextView foodListTitle;
    private AVLoadingIndicatorView foodLoading;
    private RecyclerView foodListRv;
    String textToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        idMatch();
        foodLoading.show();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        foodListRv.setLayoutManager(gridLayoutManager);
        foodListRv.setHasFixedSize(true);


        Intent i = getIntent();
        textToSearch = i.getStringExtra("search");

        foodListTitle.setText(textToSearch.substring(2));
        getData(textToSearch);


    }

    public void getData(String text) {

        boolean internetConnection = new connectionControl().internetConnection(this);
        if (!internetConnection)
            alertInternetConnection();
        else {
            final String url = "yourAPI" + text;
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    mealNames = new ArrayList<>();
                    mealThumbs = new ArrayList<>();
                    idMeals = new ArrayList<>();
                    try {
                        JSONArray categories = response.getJSONArray("meals");
                        for (int i = 0; i < categories.length(); i++) {
                            JSONObject category = categories.getJSONObject(i);

                            String Name = category.getString("strMeal");
                            mealNames.add(Name);

                            String CNameThumb = category.getString("strMealThumb");
                            mealThumbs.add(CNameThumb);

                            String idMeal = category.getString("idMeal");
                            idMeals.add(idMeal);

                        }
                        myFoodListAdapter = new foodListAdapter(foodList.this, mealNames, mealThumbs, idMeals, textToSearch);
                        foodListRv.setAdapter(myFoodListAdapter);
                        foodLoading.hide();

                    } catch (JSONException e) {
                        Log.e("Exception", e.toString());

                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
            Volley.newRequestQueue(foodList.this).add(objectRequest);
        }
    }

    private void idMatch() {
        foodLoading = findViewById(R.id.foodLoading);
        foodListTitle = findViewById(R.id.txtViewCategoryN);
        foodListRv = findViewById(R.id.foodListRecyclerview);
    }

    public void alertInternetConnection() {
        AlertDialog.Builder ad = new AlertDialog.Builder(foodList.this);
        ad.setMessage("Please connect to the internet.");
        ad.setTitle("Network Error");
        ad.setIcon(R.drawable.ic_baseline_error_24);
        ad.setCancelable(false);

        ad.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getData(textToSearch);
            }
        });
        ad.setNegativeButton("Exıt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        ad.create().show();
    }

    public void turnBack(View view) {
        finish();
    }


}