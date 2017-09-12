package com.android.nfb.config;

import android.content.Context;
import android.util.Log;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.android.nfb.constants.Constants;

/**
 * This class is used to get clients to the various AWS services. Before
 * accessing a client the credentials should be checked to ensure validity.
 */
public class AmazonClientManager {

	private static final String LOG_TAG = "AmazonClientManager";

	private AmazonDynamoDBClient ddb = null;
	private Context context;

	public AmazonClientManager(Context context) {
		this.context = context;
	}

	public AmazonDynamoDBClient ddb() {
		validateCredentials();
		return ddb;
	}

	public boolean hasCredentials() {
		return (!(Constants.ACCESS_KEY.equalsIgnoreCase("877766530587")
				|| Constants.IDENTITY_POOL_ID
						.equalsIgnoreCase("ap-south-1:267c3de4-0f06-4b2d-9b90-10c77215fbad") || Constants.UNAUTH_ROLE_ARN
					.equalsIgnoreCase("arn:aws:iam::877766530587:role/Cognito_NeedForBloodAppUnauth_Role")));
	}

	public void validateCredentials() {

		if (ddb == null) {
			initClients();
		}
	}

	private void initClients() {
		/*CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
				context, Constants.ACCESS_KEY, Constants.IDENTITY_POOL_ID,
				Constants.UNAUTH_ROLE_ARN, null, Regions.AP_SOUTH_1);*/
		CognitoCachingCredentialsProvider credentials = new CognitoCachingCredentialsProvider(
				context,
				Constants.IDENTITY_POOL_ID,
				Regions.AP_SOUTH_1);
		ddb = new AmazonDynamoDBClient(credentials);
		ddb.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
	}

	public boolean wipeCredentialsOnAuthError(AmazonServiceException ex) {
		Log.e(LOG_TAG, "Error, wipeCredentialsOnAuthError called" + ex);
		if (ex.getErrorCode().equals("IncompleteSignature")
				|| ex.getErrorCode().equals("InternalFailure")
				|| ex.getErrorCode().equals("InvalidClientTokenId")
				|| ex.getErrorCode().equals("OptInRequired")
				|| ex.getErrorCode().equals("RequestExpired")
				|| ex.getErrorCode().equals("ServiceUnavailable")

				|| ex.getErrorCode().equals("AccessDeniedException")
				|| ex.getErrorCode().equals("IncompleteSignatureException")
				|| ex.getErrorCode().equals(
						"MissingAuthenticationTokenException")
				|| ex.getErrorCode().equals("ValidationException")
				|| ex.getErrorCode().equals("InternalFailure")
				|| ex.getErrorCode().equals("InternalServerError")) {

			return true;
		}

		return false;
	}
}
