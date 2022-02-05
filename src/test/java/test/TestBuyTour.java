package test;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import jdk.jfr.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.BuyingTour;
import page.Notifications;
import sql.Sql;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBuyTour {
    private final DataHelper.CardInfo inCorData = DataHelper.getInvalidData(1);
    private final BuyingTour buyingTour = new BuyingTour();
    private final DataHelper.CardInfo data = DataHelper.getData(4);
    private final Notifications notifications = new Notifications();

    @BeforeEach
    public void setUp() {
        Configuration.headless = true;
        open("http://localhost:8080/");
    }

    @Name("Проверка успешной покупки по карте" +
            "Должен появится в базе данных ПОКУПОК со статусом ОДОБРЕНО. Так же UI проверка на успешность покупки")
    @Test
    public void ShouldAppearInTheDatabaseWithStatus() {
        // Ожидаемый результат при добавлении операции
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getEmptySymbol(),
                DataHelper.getEmptySymbol(),
                DataHelper.getEmptySymbol(),
                DataHelper.getEmptySymbol(),
                DataHelper.getEmptySymbol()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        notifications.emptyFieldMoth();
        notifications.emptyFieldYear();
        notifications.emptyFieldHolder();
        notifications.emptyFieldCvc();
        //Сравнение количества записей после новой записи в бд
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());

    }

    @Name("Проверка успешной покупки по карте" +
            "Должен появится в базе данных ПОКУПОК со статусом ОДОБРЕНО. Так же UI проверка на успешность покупки")
    @Test
    public void ShouldAppearInTheDatabaseWithStatusApproved() {
        // Ожидаемый результат при добавлении операции
        long expected = Sql.getNumberOfRawsFromOrderEntity() + 1;
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        //Проверка уведомления об отказе
        notifications.successBuy();
        assertEquals("APPROVED", Sql.checkStatus());
        //Сравнение количества записей после новой записи в бд
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());

    }

    @Name("Проверка отказа покупки" +
            "Должен появится в базе данных ПОКУПОК со статусом ОТКЛОНЕНО. Так же UI проверка на отказ от банка")
    @Test
    public void ShouldAppearInTheDatabaseWithStatusDeclined() {
        long expected = Sql.getNumberOfRawsFromOrderEntity() + 1;
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberDeclined(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        //Проверка уведомления об отказе.
        notifications.rejected();
        assertEquals("DECLINED", Sql.checkStatus());
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());

    }

    @Name("Проверка отказа покупки в кредит" +
            "Должен появится в базе данных ПОКУПОК В КРЕДИТ со статусом ОТКЛОНЕНО. Так же UI проверка на отказ от банка")
    @Test
    public void ShouldAppearInTheDatabaseCreditWithStatusDeclined() {
        long expected = Sql.getNumberOfRawsFromOrderEntity() + 1;
        buyingTour.clickBuyCredit();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberDeclined(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        //Проверка уведомления об отказе.
        notifications.rejected();
        assertEquals("DECLINED", Sql.checkStatusCredit());
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Проверка успешной покупки в кредит" +
            "Должен появится в базе данных ПОКУПОК В КРЕДИТ" +
            " со статусом ОДОБРЕНО. Так же UI проверка на одобрение от банка")
    @Test
    public void ShouldAppearInTheDatabaseCreditWithStatusApproved() {
        long expected = Sql.getNumberOfRawsFromOrderEntity() + 1;
        buyingTour.clickBuyCredit();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.successBuy();
        assertEquals("APPROVED", Sql.checkStatusCredit());
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());

    }

    @Name("Пустое поле номер карты" +
            "Должно появится уведомление 'Поле обязательно для заполнения', и проверка отсутствия новой записи в бд")
    @Test
    void shouldNotificationWillAppearEmptyCardNumberField() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getEmptySymbol(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        // Проверка уведомления о пустом поле
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Ввод символов в поле номер карты" +
            "Должно появится уведомление 'Поле обязательно для заполнения' так как поле принимает только цифры" +
            ", и проверка отсутствия новой записи в бд")
    @Test
    void shouldNotificationWillAppearInvalidFormat() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getSymbols(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        // Проверка уведомления о пустом поле
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Ввод в поле НОМЕР КАРТЫ значение с одной цифрой" +
            "Должно появится уведомление под полем НОМЕР КАРТЫ 'Неверный формат'" +
            ", и проверка отсутствия новой записи в бд")
    @Test
    void shouldNotificationWillAppearInvalidFormat1() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                inCorData.getCardNumber(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }


    @Name("Пустое поле МЕСЯЦ" +
            "Должно появится уведомление под полем МЕСЯЦ 'Поле обязательно для заполнения'" +
            ", и проверка отсутствия новой записи в бд")
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppear() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getEmptySymbol(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Ввод символов в поле МЕСЯЦ" +
            "Должно появится уведомление под полем МЕСЯЦ 'Поле обязательно для заполнения'" +
            "так как поле принимает только цифры " +
            ", и проверка отсутствия новой записи в бд")
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppear1() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getSymbols(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Проверка поля месяц " +
            "Должно появится уведомление под полем МЕСЯЦ 'Неверно указан срок действия карты'" +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppear2() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getNumb13(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.invalidMoth();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Проверка поля месяц " +
            "Должно появится уведомление под полем МЕСЯЦ 'Неверно указан срок действия карты'" +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyMonthFieldWillAppear3() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getNumb00(),
                data.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.invalidMoth();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }


    @Name("Проверка поля год: Пустое поле " +
            "Должно появится уведомление под полем ГОД 'Поле обязательно для заполнения'" +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppear() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                DataHelper.getEmptySymbol(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Проверка поля год: Ввод символов " +
            "Должно появится уведомление под полем ГОД 'Поле обязательно для заполнения' " +
            "так как поле должно принимать только цифры" +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppear1() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                DataHelper.getSymbols(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Проверка поля год: Ввод года меньше текущего на 1" +
            "Должно появится уведомление под полем ГОД 'Истёк срок действия карты' " +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppear2() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                inCorData.getYear(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.invalidYear();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Проверка поля год: Ввод 00 " +
            "Должно появится уведомление под полем ГОД 'Истёк срок действия карты' " +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyYearFieldWillAppear3() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                DataHelper.getNumb00(),
                data.getCardHolder(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.invalidYear();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }


    @Name("Проверка поля Владелец: Пустое поле " +
            "Должно появится уведомление под полем ВЛАДЕЛЕЦ 'Поле обязательно для заполнения' " +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyHolderFieldWillAppear() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                data.getYear(),
                DataHelper.getEmptySymbol(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.emptyValidNotification();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }


    @Name("Проверка поля Владелец: Ввод символов" +
            "Должно появится уведомление под полем ВЛАДЕЛЕЦ 'Неверный формат' " +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyHolderFieldWillAppear1() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                data.getYear(),
                DataHelper.getSymbols(),
                data.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }


    @Name("Проверка поля CVC: Пустое поле" +
            "Должно появится уведомление под полем CVC 'Поле обязательно для заполнения' " +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyCVCFieldWillAppear() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                DataHelper.getEmptySymbol()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }

    @Name("Проверка поля CVC: значение с 2 цифрами" +
            "Должно появится уведомление под полем CVC 'Неверный формат' " +
            ", и проверка отсутствия новой записи в бд") //
    @Test
    void shouldNotificationAboutAnEmptyCVCFieldWillAppear1() {
        long expected = Sql.getNumberOfRawsFromOrderEntity();
        buyingTour.clickBuyByCard();
        buyingTour.orderHouse(
                DataHelper.getValidCardNumberApproved(),
                DataHelper.getValidMonth(),
                data.getYear(),
                data.getCardHolder(),
                inCorData.getCvc()
        );
        buyingTour.clickOrderButton();
        notifications.emptyField();
        assertEquals(expected, Sql.getNumberOfRawsFromOrderEntity());
    }


}
