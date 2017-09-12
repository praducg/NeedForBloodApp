package com.android.nfb.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.nfb.config.AmazonClientManager;
import com.android.nfb.config.DynamoDBManager;
import com.android.nfb.config.DynamoDBManager.AppContact;
import com.packtpub.dynamodb.cookbook.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends ListActivity {
	public static AmazonClientManager clientManager = null;
	String parentdId = null;

	private List<AppContact> contacts = null;
	private ArrayList<String> labels = null;

	private ArrayAdapter<String> arrayAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		clientManager = new AmazonClientManager(this);
		SharedPreferences sp = getSharedPreferences("addressbook",
				Activity.MODE_PRIVATE);
		parentdId = sp.getString("parentEmailId", "default");
		new GetContactsListTask().execute();

	}

	private class GetContactsListTask extends AsyncTask<Void, Void, Void> {

		protected Void doInBackground(Void... inputs) {

			labels = new ArrayList<String>();

			contacts = DynamoDBManager.getAllContacts(parentdId);

			for (AppContact con : contacts) {
				labels.add(con.getName() + " " + con.getEmail() + " "
						+ con.getPhone());
			}

			return null;
		}

		protected void onPostExecute(Void result) {

			arrayAdapter = new ArrayAdapter<String>(ContactListActivity.this,
					R.layout.contacts_list, labels);
			setListAdapter(arrayAdapter);
			ListView lv = getListView();
			lv.setBackgroundColor(Color.WHITE);

		}
	}

}
