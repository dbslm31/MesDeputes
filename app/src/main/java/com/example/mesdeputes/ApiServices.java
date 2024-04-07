package com.example.mesdeputes;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class ApiServices {

    private static String URL_API_SEARCH="https://www.nosdeputes.fr/recherche/";
    private static String URL_END_FORMATJSON= "?format=json";
    private static String URL_AVATAR="https://www.nosdeputes.fr/depute/photo/";

    public static void searchRequest(Context context, String search, SearchObserver listener){
        Log.i("TagInfos","searchRequest is running");
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(URL_API_SEARCH + search + URL_END_FORMATJSON,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("c", response);
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            JSONArray jsonArray= jsonObject.getJSONArray("results");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject object= jsonArray.getJSONObject(i);
                                if(object.getString("document_type").equals("Parlementaire")){
                                    getDeputyInfo(context, object.getString("document_url"), listener);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("searchRequestError", "Error during search request: " + error.getMessage());
                error.printStackTrace();
            }
        });
        queue.add(request);
    }


    public static void getDeputyInfo(Context context, String urlInfoDeputy, SearchObserver listener){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(urlInfoDeputy,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getDeputyInfoResponse", response);
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            JSONObject jsonObjectDeputy= jsonObject.getJSONObject("depute");
                            Deputy deputy= new Deputy(jsonObjectDeputy.getInt("id"),
                                    jsonObjectDeputy.getString("prenom"),
                                    jsonObjectDeputy.getString("nom_de_famille"),
                                    jsonObjectDeputy.getString("num_deptmt"),
                                    jsonObjectDeputy.getInt("num_circo"),
                                    jsonObjectDeputy.getString("nom_circo"));

                            deputy.setGroupe(jsonObjectDeputy.getString("groupe_sigle"));
                            deputy.setSite(jsonObjectDeputy.getJSONArray("sites_web")
                                    .getJSONObject(0).getString("site"));
                            deputy.setEmail(jsonObjectDeputy.getJSONArray("emails")
                                    .getJSONObject(0).getString("email"));

                            //Fetch Responsabilities array
                            JSONArray responsabilitiesArray = jsonObjectDeputy.getJSONArray("responsabilites");
                            for (int i = 0; i < responsabilitiesArray.length(); i++) {
                                JSONObject responsabilityObject = responsabilitiesArray.getJSONObject(i).getJSONObject("responsabilite");
                                Responsability responsability = new Responsability(
                                        responsabilityObject.getString("organisme"),
                                        responsabilityObject.getString("fonction"),
                                        responsabilityObject.getString("debut_fonction")
                                );
                                deputy.addResponsability(responsability);
                            }

                            listener.onReceiveDeputyInfo(deputy);

                            getDeputyVotes(context, deputy);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }


    public static void loadDeputyAvatar (Context context, String deputyName, final ImageView imageView){
        RequestQueue queue = Volley.newRequestQueue(context);
        ImageRequest request = new ImageRequest( URL_AVATAR+deputyName+"/160",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                });
        queue.add(request);
    }

    //Fetching votes of each deputies
    public static void getDeputyVotes(Context context, Deputy deputy) {
        Log.i("TafInfos", "Getvote is running");
        RequestQueue queue = Volley.newRequestQueue(context);
        String deputySlug = removeAccents(deputy.getFirstname().toLowerCase() + "-" + deputy.getLastname().toLowerCase());
        String URL_DEPUTY_VOTES = "https://www.nosdeputes.fr/" + deputySlug + "/votes/json";
        Log.v("URL_DEPUTY_VOTES", URL_DEPUTY_VOTES);

        StringRequest request = new StringRequest(Request.Method.GET, URL_DEPUTY_VOTES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getDeputyVotes response", response);
                        try {
                            Log.i("TagInfos", "In try");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray votesArray = jsonObject.getJSONArray("votes");
                            Log.d("votesArray", votesArray.toString());

                            //Limit the fetching to 3
                            int limit = Math.min(votesArray.length(), 3);

                            for (int i = 0; i < limit; i++) {
                                JSONObject voteItem = votesArray.getJSONObject(i);
                                JSONObject voteObject = voteItem.getJSONObject("vote");
                                JSONObject scrutinObject = voteObject.getJSONObject("scrutin");
                                Log.d("voteObject", voteObject.toString());
                                Log.d("scrutinObject", scrutinObject.toString());
                                Vote vote = new Vote(
                                        scrutinObject.getString("titre"),
                                        voteObject.getString("position")
                                );


                                deputy.addVote(vote);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }


    //Method to remove accent in order to avoid errors in URL
    private static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }


}
