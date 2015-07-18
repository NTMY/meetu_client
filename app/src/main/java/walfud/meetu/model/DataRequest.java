package walfud.meetu.model;

import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import walfud.meetu.MeetUApplication;
import walfud.meetu.Utils;

/**
 * Created by song on 2015/7/6.
 */
public class DataRequest {

    public static final String TAG = "DataSender";

    public interface OnRequestListener {
        void onNoFriendNearby();

        void onFoundFriends(List<Data> nearbyFriendList);

        void onError(int errorCode);
    }

    private Data mData;
    private OnRequestListener mListener;
    public DataRequest(Data data, OnRequestListener listener) {
        mData = data;
        mListener = listener;
        mOnHttpPostResponse = new Utils.OnHttpPostResponse() {
            @Override
            public void onResponse(String response) {
                // Parse server response
//                <beans>
//                  <list>
//                      <locationCurr>
//                          <imei>12345678</imei>
//                          <longitude>50.000000</longitude>
//                          <latitude>10.000000</latitude>
//                      </locationCurr>
//                  </list>
//                </beans>
                ObjectMapper jsonMapper = new ObjectMapper();
//                XmlMapper xmlMapper = new XmlMapper();

                String xml = "<beans><list><locationCurr><imei>imei002</imei><longitude>50.0</longitude><latitude>10.0</latitude></locationCurr></list></beans>";
                try {
//                    List<Data> dataList = xmlMapper.readValue(xml, List.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(MeetUApplication.getContext(), String.format("response(%s)",
                                response),
                        Toast.LENGTH_SHORT).show();

                mWaiterSend.notify();
            }
        };
    }

    private Utils.OnHttpPostResponse mOnHttpPostResponse;
    private Object mWaiterSend = new Object();
    public boolean send(boolean async) {
        String httpRequest = toUrlRequest();

        Toast.makeText(MeetUApplication.getContext(), String.format("request(%s)",
                        httpRequest),
                Toast.LENGTH_SHORT).show();

        Utils.httpPost(httpRequest, mOnHttpPostResponse);

        if (!async) {
            try {
                mWaiterSend.wait();

//                return
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    // http://192.168.1.4:8080/meetu/userAction!meetu?imei=imei001&longitude=50.000000&latitude=10.000000
    private static final String URL_FMT = "http://45.55.4.64:8080/meetu/userAction!meetu?imei=%s&longitude=%.6f&latitude=%.6f";
    public String toUrlRequest() {
        return String.format(URL_FMT,
                mData.getId(), mData.getLongitude(), mData.getLatitude());
    }
}
