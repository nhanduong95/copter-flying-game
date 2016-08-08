package com.mygdx.game.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.GameExecution;
import com.mygdx.game.StaticValues;
import com.parse.Parse;
import com.parse.ParseObject;

public class AndroidLauncher extends AndroidApplication {
	public static ConnectivityManager connectivityManager;
	ActionResolverAndroid actionResolverAndroid;
	FileHandlerAndroid fileHandlerAndroid;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.enableLocalDatastore(this);

		connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		actionResolverAndroid = new ActionResolverAndroid(this);
		fileHandlerAndroid = new FileHandlerAndroid(this);
		Parse.initialize(this);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GameExecution(actionResolverAndroid, fileHandlerAndroid), config);

		StaticValues.SCORES_SAVED = new ParseObject("Highscore");
	}
}
