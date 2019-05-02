package com.ynov.dietynov;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    Intent intent;
    TextView title;
    TextView portions;
    TextView time;
    TextView ingredients;
    TextView steps;
    TextView nutritions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view_activity);
        intent=getIntent();
        Recipe recipe =(Recipe) intent.getSerializableExtra("recipe");

        title = findViewById(R.id.title);
        title.setText(recipe.getTitle());

        portions = findViewById(R.id.portions);
        portions.setText("Nombre de portins : " + recipe.getPortion());

        time = findViewById(R.id.time);
        time.setText("Préparation : " + recipe.getTime().getPrep() + " min. Cuisson : " + recipe.getTime().getBaking() + " min. Total : " + recipe.getTime().getTotal());

        ArrayList<Ingredient> Ingre = recipe.getIngredient();
        String ing = "Ingredients : \n";
        for(int i = 0; i < Ingre.size(); i++)
        {
           ing=ing.concat(Ingre.get(i).getName() + " " + Ingre.get(i).getQuantity() + "  " + Ingre.get(i).getUnit() + "\n");
        }

        ingredients = findViewById(R.id.ingredients);
        ingredients.setText(ing);

        ArrayList<Step> Steps = recipe.getStep();
        String step = "Etapes : \n";
        for(int i =0 ; i < Steps.size(); i++)
        {
            step=step.concat(Steps.get(i).getStep() + "\n");
        }
        steps =findViewById(R.id.steps);
        steps.setText(step);

        new DownloadImageTask((ImageView) findViewById(R.id.recipe_image)).execute(recipe.getPicture());



    }
}
