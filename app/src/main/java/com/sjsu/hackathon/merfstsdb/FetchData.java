package com.sjsu.hackathon.merfstsdb;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchData {
    ArrayList<String> allowedCountries;
    HashMap<String, String> dataTypes;

    public FetchData() {
        allowedCountries = new ArrayList<String>();
        allowedCountries.add("CN");
        allowedCountries.add("IN");
        allowedCountries.add("US");
        dataTypes = new HashMap<String, String>();
        dataTypes.put("GDP", "NY.GDP.MKTP.CD");
        dataTypes.put("FDI Inflows", "BX.KLT.DINV.WD.GD.ZS");
        dataTypes.put("FDI Outflows", "BM.KLT.DINV.WD.GD.ZS");
        dataTypes.put("Contribution To GDP", "NV.AGR.TOTL.ZS");
        dataTypes.put("Fertilizers", "AG.CON.FERT.ZS");
        dataTypes.put("Fertilizer Production", "AG.CON.FERT.PT.ZS");
        dataTypes.put("Reserves", "FI.RES.TOTL.MO");
        dataTypes.put("GNI", "FI.RES.TOTL.CD");
        dataTypes.put("Total Debt", "DT.TDS.DECT.GN.ZS");
        dataTypes.put("GNI Current", "NY.GNP.MKTP.CD");
    }

    public ArrayList<Data> getData(String type, String startYear, String endYear, String country,
                                   DataListener listener) {
        ArrayList<Data> dataList = new ArrayList<Data>();
        OkHttpClient client = new OkHttpClient();
        if (allowedCountries.contains(country) && dataTypes.containsKey(type)) {
            String url = "https://api.worldbank.org/v2/country/" + country +
                    "/indicator/" + dataTypes.get(type) + "?format=json&&date=" +
                    startYear + ":" + endYear+
                    "&per_page=100";
            System.out.println(url);
            Request request = new Request.Builder().url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String myResponse = response.body().string();
                        try {
                            JSONArray obj = new JSONArray(myResponse);
                            JSONArray list = obj.getJSONArray(1);
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject row = list.getJSONObject(i);
                                Data data = new Data(row.getString("date"),
                                        row.getLong("value"),
                                        row.getJSONObject("country").getString("id"));
                                dataList.add(data);
                            }
                            listener.onDataFinish(dataList);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
        return dataList;
    }
}
