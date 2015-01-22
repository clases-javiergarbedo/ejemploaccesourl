package es.javiergarciaescobedo.ejemploaccesourl;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainFragment extends Fragment {

    EditText editText = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        editText = (EditText) rootView.findViewById(R.id.editText);
        return rootView;
    }

    //Método que inicia el proceso de llamada a la URL
    public void accessUrl() {
        Toast.makeText(getActivity(), "Enviando datos", Toast.LENGTH_LONG).show();
        //Crea una instancia de la clase UrlGet que está declarada a continuación
        UrlGet urlGet = new UrlGet();
        //El método execute realiza el proceso de manera asíncrona, realizando implícitamente
        //  una llamada al método doInBackground
        urlGet.execute("http://www.rtve.es/rss/temas_espana.xml");
    }

    //Clase que permite realizar la llamada a una URL. Requiere que sea una extensión de la clase
    //  AsyncTask, ya que el proceso debe realizarse de manera asíncrona (se ejecuta en segundo
    //  plano, continuando la ejecución de la aplicación sin esperar a que finalice este proceso)
    public class UrlGet extends AsyncTask<String, Void, Void> {

        private boolean connectionError = false;
        String answer = ""; //Recogerá los datos obtenidos desde la URL invocada

        @Override
        protected Void doInBackground(String... urls) {
            Log.d(UrlGet.class.getName(), "Iniciando llamada a URL: " + urls[0]);
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                //Acortar el tiempo de intento de conexión y de recepción de datos
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                //Recibir el contenido de la URL solicitada
                inputStream = new BufferedInputStream(urlConnection.getInputStream());

                //Leer el flujo de datos recibido convirtiéndolo a String para poder mostrarlo
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                try {
                    while ((inputLine = br.readLine()) != null) {
                        answer += inputLine + "\n";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(UrlGet.class.getName(), answer);

            } catch (IOException e) {
                connectionError = true;
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        /**
         * Se ejecuta de manera automática cuando haya finalizado el proceso de descarga
         * de los datos desde la URL que ha sido solicitada
         */
        @Override
        protected void onPostExecute(Void result) {
            if (connectionError) {
                Toast.makeText(getActivity(), "No se ha podido realizar la conexión con el servidor", Toast.LENGTH_LONG).show();
                ;
            } else {
                Toast.makeText(getActivity(), "Datos enviados correctamente", Toast.LENGTH_LONG).show();
                //En este ejemplo, se muestran todos los datos recibidos en un EditText
                editText.setText(answer);
            }
        }

    }

}
