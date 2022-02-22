package test;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.BuyingTour;
import pages.Dashboard;
import sql.Sql;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pages.BuyingTour.*;

public class TestBuyTour {

    private final DataHelper.CardInfo invalidData = DataHelper.getInvalidDataCard();
    private final DataHelper.CardInfo validDataWithCardApproved = DataHelper.getValidDataCard(DataHelper.getValidCardNumberApproved());
    private final DataHelper.CardInfo validDataWithCardDeclined = DataHelper.getValidDataCard(DataHelper.getValidCardNumberDeclined());
    private final BuyingTour buyingTour = new BuyingTour();


    @BeforeEach
    public void setUp() {
        Configuration.headless = true;
        open("http://localhost:8080/");
    }

    // У большинства полей уведомления отображаются неинформативно. Об этом написал в отчете.
    @Name("Тест с пустыми полями" +
            "Проверка отображение уведомлений о пустых полях и их корректность")
    @Test
    public void shouldBeNotificationsDisplayedForAllFields() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                "",
                "",
                "",
                "",
                ""
        );
        //Клик по кнопке продолжить
        buyingTour.clickOrderButton();
        //Проверки уведомлений полей
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
        buyingTour.checkEmptyFieldMothError();
        buyingTour.checkEmptyFieldYearError();
        buyingTour.checkEmptyFieldHolderError();
        buyingTour.checkEmptyFieldCvcError();
    }

    /////////// Тесты покупки по карте ///////////
    @Name("Проверка успешной покупки по карте" +
            "Должен появится в базе данных ПОКУПОК со статусом ОДОБРЕНО. Так же UI проверка на успешность покупки")
    @Test
    public void shouldAppearInTheDatabaseWithStatusApproved() {
        //Удаление всех записей в бд из таблицы покупка по карте
        Sql.deleteAllStringsForPaymentEntity();
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        //UI Проверка уведомления об успешной операции
        buyingTour.successBuy();
        // Сравнение статуса последней записи в бд.
        assertEquals("APPROVED", Sql.checkStatus());


    }

    @Name("Проверка отказа покупки" +
            "Должен появится в базе данных ПОКУПОК со статусом ОТКЛОНЕНО. Так же UI проверка на отказ от банка")
    @Test
    public void shouldAppearInTheDatabaseWithStatusDeclined() {
        //Удаление всех записей в бд из таблицы покупка по карте
        Sql.deleteAllStringsForPaymentEntity();
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardDeclined.cardNumber,
                validDataWithCardDeclined.moth,
                validDataWithCardDeclined.year,
                validDataWithCardDeclined.cardHolder,
                validDataWithCardDeclined.cvc
        );
        buyingTour.clickOrderButton();
        //UI Проверка уведомления об отказе.
        buyingTour.rejected();
        // Сравнение статуса последней записи в бд.
        assertEquals("DECLINED", Sql.checkStatus());


    }
    //////Проверка валидации полей в форме покупки по карте//////
    @Name("Пустое поле номер карты" +
            "Должно появится уведомление 'Поле обязательно для заполнения'")
    @Test
    void  shouldNotificationAppearUnderTheCardNumberFieldBuyByCard() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                "",
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        // Проверка уведомления о пустом поле
        buyingTour.checkEmptyFieldErrorForCheckingOneField();

    }





    @Name("Ввод символов в поле номер карты" +
            "Должно появится уведомление 'Поле обязательно для заполнения' так как поле принимает только цифры")
    @Test
    void shouldAppearAnIncorrectFormatNotificationUnderTheCardNumbFieldBuyByCard0() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        // Проверка уведомления о пустом поле
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Ввод в поле НОМЕР КАРТЫ значение с одной цифрой" +
            "Должно появится уведомление под полем НОМЕР КАРТЫ 'Неверный формат'")
    @Test
    void shouldAppearAnIncorrectFormatNotificationUnderTheCardNumbFieldBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                "5",
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }


    @Name("Пустое поле МЕСЯЦ" +
            "Должно появится уведомление под полем МЕСЯЦ 'Поле обязательно для заполнения'")
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppearBuyByCard() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Ввод символов в поле МЕСЯЦ" +
            "Должно появится уведомление под полем МЕСЯЦ 'Поле обязательно для заполнения'" +
            "так как поле принимает только цифры ")
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppearBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля месяц " +
            "Должно появится уведомление под полем МЕСЯЦ 'Неверно указан срок действия карты'")
    @Test
    void shouldAppearNotificationAboutAnIncorrectFormatBuyByCard0() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "13",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldMothErrorWithInvalidValue();
    }

    @Name("Проверка поля месяц" +
            "Должно появится уведомление под полем МЕСЯЦ 'Неверно указан срок действия карты'")
    @Test
    void shouldAppearNotificationAboutAnIncorrectFormatBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "00",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldMothErrorWithInvalidValue();
    }


    @Name("Проверка поля год: Пустое поле " +
            "Должно появится уведомление под полем ГОД 'Поле обязательно для заполнения'")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppearBuyByCard() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                "",
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля год: Ввод символов " +
            "Должно появится уведомление под полем ГОД 'Поле обязательно для заполнения' " +
            "так как поле должно принимать только цифры")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppearBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля год: Ввод года меньше текущего на 1" +
            "Должно появится уведомление под полем ГОД 'Истёк срок действия карты' ")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppearBuyByCard2() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                invalidData.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldYearErrorWithInvalidValue();
    }

    @Name("Проверка поля год: Ввод 00 " +
            "Должно появится уведомление под полем ГОД 'Истёк срок действия карты'")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppearBuyByCard3() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                "00",
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldYearErrorWithInvalidValue();
    }


    @Name("Проверка поля Владелец: Пустое поле " +
            "Должно появится уведомление под полем ВЛАДЕЛЕЦ 'Поле обязательно для заполнения' ")
    @Test
    void shouldNotificationAboutAnEmptyHolderFieldWillAppearBuyByCard() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                "",
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorNotification();
    }


    @Name("Проверка поля Владелец: Ввод символов" +
            "Должно появится уведомление под полем ВЛАДЕЛЕЦ 'Неверный формат' " +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyHolderFieldWillAppearBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }


    @Name("Проверка поля CVC: Пустое поле" +
            "Должно появится уведомление под полем CVC 'Поле обязательно для заполнения' " +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyCVCFieldWillAppearBuyByCard0() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                ""
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля CVC: значение с 2 цифрами" +
            "Должно появится уведомление под полем CVC 'Неверный формат' ")
    @Test
    void shouldNotificationAboutAnEmptyCVCFieldWillAppearBuyByCard1() {
        Dashboard.clickButtonBuyByCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                invalidData.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }


    /////////// Тесты покупки в Кредит ///////////
    @Name("Проверка отказа покупки в кредит" +
            "Должен появится в базе данных ПОКУПОК В КРЕДИТ со статусом ОТКЛОНЕНО. Так же UI проверка на отказ от банка")
    @Test
    public void shouldAppearInTheDatabaseCreditWithStatusDeclined() {
        //Удаление всех записей в бд из таблицы покупка в кредит
        Sql.deleteAllStringsForCreditRequestEntity();
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardDeclined.cardNumber,
                validDataWithCardDeclined.moth,
                validDataWithCardDeclined.year,
                validDataWithCardDeclined.cardHolder,
                validDataWithCardDeclined.cvc
        );
        buyingTour.clickOrderButton();
        //Проверка уведомления об отказе.
        buyingTour.rejected();
        assertEquals("DECLINED", Sql.checkStatusCredit());

    }


    @Name("Проверка успешной покупки в кредит" +
            "Должен появится в базе данных ПОКУПОК В КРЕДИТ со статусом ОДОБРЕНО." +
            " Так же UI проверка на одобрение от банка")
    @Test
    public void shouldAppearInTheDatabaseCreditWithStatusApproved() {
        //Удаление всех записей в бд из таблицы покупка в кредит
        Sql.deleteAllStringsForCreditRequestEntity();
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.successBuy();
        assertEquals("APPROVED", Sql.checkStatusCredit());


    }
    /////////// Конец тестов на валидных значениях///////////


    ///////////Тесты на валидации полей в форме покупки в кредит///////////
    @Name("Пустое поле номер карты" +
            "Должно появится уведомление 'Поле обязательно для заполнения'")
    @Test
    void  shouldNotificationAppearUnderTheCardNumberField() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                "",
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        // Проверка уведомления о пустом поле
        buyingTour.checkEmptyFieldErrorForCheckingOneField();

    }

    @Name("Ввод символов в поле номер карты" +
            "Должно появится уведомление 'Поле обязательно для заполнения' так как поле принимает только цифры")
    @Test
    void shouldAppearAnIncorrectFormatNotificationUnderTheCardNumbField0() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        // Проверка уведомления о пустом поле
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Ввод в поле НОМЕР КАРТЫ значение с одной цифрой" +
            "Должно появится уведомление под полем НОМЕР КАРТЫ 'Неверный формат'")
    @Test
    void shouldAppearAnIncorrectFormatNotificationUnderTheCardNumbField1() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                "5",
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }


    @Name("Пустое поле МЕСЯЦ" +
            "Должно появится уведомление под полем МЕСЯЦ 'Поле обязательно для заполнения'")
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppear0() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Ввод символов в поле МЕСЯЦ" +
            "Должно появится уведомление под полем МЕСЯЦ 'Поле обязательно для заполнения'")
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppear1() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля месяц " +
            "Должно появится уведомление под полем МЕСЯЦ 'Неверно указан срок действия карты'")
    @Test
    void shouldAppearNotificationAboutAnIncorrectFormat0() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "13",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldMothErrorWithInvalidValue();
    }

    @Name("Проверка поля месяц " +
            "Должно появится уведомление под полем МЕСЯЦ 'Неверно указан срок действия карты'")
    @Test
    void shouldAppearNotificationAboutAnIncorrectFormat1() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                "00",
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldMothErrorWithInvalidValue();
    }


    @Name("Проверка поля год: Пустое поле " +
            "Должно появится уведомление под полем ГОД 'Поле обязательно для заполнения'")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppear0() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                "",
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля год: Ввод символов " +
            "Должно появится уведомление под полем ГОД 'Поле обязательно для заполнения' " +
            "так как поле должно принимать только цифры")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppear1() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля год: Ввод года меньше текущего на 1" +
            "Должно появится уведомление под полем ГОД 'Истёк срок действия карты' ")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppear2() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                invalidData.getYear(),
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldYearErrorWithInvalidValue();
    }

    @Name("Проверка поля год: Ввод 00 " +
            "Должно появится уведомление под полем ГОД 'Истёк срок действия карты'")
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppear3() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
               "00",
                validDataWithCardApproved.cardHolder,
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkFieldYearErrorWithInvalidValue();
    }


    @Name("Проверка поля Владелец: Пустое поле " +
            "Должно появится уведомление под полем ВЛАДЕЛЕЦ 'Поле обязательно для заполнения' ")
    @Test
    void shouldNotificationAboutAnEmptyHolderFieldWillAppear() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                "",
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorNotification();
    }


    @Name("Проверка поля Владелец: Ввод символов" +
            "Должно появится уведомление под полем ВЛАДЕЛЕЦ 'Неверный формат' ")
    @Test
    void shouldNotificationAboutAnEmptyHolderFieldWillAppear1() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                DataHelper.getInvalidSymbols(),
                validDataWithCardApproved.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }


    @Name("Проверка поля CVC: Пустое поле" +
            "Должно появится уведомление под полем CVC 'Поле обязательно для заполнения' ")
    @Test
    void shouldNotificationAboutAnEmptyCVCFieldWillAppear() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                ""
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }

    @Name("Проверка поля CVC: значение с 2 цифрами" +
            "Должно появится уведомление под полем CVC 'Неверный формат' ")
    @Test
    void shouldNotificationAboutAnEmptyCVCFieldWillAppear1() {
        Dashboard.clickButtonBuyCreditCard();
        enteringInputFields(
                validDataWithCardApproved.cardNumber,
                validDataWithCardApproved.moth,
                validDataWithCardApproved.year,
                validDataWithCardApproved.cardHolder,
                invalidData.cvc
        );
        buyingTour.clickOrderButton();
        buyingTour.checkEmptyFieldErrorForCheckingOneField();
    }



}

