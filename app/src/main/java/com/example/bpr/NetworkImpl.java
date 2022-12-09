package com.example.bpr;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bpr.Objects.CoopProducts;
import com.example.bpr.Objects.CoopStoreCore;
import com.example.bpr.Objects.Location;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkImpl {
    CoopStoreCore main = new CoopStoreCore();
    private String coopKey = "b83aac68e13b46b2afc5925fd101b5f2";




    public ArrayList<CoopProducts> getCoopProducts(String store, int kardex, Location location,final VolleyCallBack callBack) {
        //gets all products for a specific store
        String URL = "https://api.cl.coop.dk/productapi/v1/product/"+kardex;
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        ArrayList<CoopProducts> coopProductsList = new ArrayList<CoopProducts>();

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {


                for(int i = 0; i < response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Gson gson = new Gson();
                        CoopProducts coopProducts = gson.fromJson(jsonObject.toString(),CoopProducts.class);
                        coopProducts.store=store;
                        coopProducts.kardex=kardex;
                        coopProducts.latitude = location.coordinates.get(0);
                        coopProducts.longitude = location.coordinates.get(1);
                        coopProductsList.add(coopProducts);
                        //Log.e("Rest Respone", coopProductsList.get(i).navn);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callBack.onSuccessProducts(coopProductsList);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest Error", error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Ocp-Apim-Subscription-Key", coopKey);


                return params;

            }
        };
        requestQueue.add(objectRequest);

        Log.e("Rest Respone", coopProductsList.size() + "");
        return coopProductsList;
    }
    public CoopStoreCore getCoopStores(double latitude,double longtitude,int radius, final VolleyCallBackStores callback) {
        //returns all stores in a vicinity of the user based on their gps coordinates and how big of a radius they have decided
        String URL = "https://api.cl.coop.dk/storeapi/v1/stores/find/radius/"+radius+"?latitude="+latitude+"&longitude="+longtitude;
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);

       CoopStoreCore core = new CoopStoreCore();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Rest Respone", response.toString());
                try {
                    Gson gson = new Gson();
                    CoopStoreCore root = gson.fromJson(response.toString(), CoopStoreCore.class);
                    main = root;
                    callback.onSuccesStores(main.data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest Error", error.toString());
                main = core;
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Ocp-Apim-Subscription-Key", coopKey);


                return params;

            }
        };
        requestQueue.add(objectRequest);
        return main;
    }
    public ArrayList<CoopProducts> getStandardCoopProducts() {
        //gets only the standard products for a specific store
        String URL = "https://api.cl.coop.dk/assortmentapi/v1/product/24181";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);
        ArrayList<CoopProducts> coopProductsList = new ArrayList<CoopProducts>();
        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Rest Respone", response.toString());
                for(int i = 0; i < response.length();i++)
                {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Gson gson = new Gson();
                        CoopProducts coopProducts = gson.fromJson(jsonObject.toString(),CoopProducts.class);
                        coopProductsList.add(coopProducts);
                        Log.e("Rest Respone", coopProducts.navn);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest Error", error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Ocp-Apim-Subscription-Key", coopKey);


                return params;

            }
        };
        requestQueue.add(objectRequest);
        return coopProductsList;
    }
}
