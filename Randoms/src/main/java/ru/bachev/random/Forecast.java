package ru.bachev.random;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

public class Forecast {
    private final String personName;
    private final String forecastVariant;
    private final Map<String, AlgorithmRandom> strategies = Map.of(
            "Basic", getBasic(),
            "Secure", getSecure()
    );
    private final Map<Integer, String> forecasts = Map.of(
            0, "У вас сегодня будет удача в делах!",
            1, "Сегодня хороший день для саморазвития!"
    );


    public Forecast(String personName, String forecastVariant) {
        this.personName = personName;
        this.forecastVariant = forecastVariant;
    }

    public void print() {
        System.out.print("Привет, " + this.personName + " ваш прогноз: ");
        strategies.getOrDefault(forecastVariant, getDefault()).execute();
    }

    private AlgorithmRandom getBasic() {
        return () -> {
            var random = new Random();
            System.out.println(forecasts.get(random.nextInt(2)));
        };
    }

    private AlgorithmRandom getSecure() {
        return () -> {
            var secureRandom = new SecureRandom(forecasts.get(1).getBytes());
            System.out.println(forecasts.get(secureRandom.nextInt(2)));
        };
    }

    private AlgorithmRandom getDefault() {
        return () -> {
            System.out.println("Нет прогноза");
        };
    }
}
