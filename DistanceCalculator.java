import io.github.pixee.security.BoundedLineReader;
import java.io.*;
import java.math.RoundingMode;
import java.net.HttpURLConnection;

import json.Elements;
import json.GoogleMaps;
import json.Rows;
import org.json.*;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Adam Stone on 10/29/2015.
 */
public class DistanceCalculator {

    public static void main(String[] args) {

        final String FILE_NAME = args[0];
        final String ADDRESS = args[1]; //either ZIP Code or Address (ex: "8000 York Road" is entered as "8000+York+Rd")
        final String API_KEY = args[2]; //API Key from Google Maps API

        ArrayList<String> townList = new ArrayList<String>();
        String data = null;
        boolean success;
        String preFileName;
        PrintWriter writer;
        String value;

        try {

            File file = new File(FILE_NAME);
            Scanner in = new Scanner(file);

            preFileName = FILE_NAME.substring(0, (FILE_NAME.lastIndexOf(".")));
            writer = new PrintWriter(preFileName + "Output.txt", "UTF-8");

            while(in.hasNextLine()) {

                String line = in.nextLine();
                townList.add(line);

            }

            for(int i = 0; i < townList.size(); i++) {

                if ((townList.get(i).equals("")) || (townList.get(i).trim().isEmpty()) || (townList.get(i).contains(ADDRESS))) {
                    System.out.println("Address was either empty or was too close to the given address. 0 was written to output.");
                    writer.println("0");
                } else {

                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + townList.get(i) + "&destinations=" + ADDRESS + "&key=" + API_KEY;

                    HttpURLConnection c = null;
                    try {
                        URL u = new URL(url);
                        c = (HttpURLConnection) u.openConnection();
                        c.setRequestMethod("GET");
                        c.setRequestProperty("Content-length", "0");
                        c.setUseCaches(false);
                        c.setAllowUserInteraction(false);
                        c.setConnectTimeout(10000);
                        c.setReadTimeout(10000);
                        c.connect();
                        int status = c.getResponseCode();

                        switch (status) {
                            case 200:
                            case 201:
                                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                String line;
                                while ((line = BoundedLineReader.readLine(br, 5_000_000)) != null) {
                                    sb.append(line + "\n");
                                }
                                br.close();
                                data = String.valueOf(sb);
                                success = getSuccess(data);
                                if (success) {
                                    value = DistanceCalculator.getValue(data);
                                    System.out.println(i + ": From " + townList.get(i) + " to " + ADDRESS + " = " + value);
                                    writer.println(value);
                                } else {
                                    writer.println("0");
                                    System.out.println("Distance not found for " + townList.get(i) + ". 0 was written to output.");
                                }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (c != null) {
                            try {
                                c.disconnect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            writer.close();
         } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static boolean getSuccess(String data) {

        Gson gson = new Gson();
        GoogleMaps googleMaps = gson.fromJson(data, GoogleMaps.class);

        Rows[] rows = googleMaps.getRows();
        Elements[] elements = rows[0].getElements();
        return elements[0].getStatus().equals("OK");
    }

    public static String getValue(String data) {

        Gson gson = new Gson();
        GoogleMaps googleMaps = gson.fromJson(data, GoogleMaps.class);

        Rows[] rows = googleMaps.getRows();
        Elements[] elements = rows[0].getElements();
        String distanceText = elements[0].getDistance().getText();
        distanceText = distanceText.replace(" km", "");
        distanceText = distanceText.replace(",", "");
        String miles = kmToMi(Double.parseDouble(distanceText));
        double dMiles = Double.parseDouble(miles);
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        String milesFormatted = df.format(dMiles);
        distanceText = (milesFormatted);

        return distanceText;
    }

    public static String kmToMi(double kilometers) {
        // Assume there are 0.6213 miles in a kilometer.
        return String.valueOf(kilometers * 0.6213);
    }
}
