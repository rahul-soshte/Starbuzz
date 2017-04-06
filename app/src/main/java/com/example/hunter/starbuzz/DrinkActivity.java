package com.example.hunter.starbuzz;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DrinkActivity extends Activity {

    public static final String EXTRA_DRINKNO = "drinkNo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        //Get the drink from the intent
        int drinkNo = (Integer) getIntent().getExtras().get(EXTRA_DRINKNO);
       // Drink drink = Drink.drinks[drinkNo];
        //Populate the drink image
        try {
            SQLiteOpenHelper starbuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
            SQLiteDatabase db = starbuzzDatabaseHelper.getWritableDatabase();
            Cursor cursor = db.query("DRINK", new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID","FAVORITE"}, "_id=?", new String[]{Integer.toString(drinkNo)}, null, null, null);
            if (cursor.moveToFirst()) {

                String nameText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                int photoId = cursor.getInt(2);
         boolean isFavorite=(cursor.getInt(3)==1);

                TextView name = (TextView) findViewById(R.id.name);
                name.setText(nameText);


                TextView description = (TextView) findViewById(R.id.description);
                description.setText(descriptionText);


                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(photoId);
                photo.setContentDescription(nameText);

                //Populate the checkbox
                CheckBox favorite=(CheckBox)findViewById(R.id.favorite);
                favorite.setChecked(isFavorite);

            };
            cursor.close();
            db.close();

        }catch (SQLiteException e)
        {
            Toast toast=Toast.makeText(this,"Database unavailable",Toast.LENGTH_SHORT);
            toast.show();

        }

    }
    //Update the database when the checkbox is clicked
    public void onFavoriteClicked(View view)
    {
        int drinkNo=(Integer)getIntent().getExtras().get("drinkNo");
        CheckBox favorite=(CheckBox)findViewById(R.id.favorite);
        ContentValues drinkValues=new ContentValues();
        drinkValues.put("FAVORITE",favorite.isChecked());
        SQLiteOpenHelper starbuzzDatabasHelper=new StarbuzzDatabaseHelper(DrinkActivity.this);
        try{
            SQLiteDatabase db=starbuzzDatabasHelper.getWritableDatabase();
            db.update("DRINK",drinkValues,"_id=?",new String[]{Integer.toString(drinkNo)});

        db.close();

        }
        catch(SQLiteException e)
        {
            Toast toast=Toast.makeText(this,"Database unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }


    }
}