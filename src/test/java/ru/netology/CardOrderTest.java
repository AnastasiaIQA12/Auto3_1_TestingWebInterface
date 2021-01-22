package ru.netology;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardOrderTest {
    @Test
    void shouldSubmitForm() {
        open("http://localhost:9999/");
        $("[data-test-id=name] input").setValue("Елифанова Анастасия");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=order-success]").shouldHave(exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время."));
    }

    @Test
    void shouldWrongName() {
        open("http://localhost:9999/");
        $("[data-test-id=name] input").setValue("Elifanova");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldWrongPhone() {
        open("http://localhost:9999/");
        $("[data-test-id=name] input").setValue("Елифанова Анастасия");
        $("[data-test-id=phone] input").setValue("89270000000");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldWrongNameAndPhone() {
        open("http://localhost:9999/");
        $("[data-test-id=name] input").setValue("Elifanova");
        $("[data-test-id=phone] input").setValue("89270000000");
        $("[data-test-id=agreement]").click();
        $("button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }
}
