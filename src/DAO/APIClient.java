package DAO;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class APIClient {
    private static final String API_KEY = "fcb38bf38e48d19ad5c3915e8c89fcde"; // Thay bằng API key của bạn
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    /**
     * Gửi yêu cầu đến API OpenWeatherMap và trả về chuỗi JSON.
     *
     * @param city Tên thành phố để lấy dữ liệu thời tiết.
     * @return Chuỗi JSON chứa dữ liệu thời tiết.
     * @throws Exception Khi xảy ra lỗi trong quá trình gửi yêu cầu.
     */
    public static String getWeatherData(String city) throws Exception {
    	
    	// Mã hóa tên thành phố để tránh lỗi với khoảng trắng và ký tự đặc biệt
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
        // Tạo URI với các tham số truy vấn
        URI uri = new URI(
                BASE_URL + "?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric"
        );

        // Tạo HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        // Tạo HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Gửi yêu cầu và nhận phản hồi
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Kiểm tra mã trạng thái HTTP
        if (response.statusCode() != 200) {
            throw new Exception("HTTP Response Code: " + response.statusCode());
        }

        return response.body();
    }
}

