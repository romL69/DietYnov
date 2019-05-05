package com.ynov.dietynov;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReceipesActivity extends ListActivity {

    private ArrayList<Recipe> listRecipe;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipes_activity);
        listRecipe = new ArrayList();
        adapter = new RecipeAdapter(this, listRecipe);
        GetRecipe task= new GetRecipe();
        task.execute();
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        Recipe selectedRecipe = listRecipe.get(position);


        Intent myIntent = new Intent(ReceipesActivity.this, RecipeDetailActivity.class);
        myIntent.putExtra("recipe", selectedRecipe);
        ReceipesActivity.this.startActivity(myIntent);
    }


        public class GetRecipe extends AsyncTask<Void, Void, Void> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpHandler sh = new HttpHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCall("http://dev.audreybron.fr/flux/flux_recettes.json");


                if (jsonStr != null) {
                    try {
                        JSONObject jjson = new JSONObject(jsonStr);
                        JSONArray jsonObj = jjson.getJSONArray("result");

                        for (int i = 0; i < jsonObj.length(); i++) {
                            JSONObject c = jsonObj.getJSONObject(i);

                            String title = c.getString("title");
                            int portion = c.getInt("portions");
                            String picture = c.getString("picture_url");

                            JSONObject jsontime = c.getJSONObject("time");
                            Time time = new Time();
                            time.setBaking(jsontime.getInt("baking"));
                            time.setPrep(jsontime.getInt("prep"));
                            time.setTotal(jsontime.getInt("total"));

                            JSONObject jsonnutrition = c.getJSONObject("nutrition");
                            Nutrition nutrition = new Nutrition();
                            nutrition.setKcal(Float.parseFloat(jsonnutrition.getString("kcal")));
                            nutrition.setProtein(Float.parseFloat(jsonnutrition.getString("protein")));
                            nutrition.setFat(Float.parseFloat(jsonnutrition.getString("fat")));
                            nutrition.setCarbohydrate(Float.parseFloat(jsonnutrition.getString("carbohydrate")));
                            nutrition.setSugar(Float.parseFloat(jsonnutrition.getString("sugar")));
                            nutrition.setSat_fat(Float.parseFloat(jsonnutrition.getString("sat_fat")));
                            nutrition.setFiber(Float.parseFloat(jsonnutrition.getString("fiber")));
                            nutrition.setSodium(Float.parseFloat(jsonnutrition.getString("sodium")));

                            JSONArray jsonArrayStep = new JSONArray(c.getString("steps"));
                            ArrayList<Step> StepsAL = new ArrayList<>();
                            for (int j = 0 ; j < jsonArrayStep.length() ; j++) {
                                Step step= new Step();
                                JSONObject jsonstep = jsonArrayStep.getJSONObject(j);
                                step.setStep(jsonstep.getString("step"));
                                step.setOrder(jsonstep.getInt("order"));
                                StepsAL.add(step);
                            }

                            JSONArray jsonArrayIngredient = new JSONArray(c.getString("ingredients"));

                            ArrayList<Ingredient> IngredientAL = new ArrayList<>();
                            for (int j = 0 ; j < jsonArrayIngredient.length() ; j++) {
                                Ingredient ingredient= new Ingredient();
                                JSONObject jsonIngredient = jsonArrayIngredient.getJSONObject(j);
                                ingredient.setQuantity(jsonIngredient.getInt("quantity"));
                                ingredient.setUnit(jsonIngredient.getString("unit"));
                                ingredient.setName(jsonIngredient.getString("name"));
                                IngredientAL.add(ingredient);
                            }

                            Recipe recipe = new Recipe();
                            recipe.setTitle(title);
                            recipe.setPortion(portion);
                            recipe.setPicture(picture);
                            recipe.setTime(time);
                            recipe.setStep(StepsAL);
                            recipe.setIngredient(IngredientAL);
                            recipe.setNutrition(nutrition);




                            // adding contact to contact list
                            listRecipe.add(recipe);
                        }
                    } catch (final JSONException e) {
                        Log.e("YNOV", "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

                    }
                } else {
                    Log.e("YNOV", "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                adapter.addAll(listRecipe);
                adapter.notifyDataSetChanged();

            }

        }

    public class RecipeAdapter extends ArrayAdapter<Recipe> {
        public RecipeAdapter(Context context, List<Recipe> prospects) {
            super(context, 0, prospects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Recipe recipe = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
            }
            // Lookup view for data population
            TextView title = (TextView) convertView.findViewById(R.id.title);
            // Populate the data into the template view using the data object
            title.setText(recipe.getTitle());


            // Return the completed view to render on screen
            return convertView;
        }
    }

}
