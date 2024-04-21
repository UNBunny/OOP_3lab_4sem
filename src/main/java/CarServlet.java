

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@WebServlet(name = "carServlet", value = "/car")
public class CarServlet extends HttpServlet {
    private static final String FILE_PATH = "C:\\Users\\nikit\\IdeaProjects\\OOP_3lab_sem\\src\\main\\java\\car.json";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String carsJson = readCarsFromFile();
        response.getWriter().write(carsJson);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonRequest = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonRequest.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Ошибка при чтении запроса");
            return;
        }

        // Преобразование JSON-строки в JSONObject
        JSONObject newCarJson = new JSONObject(jsonRequest.toString());

        // Добавление нового автомобиля в список
        JSONArray carsJsonArray = new JSONArray(readCarsFromFile());
        carsJsonArray.put(newCarJson);

        // Запись обновленного списка автомобилей в файл
        writeCarsToFile(carsJsonArray.toString());

        // Отправка обновленного списка автомобилей
        response.getWriter().write(carsJsonArray.toString());
    }

    private String readCarsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "[]";
        }
    }

    private void writeCarsToFile(String carsJson) {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH)) {
            fileWriter.write(carsJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}