package com.invio.usgchallenge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;


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

public class MainActivity extends AppCompatActivity {

    private RecyclerView horizontalList;
    private RecyclerView verticalList;
    private AVLoadingIndicatorView loading;
    private ArrayList<String> categoryAreas, categoryNames, categoryThumbLs;
    private regionAdapter myRegionAdapter;
    private categoryAdapter myCategoryAdapter;
    private boolean regionLoadingControl = false;
    private boolean categoryLoadingControl = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        idMatch();// idleri tanıtma

        loading.show();// yükleniyor simgesini sayfam açıldığında gösteriyorum

        horizontalList.setLayoutManager(layoutManager);//yatay bir recyclerview istiyorum.
        verticalList.setNestedScrollingEnabled(false);

        verticalList.setHasFixedSize(false);
        verticalList.setLayoutManager(gridLayoutManager);//üç sütunlu bir recyclerview istiyorum.

        getRegions();
        getCategories();

    }

    public void getCategories() {
        final String url = "https://www.themealdb.com/api/json/v1/1/categories.php";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                categoryNames = new ArrayList<>();
                categoryThumbLs = new ArrayList<>();
                new ArrayList<>();
                try {
                    JSONArray categories = response.getJSONArray("categories");
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject category = categories.getJSONObject(i);


                        String CName = category.getString("strCategory");
                        categoryNames.add(CName);

                        String CThumb = category.getString("strCategoryThumb");
                        categoryThumbLs.add(CThumb);

                    }
                    myCategoryAdapter = new categoryAdapter(MainActivity.this, categoryNames, categoryThumbLs);
                    verticalList.setAdapter(myCategoryAdapter);
                    categoryLoadingControl = true;// burada categoryLoadingControl' a true değerini aktarıyorum
                    // ve alt satırda regionLoadingControl' u sorguluyorum,
                    // şayet o da true ise zaten yüklenme tamamlanmıştır recyclerviewleri gösterebilirim ve
                    // yüklenme simgesini kaldırabilirim anlamına gelir
                    if (regionLoadingControl)
                        load();


                } catch (JSONException e) {
                    Log.e("Exception", e.toString());

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(MainActivity.this).add(objectRequest);
    }

    public void getRegions() {
        final String url = "https://www.themealdb.com/api/json/v1/1/list.php?a=list";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                categoryAreas = new ArrayList<>();
                try {
                    JSONArray meals = response.getJSONArray("meals");
                    for (int i = 0; i < meals.length(); i++) {
                        JSONObject area = meals.getJSONObject(i);
                        String areaName = area.getString("strArea");
                        categoryAreas.add(areaName);
                    }
                    myRegionAdapter = new regionAdapter(MainActivity.this, categoryAreas);
                    horizontalList.setAdapter(myRegionAdapter);

                    regionLoadingControl = true;
                    // burada regionLoadingControl' e true değerini aktarıyorum
                    // ve alt satırda categoryLoadingControl' u sorguluyorum,
                    // şayet o da true ise zaten yüklenme tamamlanmıştır recyclerviewleri gösterebilirim ve
                    // yüklenme simgesini kaldırabilirim anlamına gelir
                    if (categoryLoadingControl)
                        load();


                } catch (JSONException e) {
                    Log.e("Exception", e.toString());

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(MainActivity.this).add(objectRequest);
    }

    public void load() {
        horizontalList.setVisibility(View.VISIBLE);
        verticalList.setVisibility(View.VISIBLE);
        loading.hide();
    }

    private void idMatch() {
        horizontalList = findViewById(R.id.hrzntlRecycler);
        verticalList = findViewById(R.id.vrtclRecycler);
        loading = findViewById(R.id.progressBar);
    }

}