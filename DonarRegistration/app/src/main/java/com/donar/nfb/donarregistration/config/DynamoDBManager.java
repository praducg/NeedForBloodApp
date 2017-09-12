package com.donar.nfb.donarregistration.config;

import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAutoGeneratedKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedParallelScanList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.donar.nfb.donarregistration.activity.ContactActivity;
import com.donar.nfb.donarregistration.activity.ContactListActivity;
import com.donar.nfb.donarregistration.activity.LoginActivity;
import com.donar.nfb.donarregistration.activity.RegisterActivity;
import com.donar.nfb.donarregistration.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class DynamoDBManager {

	private static final String TAG = "DynamoDBManager";

	public static void register(DonarLogin appUser) {
		AmazonDynamoDBClient ddb = RegisterActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {

			Log.d(TAG, "Registering user");
			mapper.save(appUser);
			Log.d(TAG, "User inserted");

		} catch (AmazonServiceException ex) {
			Log.e(TAG, "Error inserting users");
			RegisterActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}
	}

	public static void addNewContact(DonarRegistration appContact) {
		AmazonDynamoDBClient ddb = ContactActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {

			Log.d(TAG, "Adding new contact");
			mapper.save(appContact);
			Log.d(TAG, "Contact saved");

		} catch (AmazonServiceException ex) {
			Log.e(TAG, "Error adding new contact");
			ContactActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}
	}

	public static List<DonarRegistration> getAllContacts(String email) {
		AmazonDynamoDBClient ddb = ContactListActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);
		List<DonarRegistration> contactList = new ArrayList<DonarRegistration>();

		try {

			Log.d(TAG, "Fetching contacts");
			// Condition to get records for given userEmailId
			Condition condition = new Condition().withComparisonOperator(
					ComparisonOperator.EQ.toString()).withAttributeValueList(
					new AttributeValue().withS(email));
			DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
			scanExpression.addFilterCondition("parentId", condition);

			// Paginated scan in case no. of contacts are too much
			PaginatedParallelScanList<DonarRegistration> contacts = mapper
					.parallelScan(DonarRegistration.class, scanExpression, 4);
			for (DonarRegistration contact : contacts) {
				contactList.add(contact);
			}

		} catch (AmazonServiceException ex) {
			Log.e(TAG, "Error adding new contact");
			ContactListActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}
		return contactList;
	}

	public static DonarLogin getUserByUserName(String username) {

		AmazonDynamoDBClient ddb = LoginActivity.clientManager.ddb();
		DynamoDBMapper mapper = new DynamoDBMapper(ddb);

		try {
			DonarLogin appUser = mapper.load(DonarLogin.class, username);

			return appUser;

		} catch (AmazonServiceException ex) {
			LoginActivity.clientManager.wipeCredentialsOnAuthError(ex);
		}

		return null;
	}
	@DynamoDBTable(tableName = Constants.DONAR_LOGIN)
	public static class DonarLogin {

		private String email;
		private String username;
		private String password;

		@DynamoDBHashKey
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@DynamoDBHashKey
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		@DynamoDBAttribute(attributeName = "password")
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

	}

	@DynamoDBTable(tableName = Constants.DONAR_REGISTRATION)
	public static class DonarRegistration {



		private String email;
		private String firstName;
		private String lastName;
		private String username;
		private Integer phone;

		private Integer id;

		@DynamoDBHashKey(attributeName = "id")
		@DynamoDBAutoGeneratedKey
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}
		@DynamoDBAttribute(attributeName = "phone")
		public Integer getPhone() {
			return phone;
		}

		public void setPhone(Integer phone) {
			this.phone = phone;
		}
		@DynamoDBHashKey
		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		@DynamoDBAttribute(attributeName = "username")
		@DynamoDBRangeKey
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		@DynamoDBAttribute(attributeName = "firstname")
		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		@DynamoDBAttribute(attributeName = "lastname")
		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

	}

}
