package es.javiergarciaescobedo.ejemploaccesourl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import es.javiergarciaescobedo.android.helper.XmlDownloader;


// Más información sobre la conexión HTTP desde Android en:
// http://developer.android.com/training/basics/network-ops/connecting.html
//
// Requiere los siguientes permisos en AndroidManifest:
// <uses-permission android:name="android.permission.INTERNET"/>
// <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

public class MainFragment extends Fragment
        implements View.OnClickListener { // Este fragment debe detectar las pulsaciones de ratón
    // en lugar de su Activity como sucedería por defecto.
    // Requiere implementar el método onClick(View v)

    // Etiqueta para los mensajes de depuración. Se usará el nombre de esta clase
    private static final String DEBUG_TAG = MainFragment.class.getName();

    private EditText editText1 = null;  // EditText donde se mostrará el contenido descargado

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Cargar el Fragment con la estructura del layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Obtener referencias a los elementos de la pantalla
        editText1 = (EditText) rootView.findViewById(R.id.editText1);
        Button button1 = (Button) rootView.findViewById(R.id.button1);

        // Indicar que esta misma clase se va encargar de gestionar los clics en el botón
        // Para ello, se ha especificado en su cabecera que implementa la clase View.OnClickListener
        button1.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Se ha pulsado el botón para iniciar la descarga
            case R.id.button1:
                // URL con el contenido que se va a descargar
                String stringUrl = "http://www.rtve.es/rss/temas_espana.xml";

                //Comprobar que hay conexión a Internet
                if(XmlDownloader.hasInternetConnection(getActivity())) {
                    // Hay conexión a Internet, así que se debe iniciar la descarga.
                    // La llamada al método execute inicia la ejecución del método doInBackground
                    // de la clase asíncrona (ejecutada en segundo plano) DownloadWebpageTask que
                    // se encuentra declarada más adelante
                    new DownloadXmlTask().execute(stringUrl);
                } else {
                    // No hay conexión a Internet
                    Toast.makeText(getActivity(), "No hay conexión a Internet", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // Se usa una clase heredada de AsyncTask para que se ejecute en segundo plano, de manera que
    // la descarga de datos se realice sin que se quede parada la aplicación durante el tiempo que
    // dure la descarga.
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        // Las operaciones que se desean ejecutar en segundo plano se deben indicar en el método
        // doInBackground que recibirá como parámetro un array de Strings.
        // Este método se ejecuta cuando se invoca al método execute. Esa llamada se ha hecho antes,
        // y se le ha pasado por parámetro un único String con la URL del contenido a descargar.
        // Por tanto en el primer elemento del array urls (parámetro recibido) estará esa dirección
        // Retornará un String con el contenido de la web, o null en caso de error
        @Override
        protected String doInBackground(String... urls) {
            try {
                // Intentar realizar la descarga de la URL recibida por parámetro, la cual se ha
                // indicado en la llamada al método execute
                return XmlDownloader.downloadUrlContent(urls[0]);
            } catch (IOException e) {
                return null;
            }
        }

        // El método onPostExecute se ejecuta automáticamente cuando ha terminado el proceso de la
        // tarea que ha estado realizando en segundo plano el método doInBackground.
        // Como parámetro recibe el String que el método doInBackground ha retornado, el cual
        // almacena el contenido de la URL a la que se ha accedido
        @Override
        protected void onPostExecute(String result) {
            if(result != null) {
                // Mostrar en pantalla el contenido obtenido de la web
                editText1.setText(result);
            } else {
                // No se ha podido realizar la descarga. Mostrar un mensaje de error
                Toast.makeText(getActivity(),
                        "No se ha podido realizar la descarga. La dirección podría ser incorrecta",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


}
