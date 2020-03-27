package com.example.myapplicationvend001;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Main2ActivityConfig extends AppCompatActivity implements View.OnClickListener {

    EditText editkolvotochek;
    EditText editnazvtochki;
    TextView Viewpozmas;

    Button buttonback2;
    Button buttonsave2;
    Button buttonforward2;


    String [] myArray; // объявление массива
    String bufferMassiva;
    Integer kolvotochek;
    Integer pozvmassive;


    final String filedatamassive = "filedatamassive.txt";
    final String fileconfig = "fileconfig.txt";
    final String LOG_TAG = "myLogs";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_config);

        // находим элементы
        editkolvotochek = (EditText) findViewById(R.id.editkolvotochek);
        editnazvtochki = (EditText) findViewById(R.id.editnazvtochki);
        Viewpozmas = (TextView) findViewById(R.id.Viewpozmas);

        buttonback2 = (Button) findViewById(R.id.buttonback2);
        buttonsave2 = (Button) findViewById(R.id.buttonsave2);
        buttonforward2 = (Button) findViewById(R.id.buttonforward2);


        // прописываем обработчик
        buttonback2.setOnClickListener(this);
        buttonsave2.setOnClickListener(this);
        buttonforward2.setOnClickListener(this);


        String str = "";
        readFileconfig();
        str = String.valueOf(editkolvotochek.getText());


        //определение кол-ва элементов в массиве
        kolvotochek = Integer.parseInt(str);
        Log.d(LOG_TAG, String.valueOf(kolvotochek));
        myArray = new String[kolvotochek];
        pozvmassive = 0;
        readFilemassivdata();
    }

    // процедура перехода на 1 элемент назад в массиве
    void backMassive() {
        if (pozvmassive >= 1) {
            myArray[pozvmassive] = String.valueOf(editnazvtochki.getText());
            pozvmassive = pozvmassive - 1;
            editnazvtochki.setText(myArray[pozvmassive]);
            Viewpozmas.setText(String.valueOf(pozvmassive+1));

        }
        else {
            Log.d(LOG_TAG,"89");
            myArray[pozvmassive] = String.valueOf(editnazvtochki.getText());
            pozvmassive = kolvotochek;
            pozvmassive = pozvmassive - 1;
            editnazvtochki.setText(myArray[pozvmassive]);
            Viewpozmas.setText(String.valueOf(pozvmassive+1));

        }

    }

    // процедура перехода на 1 элемент вперед в массиве
    void forwardMassive() {
        if (pozvmassive <= (kolvotochek - 2 )) {
            myArray[pozvmassive] = String.valueOf(editnazvtochki.getText());
            pozvmassive = pozvmassive + 1;
            editnazvtochki.setText(myArray[pozvmassive]);
            Viewpozmas.setText(String.valueOf(pozvmassive+1));

        }
        else {
            myArray[pozvmassive] = String.valueOf(editnazvtochki.getText());
            pozvmassive = 0;
            editnazvtochki.setText(myArray[pozvmassive]);
            Viewpozmas.setText(String.valueOf(pozvmassive+1));

        }

    }


    void writeFileconfig() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(fileconfig,  MODE_PRIVATE)));
            // String str = "";
            // пишем данные
            bw.write(String.valueOf(editkolvotochek.getText()));
            // закрываем поток
            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void readFileconfig() {
        try {
            // открываем поток для чтения

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(fileconfig)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                editkolvotochek.setText(str);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeFilemassivdata() {
        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(filedatamassive,  MODE_PRIVATE)));
            // String str = "";
            // пишем массив с названием точек в файл
            for (int i = 0; i <= kolvotochek - 1; i++) {
                // пишем данные
                bw.write(String.valueOf(myArray[i]));
                bw.newLine();
            }
            // закрываем поток
            bw.close();
            // вывод сообщения
            Toast.makeText(this, "Файл записан", Toast.LENGTH_LONG).show();

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonsave2:
                writeFileconfig();
                //Log.d(LOG_TAG, "182");
                writeFilemassivdata();
                break;
            case R.id.buttonback2:
                backMassive();
                break;
            case R.id.buttonforward2:
                forwardMassive();
                break;



        }
    }
}
