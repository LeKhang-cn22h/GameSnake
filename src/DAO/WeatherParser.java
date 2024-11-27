package DAO;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherParser {

    /**
     * Phân tích dữ liệu thời tiết từ JSON và trả về trạng thái thời tiết.
     * @param json Chuỗi JSON chứa thông tin thời tiết.
     * @return Trạng thái thời tiết (ví dụ: "Clear", "Rain").
     * @throws Exception Nếu xảy ra lỗi trong quá trình phân tích.
     */
    public static String parseWeather(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);

        // Lấy điều kiện thời tiết từ mảng "weather"
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        String weather = weatherArray.getJSONObject(0).getString("main"); // Trạng thái thời tiết chính (ví dụ: "Clear", "Rain")
        return weather;
    }

    /**
     * Phân tích dữ liệu thời gian từ JSON và trả về giờ hiện tại trong ngày.
     * @param json Chuỗi JSON chứa thông tin thời gian (Unix timestamp).
     * @return Giờ hiện tại dưới dạng số nguyên.
     * @throws Exception Nếu xảy ra lỗi trong quá trình phân tích.
     */
    public static int parseTime(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);

        // Lấy giá trị Unix timestamp từ trường "dt"
        long timestamp = jsonObject.getLong("dt");

        // Chuyển đổi timestamp sang ZonedDateTime sử dụng múi giờ "Asia/Ho_Chi_Minh"
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ địa phương
        ZonedDateTime dateTime = Instant.ofEpochSecond(timestamp).atZone(zone);

        // Định dạng thời gian chỉ lấy giờ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        String hour = dateTime.format(formatter);
        
        // Chuyển đổi giờ từ String sang Integer
        return Integer.parseInt(hour);
    }
}
