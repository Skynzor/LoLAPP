package skynzor.lolapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.GridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Primularose on 16-1-2018.
 */

public class GetRiotJson {

    private GridView gridView;
    private Context context;
    private ArrayList<String> listChampNames = new ArrayList();
    private ArrayList<Integer> listChampIds= new ArrayList();

    public GetRiotJson(Context context, GridView gridView) {
        this.gridView = gridView;
        this.context = context;
    }

    public String loadJSON(String filename) {
        String json = null;
        JSONObject parentObject = null;
        String champName = null;
        String champId = null;
        JSONObject champIdInfo = null;

        try {
            AssetManager assetManager = context.getAssets();
            InputStream is = assetManager.open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public void getRiotJSON(String jsonFilename) {
        String json = null;
        JSONObject parentObject = null;
        String name = null;
        String id = null;
        JSONObject idInfo = null;
        int drawableId = 0;

        try {
            json = loadJSON(jsonFilename);
            JSONObject o = new JSONObject(json.trim());
            parentObject = o.getJSONObject("data");
            Iterator<?> keys = parentObject.keys();

            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( parentObject.get(key) instanceof JSONObject ) {
                    idInfo = parentObject.getJSONObject(key);
                    name = idInfo.getString("name");
                    id = idInfo.getString("id");
                    if(jsonFilename == "champlistJSON.json")
                    {
                        drawableId = getImage("champion" + id);
                    }
                    if(jsonFilename == "itemlistJSON.json")
                    {
                        drawableId = getImage("item" + id);
                    }
                    }
                    if(drawableId != 0)
                    {
                        listChampNames.add(name);
                        listChampIds.add(drawableId);
                    }
                }

            String[] namesArray = new String[listChampNames.size()];
            namesArray = listChampNames.toArray(namesArray);

            Integer[] imgInt = new Integer[listChampIds.size()];
            imgInt = listChampIds.toArray(imgInt);

            GridAdapter gridAdapter = new GridAdapter(context, imgInt, namesArray);
            gridView.setAdapter(gridAdapter);

        }   catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }
}
