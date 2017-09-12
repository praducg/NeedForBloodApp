package com.donar.nfb.donarregistration.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.donar.nfb.donarregistration.R;
import com.donar.nfb.donarregistration.config.AmazonClientManager;
import com.donar.nfb.donarregistration.config.DynamoDBManager;
import com.donar.nfb.donarregistration.config.DynamoDBManager.DonarRegistration;

public class ContactActivity extends Activity {
	public static AmazonClientManager clientManager = null;
	private static final String TAG = "ContactActivity";
	EditText fname;
	EditText lname;
	EditText uname;
	EditText email;
	EditText phone;

	DonarRegistration contact = new DonarRegistration();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);
		clientManager = new AmazonClientManager(this);

	}

	public void onClickAddNew(View v) {
		Log.d(TAG, "Saving Contact");
		email = (EditText) findViewById(R.id.contact_email);
		fname = (EditText) findViewById(R.id.contact_fname);
		lname = (EditText) findViewById(R.id.contact_lname);
		uname = (EditText) findViewById(R.id.contact_uname);
		phone = (EditText) findViewById(R.id.contact_phone);
		SharedPreferences sp = getSharedPreferences("DonarApp",
				Activity.MODE_PRIVATE);
		String parentdId = sp.getString("username", "default");
		contact.setEmail(email.getText().toString());
		contact.setFirstName(email.getText().toString());
		contact.setLastName(lname.getText().toString());
		contact.setPhone(Integer.parseInt(phone.getText().toString()));
		contact.setUsername(parentdId);
		new DynamoDBManagerTask().execute(DynamoDBManagerType.SAVE_CONTACT);

	}

	private class DynamoDBManagerTask extends
			AsyncTask<DynamoDBManagerType, Void, DynamoDBManagerTaskResult> {

		protected DynamoDBManagerTaskResult doInBackground(
				DynamoDBManagerType... types) {

			DynamoDBManagerTaskResult result = new DynamoDBManagerTaskResult();

			result.setTaskType(types[0]);

			if (types[0] == DynamoDBManagerType.SAVE_CONTACT) {

				DynamoDBManager.addNewContact(contact);

			}

			return result;
		}

		protected void onPostExecute(DynamoDBManagerTaskResult result) {

			if (result.getTaskType() == DynamoDBManagerType.SAVE_CONTACT) {

				Toast.makeText(ContactActivity.this,
						"Contact Saved Successfully !!", Toast.LENGTH_SHORT)
						.show();
				redirectToActivity(null, ContactListActivity.class);
			}
		}
	}

	public void redirectToActivity(View view, Class<?> cls) {
		Intent intent = new Intent(this, cls);
		startActivity(intent);
	}

	private enum DynamoDBManagerType {
		SAVE_CONTACT
	}

	private class DynamoDBManagerTaskResult {
		private DynamoDBManagerType taskType;

		public DynamoDBManagerType getTaskType() {
			return taskType;
		}

		public void setTaskType(DynamoDBManagerType taskType) {
			this.taskType = taskType;
		}

	}
}
