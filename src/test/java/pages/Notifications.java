package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$$;

public class Notifications {
    public Notifications() {
    }

    private final ElementsCollection notifications = $$("[class='input__sub']");

    private final SelenideElement successBuy = $$("[class='notification__content']")
            .findBy(Condition.exactText("Операция одобрена Банком."));

    private final SelenideElement rejected = $$("[class='notification__content']")
            .findBy(Condition.exactText("Ошибка! Банк отказал в проведении операции."));


    public void successBuy() {
        successBuy.shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    public void rejected() {
        rejected.shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    public void emptyField() {
        notifications.get(0).shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }

    public void emptyFieldMoth() {
        notifications.get(1).shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }

    public void emptyFieldYear() {
        notifications.get(2).shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }

    public void emptyFieldHolder() {
        notifications.get(3).shouldBe(Condition.visible).shouldHave(Condition.text("Поле обязательно для заполнения"));
    }

    public void emptyFieldCvc() {
        notifications.get(4).shouldBe(Condition.visible).shouldHave(Condition.text("Неверный формат"));
    }

    public void invalidMoth(){
        notifications.get(0).shouldBe(Condition.visible).shouldHave(Condition.text("Неверно указан срок действия карты"));
    }

    public void invalidYear(){
        notifications.get(0).shouldBe(Condition.visible).shouldHave(Condition.text("Истёк срок действия карты"));
    }

    public void emptyValidNotification(){
        notifications.get(0).shouldBe(Condition.visible).shouldHave(Condition.text("Поле обязательно для заполнения"));
    }
}
