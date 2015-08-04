package walfud.meetu.model;

/**
 * Created by song on 2015/7/6.
 */
public class DataRequest {

    public static final String TAG = "DataSender";
//
//    private Data mData;
//    private OnDataRequestListener mListener;
//
//    public DataRequest(Data data, OnDataRequestListener listener) {
//        mData = data;
//        mListener = listener;
//        mOnHttpPostResponse = new Utils.OnHttpPostResponse() {
//            @Override
//            public void onResponse(String response) {
//
//                Log.d(TAG, String.format("`DataRequest.onResponse` response(%s)", response));
//
//                // Parse server response
//                // <beans>
//                //     <list>
//                //         <locationCurr>
//                //             <imei>imei001</imei>
//                //             <longitude>50.0</longitude>
//                //             <latitude>10.0</latitude>
//                //             <uploadTime>2015-07-18 21:52:47</uploadTime>
//                //         </locationCurr>
//                //     </list>
//                // </beans>
////                String strXmlResponse = "<beans><list><locationCurr><imei>imei002</imei><longitude>50.0</longitude><latitude>10.0</latitude><uploadTime>2015-07-18 21:52:47</uploadTime></locationCurr></list></beans>";
//
//                List<Data> friendsList = new ArrayList<>();
//
//                try {
//                    ObjectMapper jsonMapper = new ObjectMapper();
//                    jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//
//                    JSONObject beansNode = XML.toJSONObject(response);
//                    JSONObject listNode = beansNode.getJSONObject("beans");
//                    Object locationCurrNode = listNode.get("list");
//                    if (locationCurrNode instanceof JSONObject) {
//                        // Find friend(s) nearby
//                        Object locationCurrValue = ((JSONObject) locationCurrNode).get("locationCurr");
//
//                        if (locationCurrValue instanceof JSONObject) {
//
//                            // Single record
//                            Data data = jsonMapper.readValue(locationCurrValue.toString(), Data.class);
//                            friendsList.add(data);
//
//                        } else if (locationCurrValue instanceof JSONArray) {
//
//                            // Array record
//                            JSONArray locationCurrValueArray = (JSONArray) locationCurrValue;
//                            for (int i = 0; i < locationCurrValueArray.length(); i++) {
//                                JSONObject locationCurr = (JSONObject) locationCurrValueArray.get(i);
//
//                                Data data = jsonMapper.readValue(locationCurr.toString(), Data.class);
//                                friendsList.add(data);
//                            }
//
//                        } else {
//
//                            // Error
//                            if (mListener != null) {
//                                mListener.onError(ERROR_UNKNOWN);
//                            }
//                        }
//                    } else {
//                        // No friend nearby
//                        if (mListener != null) {
//                            mListener.onNoFriendNearby();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (mListener != null) {
//                    mListener.onFoundFriends(friendsList);
//                }
//            }
//        };
//    }
//
//    private Utils.OnHttpPostResponse mOnHttpPostResponse;
//
//    public boolean send() {
//        String httpRequest = toUrlRequest();
//
//        Utils.httpPost(httpRequest, mOnHttpPostResponse);
//
//        Log.d(TAG, String.format("`DataRequest.send` url(%s)", httpRequest));
//
//        return true;
//    }
//
//    // http://45.55.4.64:8080/meetu/userAction!meetu?imei=imei001&longitude=50.000000&latitude=10.000000
//    private static final String URL_FMT = "http://45.55.4.64:8080/meetu/userAction!meetu?imei=%s&longitude=%.6f&latitude=%.6f";
//
//    public String toUrlRequest() {
//        return String.format(URL_FMT,
//                mData.getUserId(), mData.getLongitude(), mData.getLatitude());
//    }
}
