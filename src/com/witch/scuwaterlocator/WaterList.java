package com.witch.scuwaterlocator;


import java.io.IOException;
import java.util.Comparator;

import com.example.scuwaterlocator.R;

import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.witch.scuwaterlocator.WaterFountain;


public class WaterList extends ListActivity implements OnItemSelectedListener  {
	private boolean hasGPS = false;
	private ArrayAdapter<WaterFountain> wf_adapter;
	double gps_lat = 0;
	double gps_lon = 0;
	WaterFountain myWF[] = {
			new WaterFountain("oconnor mens", 37.349812,-121.941576,3.0 ,4.0 ,4.0),
			new WaterFountain("oconnor 1st womens",37.350243, -121.941801,3.0 ,4.0 ,4.0),
			new WaterFountain("music and dance",-121.94242476,37.3500943,2.5 ,4.0 ,2.5),
			new WaterFountain("music dance past double doors next to ballet studio",-121.94253375,37.35005419,4.5 ,4.5 ,5.0),
			new WaterFountain("mayer theater downstairs",-121.94226231,37.3497626,0.0 ,0.0 ,0.0),
			new WaterFountain("outdoor near physics",-121.9413298,37.35051069,4.0 ,1.0 ,1.0),
			new WaterFountain("alumni sciences",-121.94099852,37.35072645,2.5 ,5.0 ,5.0),
			new WaterFountain("outside physics 2 drink the left one",-121.94115671,37.35044817,4.5 ,5.0 ,3.0),
			new WaterFountain("chem lab near ice machine",-121.94065417,37.35035143,4.0 ,4.0 ,3.5),
			new WaterFountain("arts and science near vending machines",-121.93983269,37.35032765,4.0 ,5.0 ,4.5),
			new WaterFountain("lucas hall cafe water",-121.93960433,37.35084238,5.0 ,5.0 ,5.0),
			new WaterFountain("bannan law a",-121.96360587,37.55630136,3.0 ,3.0 ,4.0),
			new WaterFountain("bannan law b",-121.93865847,37.34921172,3.5 ,4.0 ,4.0),
			new WaterFountain("bannan engineering",-121.93834251,37.34939573,3.5 ,4.0 ,3.0),
			new WaterFountain("dc 1st floor",-121.93831981,37.34854659,4.5 ,5.0 ,5.0),
			new WaterFountain("stadium",-121.93574131,37.34942562,3.0 ,1.5 ,2.0),
			new WaterFountain("stadium mens side",-121.93541752,37.348815,2.5 ,3.0 ,2.0),
			new WaterFountain("malley water",-121.93628806,37.3484327,3.0 ,4.0 ,3.5),
			new WaterFountain("library just use cafe water instead",-121.93848536,37.34813771,3.0 ,3.0 ,2.0),
			new WaterFountain("benson near bronco",-121.9391469,37.34790068,4.0 ,4.0 ,4.0),
			new WaterFountain("wellness center",-121.94085725,37.34728395,3.0 ,5.0 ,4.0),
			new WaterFountain("kenna mens side",-121.94010841,37.34841341,5.0 ,5.0 ,3.5),
			new WaterFountain("oconnor womens side",-121.939858,37.34845208,4.5 ,4.0 ,4.0),
			new WaterFountain("outside water fountain",-121.94084143,37.34833726,4.0 ,4.5 ,4.0),
			new WaterFountain("admissions",-121.9402413,37.34936553,3.0 ,3.0 ,3.0)
		};
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main_water_list);
	    
	    if (savedInstanceState!=null && savedInstanceState.containsKey("gps_lat")) {
	    	hasGPS = true;
	    	gps_lat = savedInstanceState.getDouble("gps_lat");
	    	gps_lon = savedInstanceState.getDouble("gps_lon");
	    }
	    // set the list adapter
	    wf_adapter = new ArrayAdapter<WaterFountain>(this, android.R.layout.simple_list_item_1, myWF);
	    setListAdapter(wf_adapter);
	    
	    wf_adapter.sort(new Comparator<WaterFountain>() {
	        public int compare(WaterFountain arg0, WaterFountain arg1) {
	            return Double.compare(arg0.getTaste(), arg1.getTaste());
	        }
	    });

	    setListAdapter(wf_adapter);
	    wf_adapter.notifyDataSetChanged();
	    
	    
	    Spinner spinner = (Spinner) findViewById(R.id.select_spinner);
		 // Create an ArrayAdapter using the string array and a default spinner layout
		 ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
		         R.array.sortby, android.R.layout.simple_spinner_item);
		 // Specify the layout to use when the list of choices appears
		 adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 // Apply the adapter to the spinner
		 spinner.setAdapter(adapter2);
		 spinner.setOnItemSelectedListener(this);
	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		   super.onListItemClick(l, v, position, id);
		   //int id = Integer.valueOf(((TextView)v.findViewById(R.id.post_Id)).getText().toString());
		   Log.v("hi","hi"+position);
		   // THE REST OF THE CODE GOES HERE
		   Intent returnIntent = new Intent();
		   //return the object back like this
		   //we need its rating
		   //its coordinates
		   //its name
		   WaterFountain selectedWF = myWF[position];
		   
		   returnIntent.putExtra("name",selectedWF.getName());
		   returnIntent.putExtra("lat",Double.toString(selectedWF.getLatitude()));
		   returnIntent.putExtra("long",Double.toString(selectedWF.getLongitude()));
		   returnIntent.putExtra("temp",Double.toString(selectedWF.getTemp()));
		   returnIntent.putExtra("pressure",Double.toString(selectedWF.getPress()));
		   returnIntent.putExtra("taste",Double.toString(selectedWF.getTaste()));
		   setResult(RESULT_OK,returnIntent);   
		   finish();
		}
	    // other code in your class ...

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (hasGPS == false && arg2 == 0) {
			Toast.makeText(this, "Nopes", Toast.LENGTH_LONG).show();
			return;
		}
		switch(arg2)
		{
		case 0: //this is for the distance
			// not yet implemented
		break;
		
		case 1: //this is for pressure
		 wf_adapter.sort(new Comparator<WaterFountain>() {
		        public int compare(WaterFountain arg0, WaterFountain arg1) {
		            return Double.compare(arg1.getPress(),arg0.getPress());
		        }
		    });
		break;
		
		case 2: //temperature
		 wf_adapter.sort(new Comparator<WaterFountain>() {
		        public int compare(WaterFountain arg0, WaterFountain arg1) {
		            return Double.compare(arg1.getTemp(),arg0.getTemp());
		        }
		    });
		break;
		
		case 3: //taste
		 wf_adapter.sort(new Comparator<WaterFountain>() {
		        public int compare(WaterFountain arg0, WaterFountain arg1) {
		            return Double.compare(arg1.getTaste(),arg0.getTaste());
		        }
		    });
		break;
		}
	   
		Log.v("waterlist",Integer.toString(arg2));
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
	    
