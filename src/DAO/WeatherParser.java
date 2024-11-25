package DAO;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherParser {

    /**
     * Phân tích dữ liệu thời tiết từ JSON và in thông tin ra màn hình.
     * @param json Chuỗi JSON chứa thông tin thời tiết.
     * @throws Exception Nếu xảy ra lỗi trong quá trình phân tích.
     */
    public static String parseWeather(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);

        // Lấy điều kiện thời tiết và nhiệt độ
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        String weather = weatherArray.getJSONObject(0).getString("main"); // Ví dụ: "Clear", "Rain"
        return weather;

//        double temp = jsonObject.getJSONObject("main").getDouble("temp");  // Chuyển từ Kelvin sang Celsius
//
//        // In kết quả
//        System.out.println("Weather: " + weather);
//        System.out.println("Temperature: " + temp + "°C");
    }

    /**
     * Phân tích dữ liệu thời gian từ JSON và in thông tin ra màn hình.
     * @param json Chuỗi JSON chứa thông tin thời gian.
     * @throws Exception Nếu xảy ra lỗi trong quá trình phân tích.
     */
    public static int parseTime(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);

        // Lấy giá trị Unix timestamp từ trường "dt"
        long timestamp = jsonObject.getLong("dt");

        // Chuyển đổi timestamp sang ZonedDateTime
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh"); // Múi giờ địa phương
        ZonedDateTime dateTime = Instant.ofEpochSecond(timestamp).atZone(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        String hour = dateTime.format(formatter);
        System.out.println(hour);
        return Integer.parseInt(hour);

        // Định dạng giờ và phút

//
//        // In kết quả
//        System.out.println("Giờ và phút hiện tại: " + timeString);
    }
}
