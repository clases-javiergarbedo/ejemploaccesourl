package es.javiergarciaescobedo.ejemploaccesourl;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Toast;

/**
 *     <uses-permission android:name="android.permission.INTERNET"/>
 */

public class MainActivity extends ActionBarActivity {

    MainFragment mainFragment = null;   //Variable que hará referencia al Fragment que se añadirá
                                        //  dentro de esta Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Crea el Fragment y a continuación lo añade dentro de esta Activity
        mainFragment = new MainFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new MainFragment())
                    .add(R.id.container, mainFragment)
                    .commit();
        }
    }

    //Método que recoge la pulsación del botón que se encuentra en pantalla
    public void buttonPressed(View v) {
        //Toda la funcionalidad se encuentra en el método accessUrl
        mainFragment.accessUrl();
    }

}
