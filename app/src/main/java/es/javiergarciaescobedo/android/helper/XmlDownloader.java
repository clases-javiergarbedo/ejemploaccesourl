package es.javiergarciaescobedo.android.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlDownloader {

    private static final String DEBUG_TAG = XmlDownloader.class.getName();

    public static boolean hasInternetConnection(Context context) {
        Log.d(DEBUG_TAG, "Checking Internet connection ...");
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(DEBUG_TAG, "Internet connection: OK");
            return true;
        } else
            Log.d(DEBUG_TAG, "Internet connection: Disconnected");
            return false;
    }


    public static String downloadUrlContent(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);

            // Config connection reducing connection attempt time to 15 seconds and
            // data reception time to 10 seconds, using GET method and allowing data reception
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            Log.d(DEBUG_TAG, "Connecting with " + myurl);
            conn.connect();

            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "Response code: " + response);

            // Get content from URL as InputStream
            is = conn.getInputStream();

            // Convert content (InputStream) to String using a trick
            String contentAsString = convertStreamToString(is);
            return contentAsString;

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static Document getXmlDocument(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
        } catch (ParserConfigurationException e) {
            Log.e(DEBUG_TAG, "Error: "+e.getMessage());
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            Log.e(DEBUG_TAG, "Error: "+e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e(DEBUG_TAG, "Error: "+e.getMessage());
            e.printStackTrace();
            return null;
        }
        return doc;
    }

    public static String getXmlElementValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        Node nodo = n.item(0);
        if (nodo != null) {
            if (nodo.hasChildNodes()) {
                for (Node child = nodo.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return null;
    }

    // InputStream to String converter
    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
