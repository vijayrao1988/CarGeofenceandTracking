package in.ac.iitd.cargeofenceandtracking;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import in.ac.iitd.cargeofenceandtracking.RowItem;
import in.ac.iitd.cargeofenceandtracking.CustomBaseAdapter;
import in.ac.iitd.cargeofenceandtracking.R;

public class ImageTextListBaseAdapterActivity extends Activity implements
        OnItemClickListener {

    public static final String[] titles = new String[] { "Geofence1",
            "Geofence2", "Geofence3", "Geofence4" };

    public static final String[] titlesCars = new String[] { "Car1",
            "Car2", "Car3", "Car4" };

    public static final String[] titlesGeofences = new String[] { "Geofence1",
            "Geofence2", "Geofence3", "Geofence4" };

    public static final String[] descriptions = new String[] {
            "Circle: Radius = 3km",
            "Square: Side = 6km",
            "Circle: Radius = 5km",
            "Custom: Area = 34 sq.km." };

    public static final Integer[] images = { R.drawable.ic_menu_camera,
            R.drawable.ic_menu_gallery, R.drawable.ic_menu_send, R.drawable.ic_menu_slideshow };

    ListView listView;
    List<RowItem> rowItems;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listofgeofences);

        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.listofgeofences);
        CustomBaseAdapter adapter = new CustomBaseAdapter(this, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Item " + (position + 1) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }
}