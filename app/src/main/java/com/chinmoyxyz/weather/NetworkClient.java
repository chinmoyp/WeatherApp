//package com.chinmoyxyz.weather;
//
//
//        import android.content.Context;
//import android.util.Log;
//
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//
//import org.apache.http.Header;
//
//
//public class NetworkClient {
//
//    private static AsyncHttpClient client = new AsyncHttpClient();
//
//    private static final String TAG = "Dekkoh";
//
//
//    public static void autoComplete(Context context, RequestParams requestParams, final MainActivity mainActivity) {
//        client.get(context, "https://maps.googleapis.com/maps/api/place/autocomplete/json", requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//                StringBuilder jsonResults = new StringBuilder();
//                try {
//                    String responseJson = new String(responseBody);
//                    Log.d(TAG, "Success: Response:" + responseJson);
//                    Log.d(TAG, "Success: Response Code:" + statusCode);
//                    mainActivity.getPredictions(responseJson);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//            }
//        });
//    }
//
//
//
//}