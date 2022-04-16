package dps924.assignment4.data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dps924.assignment4.FirstFragment;
import dps924.assignment4.MainActivity;
import dps924.assignment4.R;
import dps924.assignment4.RecipeView;
import dps924.assignment4.ui.browse.BrowseFragment;
import dps924.assignment4.ui.create.CreateFragment;
import dps924.assignment4.ui.saved.SavedFragment;

public class DataService extends Service {

    private SavesDatabase database;
    private ExecutorService dbExecutor = Executors.newFixedThreadPool(3);

    private Context context;

    private final String API_ROOT_URL = "https://api.spoonacular.com/";
    private final String API_KEY_PRIMARY = "2fa953be9058495c8061f04d871144fa";
    private final String API_KEY_FALLBACK = "1febd37b3f8e478eab12e086f470071d";


    private void doAsyncRequestHTTP(String path, HashMap<String, String> queries, String id) {
        if (queries == null)
            queries = new HashMap<>();
        else
            queries.put("apiKey", API_KEY_FALLBACK);

        StringBuilder parameters = new StringBuilder();
        if (queries.size() > 0) {
            parameters.append("?");
            for (Map.Entry<String, String> entry : queries.entrySet()) {
                try {
                    parameters.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    parameters.append("=");
                    parameters.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    parameters.append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!path.startsWith("http"))
            new Task().execute(API_ROOT_URL, path, parameters.toString());
        else
            new Task().execute(id, path, parameters.toString());
    }

    public DataService(Context theContext) {
        context = theContext;
        if (database == null)
            database = SavesDatabase.getInstance(context);
    }

    public void getRecipesAsync(String query) {
        System.out.println("NETWORK:\n\t- getRecipesAsync...");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("query", query);
        parameters.put("sort", "popularity");
        doAsyncRequestHTTP("recipes/complexSearch", parameters, "");
    }

    public void getRecipeByIdAsync(int id) {
        System.out.println("NETWORK:\n\t- getRecipeByIdAsync...");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("includeNutrition", "false");
        doAsyncRequestHTTP("recipes/" + Math.abs(id) + "/information", parameters, "");
    }

    public void getRandomRecipe() {
        System.out.println("NETWORK:\n\t- getRandomRecipe...");
        Intent intent = new Intent(MainActivity.AppContext, RecipeView.class);
        intent.putExtra(MainActivity.AppContext.getResources().getString(R.string.recipeID), (int)(Math.ceil(Math.random() * 1000) + 1) * -1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainActivity.AppContext.startActivity(intent);
    }

    public void getAutocompleteAsync(String query) {
        System.out.println("NETWORK:\n\t- getAutocompleteAsync...");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("query", query);
        parameters.put("number", "5");
        doAsyncRequestHTTP("recipes/autocomplete", parameters , "");
    }

    public void getImageAsync(String url, String id) {
        System.out.println("NETWORK:\n\t- getImageAsync...");
        doAsyncRequestHTTP(url, null, id);
    }

    public boolean saveRecipe(Recipe recipe) {
        System.out.println("DATABASE:\n\t- saveRecipe...");
        try {
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    database.saveDao().insertSave(new SavedRecipe(recipe));
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getRecipe(int index) {
        System.out.println("DATABASE:\n\t- getRecipe...");
        try {
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    SavedRecipe savedRecipe = database.saveDao().getSave(Math.abs(index));
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            CreateFragment.updateFragment(savedRecipe);
                        }
                    };
                    mainHandler.post(myRunnable);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getAllRecipes() {
        System.out.println("DATABASE:\n\t- getAllRecipes...");
        try {
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ArrayList<SavedRecipe> saves = new ArrayList<>(database.saveDao().getSavedList());
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            SavedFragment.updateFragment(saves);
                        }
                    };
                    mainHandler.post(myRunnable);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRecipe(SavedRecipe recipe) {
        System.out.println("DATABASE:\n\t- updateRecipe...");
        try {
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    database.saveDao().updateSave(recipe);
                    /*Handler mainHandler = new Handler(Looper.getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            for (int index = 0; index < SavedFragment.lastResults.size(); index++) {
                                if (SavedFragment.lastResults.get(index).id == recipe.id) {
                                    SavedFragment.lastResults.set(index, recipe);
                                    SavedFragment.updateFragment(SavedFragment.lastResults);
                                    return;
                                }
                            }

                        }
                    };
                    mainHandler.post(myRunnable);*/
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*public boolean deleteRecipe(SavedRecipe recipe) {
        System.out.println("DATABASE:\n\t- deleteRecipe...");
        try {
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    database.saveDao().deleteSave(recipe);
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/

    public boolean clearRecipes() {
        System.out.println("DATABASE:\n\t- clearRecipes...");
        try {
            dbExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    database.saveDao().deleteAll();
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        //return null;
    }

    private class Task extends AsyncTask<String, Void, Void> {

        //Task() {
            // TODO: Manually request internet permissions if not already given
        //}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // TODO: Loading spinner stuff here?

        }

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL((params[0] != API_ROOT_URL ? "" : params[0]) + params[1] + params[2]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestMethod("GET");
                System.out.println("Sending HTTP Request:\n\t" + connection.getURL());

                int status = connection.getResponseCode();
                Reader streamReader = null;

                if (status == HttpURLConnection.HTTP_PAYMENT_REQUIRED) { // API KEY OVERUSED
                    System.out.println("API limit reached...\n\tUsing backup key and retrying!");
                    if (params[2] == API_KEY_PRIMARY)
                        new Task().execute(params[0], params[1], params[2].replace(API_KEY_PRIMARY, API_KEY_FALLBACK));
                    else
                        System.out.println("YIKES:\n\tFallback key failed too!!");
                    return null;
                }
                if (status >= HttpURLConnection.HTTP_MULT_CHOICE) // Failed call
                    streamReader = new InputStreamReader(connection.getErrorStream());
                else if (params[0] != API_ROOT_URL) { // Getting image
                    Bitmap bmp = BitmapFactory.decodeStream(connection.getInputStream());
                    int index = 0;
                    if (params[0] != "" && params[0].length() > 1)
                        try {
                            index = Integer.parseInt(params[0].substring(1));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            return null;
                        }
                    else
                        return null;
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    if (params[0].charAt(0) == '-' || params[0].charAt(0) == '+')
                        if (index > 0)
                            index--;
                        else if (index < 0)
                            index++;
                    int finalIndex = index;
                    System.out.println("GOT RESPONSE:\n\tSubmitting to index of: " + params[0].charAt(0) + finalIndex);
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() { // New UI task for main thread
                            if (params[0].charAt(0) == '-')
                                BrowseFragment.updateFragment(bmp, finalIndex); // Update browser list
                            else if (params[0].charAt(0) == '=')
                                FirstFragment.updateFragment(bmp); // Update fetched recipe
                            else if (params[0].charAt(0) == '+')
                                SavedFragment.updateFragment(bmp, finalIndex); // Update saves list
                            else
                                CreateFragment.updateFragment(bmp); // Update created recipe
                        }
                    };
                    mainHandler.post(myRunnable);
                    return null;
                } else
                    streamReader = new InputStreamReader(connection.getInputStream());

                BufferedReader in = new BufferedReader(streamReader);
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null)
                    content.append(inputLine);
                in.close();

                System.out.println("GOT RESPONSE:\n\t" + content.toString());

                if (status < HttpURLConnection.HTTP_MULT_CHOICE)
                    switch (params[1]) {
                        case "recipes/complexSearch": {
                                JSONObject jsonObject = new JSONObject(content.toString());
                                RecipeBrowserResults results = new RecipeBrowserResults(jsonObject);
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() { // New UI task for main thread
                                        BrowseFragment.updateFragment(results);
                                    }
                                };
                                mainHandler.post(myRunnable);
                            }
                            break;

                        case "recipes/autocomplete": {
                                JSONArray jsonArray = new JSONArray(content.toString());
                                ArrayList<RecipeBase> suggestions = new ArrayList<>();
                                for (int index = 0; index < jsonArray.length(); index++) {
                                    JSONObject item = jsonArray.getJSONObject(index);
                                    suggestions.add(new RecipeBase(item));
                                }
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() { // New UI task for main thread
                                        BrowseFragment.updateAutoComplete(suggestions);
                                    }
                                };
                                mainHandler.post(myRunnable);
                            }
                            break;

                        default: {
                            // Get by ID, including random
                                JSONObject jsonObject = new JSONObject(content.toString());
                                Recipe recipe = new Recipe(jsonObject);
                                // TODO: Test if recipe (id usually) is valid before intenting
                                Handler mainHandler = new Handler(Looper.getMainLooper());
                                Runnable myRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        RecipeView.recipe = new SavedRecipe(recipe);
                                        FirstFragment.updateFragment(RecipeView.recipe);
                                    }
                                };
                                mainHandler.post(myRunnable);
                            }
                            break;
                    }
                else
                    Snackbar.make(((Activity) getApplicationContext()).getCurrentFocus(), MainActivity.AppContext.getResources().getString(R.string.recipe_saved), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}