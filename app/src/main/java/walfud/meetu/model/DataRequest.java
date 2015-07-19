package walfud.meetu.model;

import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import walfud.meetu.MeetUApplication;
import walfud.meetu.Utils;

/**
 * Created by song on 2015/7/6.
 */
public class DataRequest {

    public static final String TAG = "DataSender";

    public static final int ERROR_UNKNOWN = 0;
    public interface OnDataRequestListener {
        void onNoFriendNearby();

        void onFoundFriends(List<Data> nearbyFriendList);

        void onError(int errorCode);
    }

    private Data mData;
    private OnDataRequestListener mListener;

    public DataRequest(Data data, OnDataRequestListener listener) {
        mData = data;
        mListener = listener;
        mOnHttpPostResponse = new Utils.OnHttpPostResponse() {
            @Override
            public void onResponse(String response) {

                // Parse server response
                // <beans>
                //     <list>
                //         <locationCurr>
                //             <imei>imei001</imei>
                //             <longitude>50.0</longitude>
                //             <latitude>10.0</latitude>
                //             <uploadTime>2015-07-18 21:52:47</uploadTime>
                //         </locationCurr>
                //     </list>
                // </beans>
                String strXmlResponse = "<beans><list><locationCurr><imei>imei002</imei><longitude>50.0</longitude><latitude>10.0</latitude><uploadTime>2015-07-18 21:52:47</uploadTime></locationCurr></list></beans>";

                List<Data> friendsList = new ArrayList<>();

                try {
                    ObjectMapper jsonMapper = new ObjectMapper();
                    jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

                    JSONObject beansNode = XML.toJSONObject(strXmlResponse);
                    JSONObject listNode = beansNode.getJSONObject("beans");
                    Object locationCurrNode = listNode.get("list");
                    if (locationCurrNode instanceof JSONObject) {
                        // Find friend(s) nearby
                        Object locationCurrValue = ((JSONObject) locationCurrNode).get("locationCurr");

                        if (locationCurrValue instanceof JSONObject) {

                            // Single record
                            Data data = jsonMapper.readValue(locationCurrValue.toString(), Data.class);
                            friendsList.add(data);

                        } else if (locationCurrValue instanceof JSONArray) {

                            // Array record
                            JSONArray locationCurrValueArray = (JSONArray) locationCurrValue;
                            for (int i = 0; i < locationCurrValueArray.length(); i++) {
                                JSONObject locationCurr = (JSONObject) locationCurrValueArray.get(i);

                                Data data = jsonMapper.readValue(locationCurr.toString(), Data.class);
                                friendsList.add(data);
                            }

                        } else {

                            // Error
                            mListener.onError(ERROR_UNKNOWN);
                        }
                    } else {
                        // No friend nearby
                        mListener.onNoFriendNearby();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mListener.onFoundFriends(friendsList);
            }
        };
    }

    private Utils.OnHttpPostResponse mOnHttpPostResponse;

    public boolean send() {
        String httpRequest = toUrlRequest();

        Utils.httpPost(httpRequest, mOnHttpPostResponse);

        return true;
    }

    // http://45.55.4.64:8080/meetu/userAction!meetu?imei=imei001&longitude=50.000000&latitude=10.000000
    private static final String URL_FMT = "http://45.55.4.64:8080/meetu/userAction!meetu?imei=%s&longitude=%.6f&latitude=%.6f";

    public String toUrlRequest() {
        return String.format(URL_FMT,
                mData.getImei(), mData.getLongitude(), mData.getLatitude());
    }
}
