package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Selenide.*;

public class BuyingTour {

    public BuyingTour() {
    }


    private final ElementsCollection cardHolder = $$("[class='input__control']");

    private final SelenideElement form = $$("[class='button__content']")
            .findBy(Condition.text("Купить"));

    private final SelenideElement formCredit = $$("[class='button__content']")
            .findBy(Condition.text("Купить в кредит"));

    private final SelenideElement cardNumber = $("[placeholder='0000 0000 0000 0000']");

    private final SelenideElement month = $$(".input__control")
            .findBy(Condition.attribute("placeholder", "08"));

    private final SelenideElement year = $$(".input__control")
            .findBy(Condition.attribute("placeholder", "22"));

    private final SelenideElement owner = $$("[class='input__control']").
            findBy(Condition.attribute("placeholder", "Владелец"));

    private final SelenideElement cvc = $$(".input__control")
            .findBy(Condition.attribute("placeholder", "999"));

    private final SelenideElement orderButton = $$("[class='button__content']")
            .findBy(Condition.text("Продолжить"));


    public void orderHouse(String cardNumber, String month, String year, String cardHolder, String cvc) {
        this.cardNumber.setValue(cardNumber);
        this.month.setValue(month);
        this.year.setValue(year);
        this.cardHolder.get(3).setValue(cardHolder);
        this.cvc.setValue(cvc);
    }

    public void clickBuyByCard() {
        form.click();
    }

    public void clickBuyCredit() {
        formCredit.click();
    }

    public void clickOrderButton() {
        orderButton.click();
    }
}
