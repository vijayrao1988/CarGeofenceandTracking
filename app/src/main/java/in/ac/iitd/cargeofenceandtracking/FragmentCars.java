package in.ac.iitd.cargeofenceandtracking;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static in.ac.iitd.cargeofenceandtracking.ImageTextListBaseAdapterActivity.descriptions;
import static in.ac.iitd.cargeofenceandtracking.ImageTextListBaseAdapterActivity.titles;
import static in.ac.iitd.cargeofenceandtracking.ImageTextListBaseAdapterActivity.titlesCars;

public class FragmentCars extends ListFragment implements OnItemClickListener {
    public CustomBaseAdapter listOfCars;

    List<RowItem> rowItems;
    ListView listView;
    public static final Integer[] images = { R.mipmap.ic_cars_suv,
            R.mipmap.ic_cars_suv, R.mipmap.ic_cars_suv, R.mipmap.ic_cars_suv };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cars, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(images[i], titlesCars[i], descriptions[i]);
            rowItems.add(item);
        }

        CustomBaseAdapter adapter = new CustomBaseAdapter(getActivity(), rowItems);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(, R.array.Planets, android.R.layout.simple_list_item_1);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);


        //listView = (ListView) findViewById(R.id.listofgeofences);
        //listView.setAdapter(adapter);
        //listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }
}