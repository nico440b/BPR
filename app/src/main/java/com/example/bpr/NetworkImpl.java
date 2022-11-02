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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetworkImpl {
    CoopStoreCore main = new CoopStoreCore();




    public ArrayList<CoopProducts> CoopProductsAPI(final VolleyCallBack callBack) {
        String URL = "https://api.cl.coop.dk/productapi/v1/product/24181";
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
                        coopProductsList.add(coopProducts);
                        //Log.e("Rest Respone", coopProductsList.get(i).navn);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                callBack.onSuccess(coopProductsList);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest Error", error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Ocp-Apim-Subscription-Key", "78ed0c68a5d04b30b5e3e82ad25cbf50");


                return params;

            }
        };
        requestQueue.add(objectRequest);

        Log.e("Rest Respone", coopProductsList.size() + "");
        return coopProductsList;
    }
    public CoopStoreCore CoopStoreAPI() {
        String URL = "https://api.cl.coop.dk/storeapi/v1/stores/find/radius/2000?latitude=55.857543&longitude=9.838736";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.context);

        CoopStoreCore core = new CoopStoreCore();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.e("Rest Respone", response.toString());
                try {
                    Gson gson = new Gson();
                    CoopStoreCore root = gson.fromJson(response.toString(), CoopStoreCore.class);
                    main = root;

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
                params.put("Ocp-Apim-Subscription-Key", "78ed0c68a5d04b30b5e3e82ad25cbf50");


                return params;

            }
        };
        requestQueue.add(objectRequest);
        return main;
    }
    public ArrayList<CoopProducts> CoopAssortmentAPI() {
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
                params.put("Ocp-Apim-Subscription-Key", "78ed0c68a5d04b30b5e3e82ad25cbf50");


                return params;

            }
        };
        requestQueue.add(objectRequest);
        return coopProductsList;
    }
}
