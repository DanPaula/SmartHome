package com.example.smarthome;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.os.AsyncTask;
import android.util.Log;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.content.Context;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    private Context context;
    public String sensorID;

    public HttpAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String userId = params[0];
        String response = "";

        try {

            // Send user ID to Arduino
            URL userUrl = new URL("http:///user");
            HttpURLConnection userConnection = (HttpURLConnection) userUrl.openConnection();
            userConnection.setRequestMethod("POST");
            userConnection.setDoOutput(true);

            OutputStream userOs = userConnection.getOutputStream();
            userOs.write(userId.getBytes());
            userOs.flush();
            userOs.close();

            int userResponseCode = userConnection.getResponseCode();
            if (userResponseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader userIn = new BufferedReader(new InputStreamReader(userConnection.getInputStream()));
                String userInputLine;
                StringBuilder userResponseBuilder = new StringBuilder();
                while ((userInputLine = userIn.readLine()) != null) {
                    userResponseBuilder.append(userInputLine);
                }
                userIn.close();
                response += "User ID sent successfully. ";
            } else {
                response += "Error sending user ID: " + userResponseCode;
            }
            userConnection.disconnect();

            // Receive sensor ID from Arduino
//            URL sensorUrl = new URL("http://192.168.1.135/sensor");
//            HttpURLConnection sensorConnection = (HttpURLConnection) sensorUrl.openConnection();
//            sensorConnection.setRequestMethod("GET");

           // int sensorResponseCode = sensorConnection.getResponseCode();
//            if (sensorResponseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader sensorIn = new BufferedReader(new InputStreamReader(sensorConnection.getInputStream()));
//                String sensorInputLine;
//                StringBuilder sensorResponseBuilder = new StringBuilder();
//                while ((sensorInputLine = sensorIn.readLine()) != null) {
//                    sensorResponseBuilder.append(sensorInputLine);
//                }
//                sensorIn.close();
//                String sensorId = sensorResponseBuilder.toString();
//                sensorID = sensorId;
//                response += "Sensor ID received: " + sensorId;
//            } else {
//                response += "Error receiving sensor ID: " + sensorResponseCode;
//            }
//            sensorConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.getMessage();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("HTTP Response", result);
    }
}