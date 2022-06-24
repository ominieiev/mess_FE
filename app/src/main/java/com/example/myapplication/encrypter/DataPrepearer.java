package com.example.myapplication.encrypter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DataPrepearer {

    private static final String MESSAGE_SERVER_DELIMITER = "&&##!!";
    private static final String MESSAGE_DELIMITER = "\n\n";

    public static String prepareText(String text) {
        if (text.trim().isEmpty()) {
            return "";
        }
        List<String> strings = Arrays.asList(text.split(MESSAGE_SERVER_DELIMITER));
        String result = strings.stream().map(str -> AES.decrypt(str))
                .collect(Collectors.joining(MESSAGE_DELIMITER));
        if (result == null) {
            return "";
        }
        return result;
    }

    public static String generateRandomName() {
        List<String> animals = new ArrayList<>();
        animals.add("Крот");
        animals.add("Волк");
        animals.add("Медведь");
        animals.add("Пингвин");
        animals.add("Бобер");
        animals.add("Заяц");
        animals.add("Дельфин");
        animals.add("Кот");
        int animal = new Random().nextInt(7);
        List<String> colours = new ArrayList<>();
        colours.add("Синий");
        colours.add("Желтый");
        colours.add("Черный");
        colours.add("Белый");
        colours.add("Красный");
        colours.add("Оранжевый");
        colours.add("Зеленый");
        colours.add("Фиолетовый");
        int colour = new Random().nextInt(7);

        return colours.get(colour) + " " + animals.get(animal);
    }
}
