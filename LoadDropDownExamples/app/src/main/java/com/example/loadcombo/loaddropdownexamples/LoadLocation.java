package com.example.loadcombo.loaddropdownexamples;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by Pradeep on 09/207/2017.
 */
public class LoadLocation extends AppCompatActivity{
    JSONObject jsonCountry;
    JSONObject jsonState;
    JSONObject jsonCity;
    JSONObject jsonArea;
    JSONArray jsonarray;
    ProgressDialog mProgressDialog;
    ArrayList<String> worldlist;

    Spinner countrySpinner;
    Spinner stateSpinner;
    Spinner citySpinner;
    Spinner areaSpinner;
    Spinner bgrpSpinner;
    private String setComboValues;
    public String getSetComboValues() {
        return setComboValues;
    }

    public void setSetComboValues(String setComboValues) {
        this.setComboValues = setComboValues;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_location);
        new PopulateCountry().execute();

    }
    // Make First Web Service call
    private class PopulateCountry extends AsyncTask<Void, Void, Void> {
       ArrayList<WorldDetails> world = new ArrayList<WorldDetails>();
        @Override
        protected Void doInBackground(Void... params) {
            worldlist = new ArrayList<String>();
            countrySpinner = (Spinner)findViewById(R.id.country_spinner);
            jsonCountry = JSONfunctions
                    .getJSONfromURL("http://api.geonames.org/countryInfoJSON?username=praducg");
            try {
                jsonarray = jsonCountry.getJSONArray("geonames");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonCountry = jsonarray.getJSONObject(i);
                    WorldDetails worldpop = new WorldDetails();
                    worldpop.setCountry(jsonCountry.optString("countryName"));
                    worldpop.setGeoNameId(jsonCountry.optString("geonameId"));
                    world.add(worldpop);
                    worldlist.add(jsonCountry.optString("countryName"));


                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
       @Override
        protected void onPostExecute(Void args) {
           countrySpinner
                    .setAdapter(new ArrayAdapter<String>(LoadLocation.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            worldlist));

            // Spinner on item click listener
           countrySpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                           // String result=world.get(position).getGeoNameId();
                            setSetComboValues(world.get(position).getGeoNameId());
                            new PopulateState().execute();
                            //new DownloadStateJSON().execute(world.get(position).getGeoNameId());

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }

    //Make 2nd web service call

    private class PopulateState extends AsyncTask< Void, Void, Void> {
        ArrayList<WorldDetails> world = new ArrayList<WorldDetails>();
        @Override
        protected Void doInBackground(Void ... params) {


            worldlist = new ArrayList<String>();
            stateSpinner = (Spinner)findViewById(R.id.state_spinner);
                Integer geonameId= Integer.parseInt(getSetComboValues());

            jsonState = JSONfunctions
                    .getJSONfromURL("http://api.geonames.org/childrenJSON?geonameId="+geonameId+"&username=praducg");
            try {
                // Locate the NodeList name
                jsonarray = jsonState.getJSONArray("geonames");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonState = jsonarray.getJSONObject(i);
                    WorldDetails worldpop = new WorldDetails();
                    worldpop.setGeoNameId(jsonState.optString("geonameId"));
                    worldpop.setCountry(jsonCountry.optString("toponymName"));
                    world.add(worldpop);
                    worldlist.add(jsonState.optString("toponymName"));
                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
            //Spinner mySpinner = (Spinner) findViewById(R.id.country_spinner);

            // Spinner adapter
            stateSpinner
                    .setAdapter(new ArrayAdapter<String>(LoadLocation.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            worldlist));

            // Spinner on item click listener
            stateSpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                           // String result=world.get(position).getGeoNameId();
                            setSetComboValues(world.get(position).getGeoNameId());
                            new PopulateCity().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }

    //Make 3rd Web Service call
    private class PopulateCity extends AsyncTask< Void, Void, Void> {
        ArrayList<WorldDetails> world = new ArrayList<WorldDetails>();
        @Override
        protected Void doInBackground(Void... params) {

         //   ArrayList<WorldDetails> world = new ArrayList<WorldDetails>();
            worldlist = new ArrayList<String>();
            citySpinner = (Spinner)findViewById(R.id.city_spinner);
            Integer geonameId= Integer.parseInt(getSetComboValues());
            jsonCity = JSONfunctions
                    .getJSONfromURL("http://api.geonames.org/childrenJSON?geonameId="+geonameId+"&username=praducg");
            try {
                // Locate the NodeList name
                jsonarray = jsonCity.getJSONArray("geonames");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonCity = jsonarray.getJSONObject(i);
                    WorldDetails worldpop = new WorldDetails();
                    worldpop.setGeoNameId(jsonCity.optString("geonameId"));
                    world.add(worldpop);
                    worldlist.add(jsonCity.optString("toponymName"));


                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
            Spinner mySpinner = (Spinner) findViewById(R.id.country_spinner);

            // Spinner adapter
            citySpinner
                    .setAdapter(new ArrayAdapter<String>(LoadLocation.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            worldlist));

            // Spinner on item click listener
            citySpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            setSetComboValues(world.get(position).getGeoNameId());
                            new PopulateRegion().execute();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }


    //Make 4th web service call

    private class PopulateRegion extends AsyncTask< Void, Void, Void> {
        ArrayList<WorldDetails> world = new ArrayList<WorldDetails>();
        @Override
        protected Void doInBackground(Void... params) {

            //   ArrayList<WorldDetails> world = new ArrayList<WorldDetails>();
            worldlist = new ArrayList<String>();
            // countrySpinner = (Spinner)findViewById(R.id.country_spinner);
            //stateSpinner = (Spinner)findViewById(R.id.state_spinner);
            areaSpinner = (Spinner)findViewById(R.id.area_spinner);
            Integer geonameId= Integer.parseInt(getSetComboValues());
            jsonArea = JSONfunctions
                    .getJSONfromURL("http://api.geonames.org/childrenJSON?geonameId="+geonameId+"&username=praducg");
            try {
                // Locate the NodeList name
                jsonarray = jsonArea.getJSONArray("geonames");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonArea = jsonarray.getJSONObject(i);
                    WorldDetails worldpop = new WorldDetails();
                    worldpop.setGeoNameId(jsonArea.optString("geonameId"));
                    world.add(worldpop);
                    worldlist.add(jsonArea.optString("toponymName"));
                }

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml
          //  Spinner mySpinner = (Spinner) findViewById(R.id.area_spinner);

            // Spinner adapter
            areaSpinner
                    .setAdapter(new ArrayAdapter<String>(LoadLocation.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            worldlist));

            // Spinner on item click listener
            areaSpinner
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            setSetComboValues(world.get(position).getGeoNameId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }





}
