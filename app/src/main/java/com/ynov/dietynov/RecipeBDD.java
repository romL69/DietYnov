package com.ynov.dietynov;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RecipeBDD {
    private static final int VERSION_BDD = 3;
    private static final String NOM_BDD = "ynov.db";

    private static final String TABLE_RECIPES = "table_recipes";
    private static final String TABLE_INGREDIENTS = "table_ingredients";
    private static final String TABLE_STEPS = "table_steps";
    private static final String TABLE_TIME = "table_time";
    private static final String TABLE_NUTRITION = "table_nutrition";

    private static final String COL_ID = "ID";
    private static final String COL_TITLE = "Title";
    private static final String COL_PORTIONS = "Portions";
    private static final String COL_PICTURE_URL = "Picture_url";

    private static final String COL_QUANTITY = "Quantity";
    private static final String COL_UNIT = "Unit";
    private static final String COL_NAME = "Name";

    private static final String COL_ORDER = "Order_Step";

    private static final String COL_TIME_TOTAL = "Total";
    private static final String COL_TIME_PREP = "Prep";
    private static final String COL_TIME_BAKING = "Baking";

    private static final String COL_NUTRITION = "Nutrition";
    private static final String COL_RECIPE_ID = "recipe_id";


    private static final int NUM_COL_ID = 0;
    private String[] allColumns = {COL_ID, COL_TITLE, COL_PORTIONS, COL_PICTURE_URL};

    private SQLiteDatabase bdd;

    private RecipesSQLite maBaseSQLite;

    public RecipeBDD(Context context) {
        //On crÃ©e la BDD et sa table
        maBaseSQLite = new RecipesSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open() {
        //on ouvre la BDD en Ã©criture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        //on ferme l'accÃ¨s Ã  la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD() {
        return bdd;
    }

    public void insertRecipe(Recipe recipe) {
        ContentValues values_recipe = new ContentValues();
        values_recipe.put(COL_TITLE, recipe.getTitle());
        values_recipe.put(COL_PICTURE_URL, recipe.getPicture());
        values_recipe.put(COL_PORTIONS, recipe.getPortion());

        //on insÃ¨re l'objet dans la BDD via le ContentValues
        bdd.insert(TABLE_RECIPES, null, values_recipe);

        ContentValues values_time = new ContentValues();
        values_time.put(COL_TIME_TOTAL, recipe.getTime().getTotal());
        values_time.put(COL_TIME_BAKING, recipe.getTime().getBaking());
        values_time.put(COL_TIME_PREP, recipe.getTime().getPrep());
        values_time.put(COL_RECIPE_ID, this.getRecipeWithTitle(recipe.getTitle()).getId());
        bdd.insert(TABLE_TIME, null, values_time);

        ContentValues values_ingredients = new ContentValues();
        for (Ingredient ingredient : recipe.getIngredient()) {
            values_ingredients.put(COL_QUANTITY, ingredient.getQuantity());
            values_ingredients.put(COL_NAME, ingredient.getName());
            values_ingredients.put(COL_UNIT, ingredient.getUnit());
            values_ingredients.put(COL_RECIPE_ID, this.getRecipeWithTitle(recipe.getTitle()).getId());
            bdd.insert(TABLE_INGREDIENTS, null, values_ingredients);
        }

        ContentValues values_steps = new ContentValues();
        for (Step step : recipe.getStep()) {
            values_steps.put(COL_ORDER, step.getOrder());
            values_steps.put(COL_NAME, step.getStep());
            values_steps.put(COL_RECIPE_ID, this.getRecipeWithTitle(recipe.getTitle()).getId());
            bdd.insert(TABLE_STEPS, null, values_steps);
        }


    }

    public int updateRecipe(int id, Recipe recipe) {

        ContentValues values = new ContentValues();

        return bdd.update(TABLE_RECIPES, values, COL_ID + " = " + id, null);
    }

    public int removeRecipeWithID(int id) {
        //Suppression d'un prospect de la BDD grÃ¢ce Ã  l'ID
        bdd.delete(TABLE_TIME, COL_RECIPE_ID + " = " + id, null);
        bdd.delete(TABLE_STEPS, COL_RECIPE_ID + " = " + id, null);
        bdd.delete(TABLE_INGREDIENTS, COL_RECIPE_ID + " = " + id, null);
        return bdd.delete(TABLE_RECIPES, COL_ID + " = " + id, null);
    }

    public Recipe getRecipeWithTitle(String title) {
        bdd = maBaseSQLite.getReadableDatabase();
        Cursor c = bdd.query(TABLE_RECIPES, new String[] {COL_ID, COL_TITLE, COL_PORTIONS, COL_PICTURE_URL}, COL_TITLE + " LIKE \"" + title + "\"" , null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            return cursorToRecipe(c);
        } else {
            return null;
        }
    }

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        bdd = maBaseSQLite.getReadableDatabase();
        Cursor cursor = bdd.query(TABLE_RECIPES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursorToRecipe(cursor);
            recipes.add(recipe);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return recipes;
    }

    private Recipe cursorToRecipe(Cursor c) {
        //si aucun Ã©lÃ©ment n'a Ã©tÃ© retournÃ© dans la requÃªte, on renvoie null
        if (c.getCount() == 0)
            return null;

        Cursor cursor_time = bdd.query(TABLE_TIME,new String[] {COL_ID, COL_TIME_TOTAL, COL_TIME_BAKING , COL_TIME_PREP, COL_RECIPE_ID} ,COL_RECIPE_ID + " LIKE \"" + c.getInt(NUM_COL_ID) +"\"" , null ,null , null ,null);
        cursor_time.moveToFirst();
        Time time = new Time();
        if (!cursor_time.isAfterLast()) {
            time.setTotal(cursor_time.getInt(1));
            time.setBaking(cursor_time.getInt(2));
            time.setPrep(cursor_time.getInt(3));
        }


        if (cursor_time != null && !cursor_time.isClosed()) {
            cursor_time.close();
        }

        //Get All Ingredients of recipe
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        Cursor cursoringredients = bdd.query(TABLE_INGREDIENTS,new String[] {COL_ID, COL_QUANTITY, COL_UNIT , COL_NAME, COL_RECIPE_ID} ,COL_RECIPE_ID + " LIKE \"" + c.getInt(NUM_COL_ID) +"\"", null ,null , null ,null);
        cursoringredients.moveToFirst();
        while (!cursoringredients.isAfterLast()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setQuantity(cursoringredients.getInt(1));
            ingredient.setUnit(cursoringredients.getString(2));
            ingredient.setName(cursoringredients.getString(3));
            ingredients.add(ingredient);
            cursoringredients.moveToNext();
        }
        if (cursoringredients != null && !cursoringredients.isClosed()) {
            cursoringredients.close();
        }
        ArrayList<Step> steps = new ArrayList<>();
        Cursor cursor_steps = bdd.query(TABLE_STEPS,new String[] {COL_ID, COL_ORDER, COL_NAME, COL_RECIPE_ID} ,COL_RECIPE_ID + " LIKE \"" + c.getInt(NUM_COL_ID) +"\"", null ,null , null ,null);
        cursor_steps.moveToFirst();
        while (!cursor_steps.isAfterLast()) {
            Step step = new Step();
            step.setOrder(cursor_steps.getInt(1));
            step.setStep(cursor_steps.getString(2));
            steps.add(step);
            cursor_steps.moveToNext();
        }

        if (cursor_steps != null && !cursor_steps.isClosed()) {
            cursor_steps.close();
        }

        Recipe recipe = new Recipe();
        recipe.setId(c.getInt(NUM_COL_ID));
        recipe.setTitle(c.getString(1));
        recipe.setPortion(c.getInt(2));
        recipe.setPicture(c.getString(3));
        recipe.setTime(time);
        recipe.setIngredient(ingredients);
        recipe.setStep(steps);

        return recipe;
    }

}