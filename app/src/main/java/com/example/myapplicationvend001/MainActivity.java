package com.example.myapplicationvend001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.Permissions;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final int MENU_NASTR_ID = 1;
    final int MENU_QUIT_ID = 3;
    final int MENU_SAVE_ID = 2;
    final int MENU_BLOKN_ID = 4;


    TextView Viewpozmas1;
    TextView editTextData;
    TextView textViewtorg;
    TextView kolvotochek1;
    EditText textSumma;
    EditText textViewKolvo;
    EditText textViewVyash;
    EditText textViewVtubu;
    EditText textViewBankn;
    EditText textViewNaSdachu;
    EditText Edittextbeznal;

    Button buttonBack;
    Button buttonSave;
    Button buttonForward;
    Button buttonCalend;

    final String fileconfig = "fileconfig.txt";
    final String LOG_TAG = "myLogs";
    final String filedatamassive = "filedatamassive.txt";

    final String DIR_SD = "MyVendFiles";
    String FILENAME_SD = "Statistika";
    String FILENAME_SDFinal;

    String [] myArray; // объявление массива с названием точек
    String [] nazvpunkt; // объявление массива с названием пунктов меню
    String [][] osnovnMassive; //объявление массива с данными
    Integer kolvotochek;
    Integer pozvmassive;



    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        // находим элементы

        Viewpozmas1 = (TextView) findViewById(R.id.Viewpozmas1);
        textViewtorg = (TextView) findViewById(R.id.textViewtorg);
        kolvotochek1 = (TextView) findViewById(R.id.kolvotochek1);
        editTextData = (TextView) findViewById(R.id.editTextData);

        textSumma = (EditText) findViewById(R.id.textSumma);
        textViewKolvo = (EditText) findViewById(R.id.textViewKolvo);
        textViewVyash = (EditText) findViewById(R.id.textViewVyash);
        textViewVtubu = (EditText) findViewById(R.id.textViewVtubu);
        textViewBankn = (EditText) findViewById(R.id.textViewBankn);
        textViewNaSdachu = (EditText) findViewById(R.id.textViewNaSdachu);
        Edittextbeznal = (EditText) findViewById(R.id.Edittextbeznal);

        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonForward = (Button) findViewById(R.id.buttonForward);
        buttonCalend = (Button) findViewById(R.id.buttonCalend);

        // прописываем обработчик
        buttonBack.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonForward.setOnClickListener(this);
        buttonCalend.setOnClickListener(this);

        String str = "";
        readFileconfig();
        str = String.valueOf(kolvotochek1.getText());

        //определение кол-ва элементов в массиве
        kolvotochek = Integer.parseInt(str);
        Log.d(LOG_TAG, String.valueOf(kolvotochek));
        myArray = new String[kolvotochek];

        pozvmassive = 0;
        readFilemassivdata();

        textViewtorg.setText(myArray[0]);
        //задание времени
        SimpleDateFormat sdfdate = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdfdate.format(new Date(System.currentTimeMillis()));
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
        String time = sdftime.format(new Date(System.currentTimeMillis()));
        editTextData.setText("        "+ date + " " + time);


        nazvpunkt = new String[7];
        nazvpunkt[0] = "Сумма";
        nazvpunkt[1] = "Кол-во";
        nazvpunkt[2] = "В ящик";
        nazvpunkt[3] = "В тубу";
        nazvpunkt[4] = "Банкнот";
        nazvpunkt[5] = "На сдачу";
        nazvpunkt[6] = "Безнал";

        //заполнение массива пустыми элементами для того чтобы он не записывал гребанный null
        osnovnMassive = new String[7][kolvotochek];
        for (int i=0; i<=kolvotochek-1; i++){
            for (int j=0; j<=6; j++){
                osnovnMassive [j][i] = "";
            }

        }

        readbufferSD();
        izMassivavFormu();


    }



    // процедура перехода на 1 элемент вперед в массиве
    void forwardMassive() {
        if (pozvmassive <= (kolvotochek - 2 )) {
            zapolnMassiva();
            pozvmassive = pozvmassive + 1;
            izMassivavFormu();


        }
        else {
            zapolnMassiva();
            pozvmassive = 0;
            izMassivavFormu();

        }

    }

    // процедура перехода на 1 элемент назад в массиве
    void backMassive() {
        if (pozvmassive >= 1) {
            zapolnMassiva();
            pozvmassive = pozvmassive - 1;
            izMassivavFormu();

        }
        else {
            zapolnMassiva();
            pozvmassive = kolvotochek;
            pozvmassive = pozvmassive - 1;
            izMassivavFormu();

        }

    }

    // заполнение массива
    void zapolnMassiva() {

        osnovnMassive[0][pozvmassive] = String.valueOf(textSumma.getText());
        osnovnMassive[1][pozvmassive] = String.valueOf(textViewKolvo.getText());
        osnovnMassive[2][pozvmassive] = String.valueOf(textViewVyash.getText());
        osnovnMassive[3][pozvmassive] = String.valueOf(textViewVtubu.getText());
        osnovnMassive[4][pozvmassive] = String.valueOf(textViewBankn.getText());
        osnovnMassive[5][pozvmassive] = String.valueOf(textViewNaSdachu.getText());
        osnovnMassive[6][pozvmassive] = String.valueOf(Edittextbeznal.getText());
    }

    // заполнение формы из массива
    void izMassivavFormu() {
        //for (int j=0; j<=6; j++){

          //  String str = osnovnMassive[j][pozvmassive];
            //Log.d(LOG_TAG, str );
            //if (str == "") {
              //  osnovnMassive[j][pozvmassive] = " ";
            //}
        //}
        textSumma.setText(osnovnMassive[0][pozvmassive]);
        textViewKolvo.setText(osnovnMassive[1][pozvmassive]);
        textViewVyash.setText(osnovnMassive[2][pozvmassive]);
        textViewVtubu.setText(osnovnMassive[3][pozvmassive]);
        textViewBankn.setText(osnovnMassive[4][pozvmassive]);
        textViewNaSdachu.setText(osnovnMassive[5][pozvmassive]);
        Edittextbeznal.setText(osnovnMassive[6][pozvmassive]);
        Viewpozmas1.setText(String.valueOf(pozvmassive+1));
        textViewtorg.setText(myArray[pozvmassive]);
    }

    void readFileconfig() {
        try {
            // открываем поток для чтения

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(fileconfig)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                kolvotochek1.setText(str);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //читаем файл в массив
    void readFilemassivdata() {
        try {
            // открываем поток для чтения

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(filedatamassive)));
            String str = "";
            int x = 0;
            // читаем содержимое
            while (((str = br.readLine()) != null) && x <= kolvotochek - 1 )   {
                myArray[x] = str;
                x++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFileSDmassive() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        //File sdPath = Environment.getExternalStorageDirectory();
        File sdPath = getExternalFilesDir(null);//getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        //Date date = new Date();

        //задание времени
        SimpleDateFormat sdfdate = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdfdate.format(new Date(System.currentTimeMillis()));
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
        String time = sdftime.format(new Date(System.currentTimeMillis()));

        // формируем объект File, который содержит путь к файлу
        FILENAME_SDFinal = date + " " + time +" " + textViewtorg.getText() + ".txt";
        File sdFile = new File(sdPath, FILENAME_SDFinal);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            bw.write(date);
            bw.newLine();

            for (int i = 0; i <= kolvotochek - 1; i++) {
                // пишем данные
                bw.newLine();
                bw.write(String.valueOf(myArray[i]));
                bw.newLine();
                for (int j = 0; j <= 6; j++) {
                    bw.write(String.valueOf(nazvpunkt[j]));
                    bw.newLine();
                    bw.write(String.valueOf(osnovnMassive[j][i]));
                    bw.newLine();
                }
            }
            // закрываем поток
            bw.close();
            // вывод сообщения
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writebufferSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
       // File sdPath = Environment.getExternalStorageDirectory();
        File sdPath = getExternalFilesDir(null);
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        sdPath.mkdirs();
        //Date date = new Date();

        //задание времени
        SimpleDateFormat sdfdate = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdfdate.format(new Date(System.currentTimeMillis()));


        // формируем объект File, который содержит путь к файлу
        FILENAME_SDFinal = date + " " + "BufferSD" + ".txt";
        File sdFile = new File(sdPath, FILENAME_SDFinal);
        try {
            // открываем поток для записи
            BufferedWriter bw = new BufferedWriter(new FileWriter(sdFile));
            //bw.write(date);


            for (int i = 0; i <= kolvotochek - 1; i++) {
                // пишем данные
                //bw.newLine();
                //bw.write(String.valueOf(myArray[i]));

                for (int j = 0; j <= 6; j++) {

                    bw.write(String.valueOf(osnovnMassive[j][i]));
                    bw.newLine();


                }
            }
            // закрываем поток
            bw.close();
            // вывод сообщения
            Log.d(LOG_TAG, "Файл записан на SD: " + sdFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readbufferSD() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        //File sdPath = Environment.getExternalStorageDirectory();
        File sdPath = getExternalFilesDir(null);
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        // создаем каталог
        //sdPath.mkdirs();
        SimpleDateFormat sdfdate = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdfdate.format(new Date(System.currentTimeMillis()));
        try {

            FILENAME_SDFinal = date + " " + "BufferSD" + ".txt";
            File sdFile = new File(sdPath, FILENAME_SDFinal);

            // открываем поток для чтения

            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            int x = 0;
            //int i = 0;
            // читаем содержимое
            while (((str = br.readLine()) != null) && x <= kolvotochek - 1 )   {
                osnovnMassive[0][x] = str;
                for (int j = 1; j <= 6; j++) {
                    str = br.readLine();
                    osnovnMassive[j][x] = str;


                }

                x++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Файл не найден на  SD " );


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // обработчик в момент закрытие приложения
    @Override
    protected void onDestroy() {
        super.onDestroy();
        zapolnMassiva();
        if (pozvmassive == (kolvotochek -1)) {
            writeFileSDmassive();
        }

        writebufferSD();


    }

    //обработчик нажатий кнопок
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                backMassive();
                break;
            case R.id.buttonSave:
                zapolnMassiva();
                if (pozvmassive == (kolvotochek -1)) {
                    writeFileSDmassive();
                    Toast.makeText(this, "Файл записан на SD", Toast.LENGTH_SHORT).show();
                }
                writebufferSD();
                Toast.makeText(this, "Буфер записан ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonForward:
                forwardMassive();
                break;
            case R.id.buttonCalend:
                CalendView ();
                break;
        }


    }

    void CalendView () {

        //Вызов календаря для записи

        // A date-time specified in milliseconds since the epoch.

        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, System.currentTimeMillis());
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());
        startActivity(intent);
    }

    // создание меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_NASTR_ID, 0, "Config");
        menu.add(0, MENU_SAVE_ID, 0, "Save data");
        menu.add(0, MENU_BLOKN_ID, 0, "View blokn");
        menu.add(0, MENU_QUIT_ID, 0, "Quit");
        return super.onCreateOptionsMenu(menu);
    }

    // обработка нажатий на пункты меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_NASTR_ID:
                // переход в настройки
                // TODO Call second activity
                Intent intent = new Intent(this, Main2ActivityConfig.class);
                startActivity(intent);
                break;
            case MENU_QUIT_ID:
                // выход из приложения
                finish();
                break;
            case MENU_SAVE_ID:
                //принудительное сохранение данных
                writeFileSDmassive();
                Toast.makeText(this, "Файл записан на SD", Toast.LENGTH_LONG).show();
                break;
            case MENU_BLOKN_ID:
                // переход в заметки
                // TODO Call therd activity
                Intent intent2 = new Intent(this, Main3ActivityNote.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
