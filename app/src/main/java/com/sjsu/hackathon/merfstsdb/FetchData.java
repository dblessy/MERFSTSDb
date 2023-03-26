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
    HashMap<String, String> tableNames;

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
        tableNames = new HashMap<String, String>();
        tableNames.put("GDP", "gdp");
        tableNames.put("FDI Inflows", "fdi_inflows");
        tableNames.put("FDI Outflows", "fdi_outflows");
        tableNames.put("Contribution To GDP", "con_gdp");
        tableNames.put("Fertilizers", "fertilizer");
        tableNames.put("Fertilizer Production", "fertilizer_prod");
        tableNames.put("Reserves", "reserves");
        tableNames.put("GNI", "gni");
        tableNames.put("Total Debt", "debt");
        tableNames.put("GNI Current", "gni_cur");
    }

    public void getData(DBHandler dbHandler, String type, String startYear, String endYear, String country,
                                   DataListener listener) {
        ArrayList<Data> dataList = new ArrayList<Data>();
        OkHttpClient client = new OkHttpClient();
        if (!allowedCountries.contains(country)) {
            listener.onDataFail("No such country");
        } else if (!dataTypes.containsKey(type)) {
            listener.onDataFail("No such type");
        } else {
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
                            String tableName = tableNames.get(type);//I changed "type" to type and it stopped crashing
                            dbHandler.removeData(tableName, startYear, endYear);
                            JSONArray obj = new JSONArray(myResponse);
                            JSONArray list = obj.getJSONArray(1);
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject row = list.getJSONObject(i);
                                String year = row.getString("date");
                                long data = row.getLong("value");
                                String country = row.getJSONObject("country").getString("id");
                                Data newData = new Data(year,
                                        data,
                                        country);
                                dataList.add(newData);
                                dbHandler.addNewData(tableName, year, data, country);
                            }
                            listener.onDataFinish(dataList);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }

    public void getOfflineData(DBHandler dbHandler, String type, String startYear, String endYear, String country,
                          DataListener listener) {
        if (!allowedCountries.contains(country)) {
            listener.onDataFail("No such country");
        } else if (!tableNames.containsKey(type)) {
            listener.onDataFail("No such type");
        } else {
            listener.onDataFinish(dbHandler.getData(tableNames.get("type"), startYear, endYear, country));
        }
    }
}
