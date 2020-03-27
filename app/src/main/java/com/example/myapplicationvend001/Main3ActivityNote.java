package com.example.myapplicationvend001;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main3ActivityNote extends AppCompatActivity implements OnClickListener {

    Button buttonzap, buttondel;
    EditText EdittextBloknot;
    String[] massivestr;

    DBHelper dbHelper;

    final String LOG_TAG = "myLogs";

    Integer x,y,idColIndex,pozvdb;
    //boolean trnulltabl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3_note);

        buttonzap = (Button) findViewById(R.id.buttonzap);
        buttonzap.setOnClickListener(this);
        buttondel = (Button) findViewById(R.id.buttondel);
        buttondel.setOnClickListener(this);

        EdittextBloknot = (EditText) findViewById(R.id.EdittextBloknot);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);

        // находим список
        ListView Listzam = (ListView) findViewById(R.id.Listzam);
        // устанавливаем режим выбора пунктов списка
        Listzam.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        readdbtoList();

        dbHelper.close();

    }

    @Override
    public void onClick(View v) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String zametka = "   " + EdittextBloknot.getText().toString();

        // находим список
        ListView Listzam = (ListView) findViewById(R.id.Listzam);
        // устанавливаем режим выбора пунктов списка
        Listzam.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();




        switch (v.getId()) {
            case R.id.buttonzap:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");
                // подготовим данные для вставки в виде пар: наименование столбца - значение

                cv.put("zametka", zametka);

                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);



                readdbtoList ();
                break;

            case R.id.buttondel:
                //Log.d(LOG_TAG, "checked: " + Listzam.getCheckedItemPosition());
                Integer poz = Listzam.getCheckedItemPosition();
                poz++;


                // делаем запрос всех данных из таблицы mytable, получаем Cursor
                Cursor c = db.query("mytable", null, null, null, null, null, null);
                // проверка на не пустую таблицу
                if (c.moveToFirst()) {


                    // определяем номера столбцов по имени в выборке
                    idColIndex = c.getColumnIndex("id");
                    for (int x = 1; x<=poz; x++){

                        // извлекаем номер строки
                         pozvdb = c.getInt(idColIndex);
                        c.moveToNext();
                    }
                    db.delete("mytable", "id = " + pozvdb, null);

                }

                //Log.d(LOG_TAG, String.valueOf(pozvdb));

                //обновляем список
                readdbtoList ();
                break;


        }

        // закрываем подключение к БД
        dbHelper.close();
    }

    void readdbtoList (){

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // находим список
        ListView Listzam = (ListView) findViewById(R.id.Listzam);

        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);



        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {


            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int zametkaColIndex = c.getColumnIndex("zametka");




            // узнаем кол-во записей в массиве
            x = 0;
            do {
                x++;


            } while (c.moveToNext());
            c.moveToFirst();
            massivestr = new String [x];
             y = 0;
            do {
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла

                Log.d(LOG_TAG,
                       "ID = " + c.getInt(idColIndex) +
                               ", name = " + c.getString(zametkaColIndex));
                massivestr[y] = c.getString(zametkaColIndex);


                y++;

            } while (c.moveToNext() && y<=x-1);

            //for (int j=0;(j<=x-1);j++){
              //  Log.d(LOG_TAG,massivestr [j]);
            //}

        }
        else {
            // если БД пустая, то обнуляем массив
            massivestr = new String [1];
            massivestr [0] = "";

        }

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, massivestr);
        // присваиваем адаптер списку
        Listzam.setAdapter(adapter);

    }



    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "zametka text"+ ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
