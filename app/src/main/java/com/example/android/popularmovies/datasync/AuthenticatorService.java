package com.example.android.popularmovies.datasync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.net.Authenticator;

/**
 * Used from  https://developer.android.com/training/sync-adapters/creating-authenticator.html
 */
public class AuthenticatorService extends Service {
    public final String LOG_TAG = AuthenticatorService.class.getSimpleName();

    private AuthenticatorStub mAuthenticator;
    @Override
    public void onCreate() {
        Log.d(LOG_TAG,"In onCreate");
        // Create a new authenticator object
        mAuthenticator = new AuthenticatorStub(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

}
