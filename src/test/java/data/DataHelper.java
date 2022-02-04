package data;

import com.github.javafaker.Faker;
import lombok.Value;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {

    public DataHelper() {
    }

    @Value
    public static class CardInfo {
        public String cardNumber;
        public String year;
        public String cardHolder;
        public String cvc;
    }


    public static CardInfo getData(int years) {
        Faker faker = new Faker();
        String cardNumber = faker.finance().creditCard();
        String year = LocalDate.now().plusYears(years).format(DateTimeFormatter.ofPattern("yy"));
        String cardHolder = faker.name().fullName().toUpperCase(Locale.forLanguageTag("eng"));
        String cvc = faker.numerify("###");
        return new CardInfo(cardNumber, year, cardHolder, cvc);
    }

    public static CardInfo getInvalidData(int years) {
        Faker faker = new Faker();
        String cardNumber = faker.numerify("#");
        String year = LocalDate.now().minusYears(years).format(DateTimeFormatter.ofPattern("yy"));
        String cardHolder = faker.name().firstName().toUpperCase(Locale.forLanguageTag("eng"));
        String cvc = faker.numerify("##");
        return new CardInfo(cardNumber, year, cardHolder, cvc);
    }

    public static String getSymbols() {
        return "*&^<&%>{%#%";
    }

    public static String getValidCardNumberApproved() {
        return "4444 4444 4444 4441";
    }

    public static String getValidCardNumberDeclined() {
        return "4444 4444 4444 4442";
    }

    public static String getNumb13() {
        return "13";
    }

    public static String getNumb00() {
        return "00";
    }

    public static String getValidMonth() {
        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        Random index = new Random();
        int indexInt = index.nextInt(months.length);
        return months[indexInt];
    }
    public static String getEmptySymbol(){
        return null;
    }
}
