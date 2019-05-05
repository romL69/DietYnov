package com.ynov.dietynov;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipesSQLite extends SQLiteOpenHelper {

    public static String getTableRecipes() {
        return TABLE_RECIPES;
    }

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
    private static final String COL_NAME= "Name";

    private  static final String COL_ORDER = "Order_Step";

    private static final String COL_TIME_TOTAL = "Total";
    private static final String COL_TIME_PREP = "Prep";
    private static final String COL_TIME_BAKING = "Baking";

    private static final String COL_NUTRITION ="Nutrition";
    private static final String COL_RECIPE_ID = "recipe_id";


    private static final String CREATE_TABLE_RECIPES = "CREATE TABLE " + TABLE_RECIPES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TITLE + " TEXT NOT NULL, "
            + COL_PORTIONS + " INTEGER NOT NULL, "
            + COL_PICTURE_URL + " TEXT NOT NULL);";
    private static final String CREATE_TABLE_INGREDIENTS = "CREATE TABLE " + TABLE_INGREDIENTS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_QUANTITY + " INTEGER NOT NULL, "
            + COL_UNIT + " TEXT NOT NULL, "
            + COL_NAME + " TEXT NOT NULL, "
            + COL_RECIPE_ID+ " INTEGER NOT NULL); ";

    private static final String CREATE_TABLE_STEPS= "CREATE TABLE " + TABLE_STEPS + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ORDER + " INTEGER NOT NULL, "
            + COL_NAME + " TEXT NOT NULL, "
            + COL_RECIPE_ID+ " INTEGER NOT NULL); ";

    private static final String CREATE_TABLE_TIME = "CREATE TABLE " + TABLE_TIME + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TIME_TOTAL + " INTEGER NOT NULL, "
            + COL_TIME_BAKING + " INTEGER NOT NULL, "
            + COL_TIME_PREP + " INTEGER NOT NULL, "
            + COL_RECIPE_ID + " INTEGER NOT NULL); ";



    public RecipesSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPES);
        db.execSQL(CREATE_TABLE_INGREDIENTS);
        db.execSQL(CREATE_TABLE_STEPS);
        db.execSQL(CREATE_TABLE_TIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE " + TABLE_TIME + ";");
        db.execSQL("DROP TABLE " + TABLE_INGREDIENTS + ";");
        db.execSQL("DROP TABLE " + TABLE_STEPS + ";");
        db.execSQL("DROP TABLE " + TABLE_RECIPES + ";");
        onCreate(db);
    }
}
