package com.wzgiceman.rxretrofitlibrary.retrofit_rx.http.cookie;

import android.content.Context;
 
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.utils.PreferencesUtils;

import java.io.IOException; 
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * This Interceptor add all received Cookies to the app DefaultPreferences.
 * Your implementation on how to save the Cookies on the Preferences MAY VARY.
 * <p>
 * Created by tsuharesu on 4/1/15.
 */
public class ReceivedCookiesInterceptor implements Interceptor {
  private Context context;
  public ReceivedCookiesInterceptor(Context context){
    this.context = context;
  }
  @Override
  public Response intercept(Chain chain) throws IOException {
    Response originalResponse = chain.proceed(chain.request());

    if (!originalResponse.headers("Set-Cookie").isEmpty()) {
      HashSet<String> cookies = new HashSet<>();

      for (String header : originalResponse.headers("Set-Cookie")) {
        cookies.add(header);
      }

      PreferencesUtils.putStringSet(context,PreferencesUtils.PREF_COOKIES, cookies);
    }

    return originalResponse;
  }
}