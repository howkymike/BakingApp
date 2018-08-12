package com.howky.mike.bakingapp.utils;

import android.content.ContentValues;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.howky.mike.bakingapp.provider.BakingContract;
import com.howky.mike.bakingapp.provider.BakingProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    private static final String JSON_CAKES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static RequestQueue mRequestQueue;



    /**
     * Saves passed jsonArray to database
     * @param context application context
     * @param jsonArray jsonArray
     */
    private static void processJsonData(Context context, JSONArray jsonArray) {
        JSONObject cake;
        long id;
        String name;
        String ingredients;
        String steps;
        int servings;
        String image;

        ContentValues cv;

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                cake = jsonArray.getJSONObject(i);
                id = cake.getLong("id");
                name = cake.getString("name");
                ingredients = cake.getJSONArray("ingredients").toString();
                steps = cake.getJSONArray("steps").toString();
                servings = cake.getInt("servings");
                image = cake.getString("image");

                cv = new ContentValues();
                cv.put(BakingContract.CakeColumns.CAKE_ID, id);
                cv.put(BakingContract.CakeColumns.TITLE, name);
                cv.put(BakingContract.CakeColumns.INGREDIENTS, ingredients);
                cv.put(BakingContract.CakeColumns.STEPS, steps);
                cv.put(BakingContract.CakeColumns.SERVINGS, servings);
                cv.put(BakingContract.CakeColumns.IMAGE, image);


                context.getContentResolver().insert(BakingProvider.Cakes.CONTENT_URI, cv);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Uses the Volley (https://github.com/google/volley) library to load JSON from URL
     * @param context applicaiton context
     */
    public static void loadUrlData(final Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, JSON_CAKES_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                processJsonData(context, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        mRequestQueue.add(jsonArrayRequest);

    }


    public static String[] getIngredients(String stringIngredients) {

        try {
            JSONArray jsonIngredients = new JSONArray(stringIngredients);
            Double[] quantities = new Double[jsonIngredients.length()];
            String[] measures = new String[jsonIngredients.length()];
            String[] ingredientNames = new String[jsonIngredients.length()];
            String[] ingredientsText = new String[jsonIngredients.length()];

            JSONObject jsonIngredient;
            for (int i = 0; i < jsonIngredients.length(); i++) {
                jsonIngredient = jsonIngredients.getJSONObject(i);
                quantities[i] = jsonIngredient.getDouble("quantity");
                measures[i] = jsonIngredient.getString("measure");
                ingredientNames[i] = jsonIngredient.getString("ingredient");

                ingredientsText[i] = quantities[i] + " " + measures[i] + " "
                        + ingredientNames[i];
            }
            return ingredientsText;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
