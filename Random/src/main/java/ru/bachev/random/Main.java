package ru.bachev.random;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Нет параметров!");
            return;
        }

        var forecasts = Map.of(
                0, "У вас сегодня будет удача в делах!",
                1, "Сегодня хороший день для саморазвития!"
        );
        var name = args[0];
        var variant = args[1] == null ? "" : args[1];

        System.out.print("Привет, " + name + " ваш прогноз: ");

        if ("Basic".equals(variant)) {
            var random = new Random();
            System.out.println(forecasts.get(random.nextInt(2)));
        } else if ("Secure".equals(variant)){
            var secureRandom = new SecureRandom(forecasts.get(1).getBytes());
            System.out.println(forecasts.get(secureRandom.nextInt(2)));
        } else {
            System.out.println("Нет прогноза");
        }
    }
}
