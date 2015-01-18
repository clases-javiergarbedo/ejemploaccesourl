package es.javiergarciaescobedo.ejemploaccesourl;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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

    public void accessUrl() {
        Toast.makeText(getActivity(), "Enviando datos", Toast.LENGTH_LONG).show();
        Log.d("kk", "pulsado");
        UrlGet urlGet = new UrlGet();
        Log.d("kk", "paso2");
        urlGet.execute("http://www.rtve.es/rss/temas_espana.xml");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        editText = (EditText) rootView.findViewById(R.id.editText);
        return rootView;
    }

    public class UrlGet extends AsyncTask<String, Void, InputStream> {

        private boolean connectionError = false;

        @Override
        protected java.io.InputStream doInBackground(String... urls) {
            Log.d(UrlGet.class.getName(), "Iniciando llamada a URL: " + urls[0]);
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(urls[0]);


                //URL url = new URL("http://www.android.com/");
                urlConnection = (HttpURLConnection) url.openConnection();
                //Acortar el tiempo de intento de conexión y de recepción de datos
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                inputStream = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                String answer = "Respuesta: ";

                try {
                    while ((inputLine = br.readLine()) != null) {
                        answer+=inputLine;
                    }
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(UrlGet.class.getName(), answer);
                editText.setText(answer, TextView.BufferType.NORMAL);
            } catch (IOException e) {
                connectionError = true;
                e.printStackTrace();
            } finally {
//                urlConnection.disconnect();
//                if (inputStream != null) {
//                    try {
//                        inputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            Log.d(UrlGet.class.getName(), "PostExceute");
            if(connectionError) {
                Log.d(UrlGet.class.getName(), "Mostranbdo error");
                Toast.makeText(getActivity(), "No se ha podido realizar la conexión con el servidor", Toast.LENGTH_LONG).show();;
                Log.d(UrlGet.class.getName(), "Mostrado error");
            } else {
                Toast.makeText(getActivity(), "Datos enviados correctamente", Toast.LENGTH_LONG).show();
            }
        }

    }

}
