### Что было запланировано
* Написать тестовую документацию и подготовить тестовую среду для автоматического тестирования
* Написать автотесты по подготовленным сценариям и прогнать их для тестирования веб-приложения.
* Подготовить отчет по проделанной работе.
### Было сделано
* Тестовая документация. Гибридная (Тест кейсы + чек-лист)
* Написаны автотесты .
* Сформирован отчет по автотестам.Смотреть тут [тут](../build/reports/allure-report/allureReport/index.html)
* Написан краткий отчет и рекомендации по продукту. Смотреть [тут](Report.md)
### Не было сделано
* Не были написаны автотесты для полей в модуле "Покупка в кредит". 
Так как поля абсолютно идентичны и если исправить в одном модуле
то исправятся и там.
### Сработавшие риски 
* В целом все риски сработали
1. Были сложности с запуском Банковского симулятора. 
Проблема решалась 6 часов. Как оказалось через Docker он запускался на другом порту.
Проблема решилась скачиванием NodeJS на пк и запуском непосредственно с помощью ее.
2. Поиск локатовор вызвали небольшие сложности, так как не было тестовых идентификаторов.
Приходилось создавать колекции из селекторов и оттуда вытаскивать нужный.
3. По ходу написания возникли сложности из за неверных уведомлений под полями.
Так как по их текстам был поиск локаторов. Пришлось немного увеличить количество строк.

### Сколько запланировали/Сколько потратили с обоснованием расхождения

1. ***Изучение сайта - Запланировано 2 Часа / Потратил 30 мин***
2. ***Написание тестовой документации - Запланировано 2 часа / Потратил 3-4 часа***
* Я думаю это связано с опытом. Так как, чтобы четко спланировать нужно написать достаточно много документаций.
Если бы я писал чек листы на каждый сценарий то ушло бы в 3 раза больше времени.
3. ***Анализ рисков - Запланировано 1 час / Потрачено 15 минут***
* Тут я бы сказал, что риски достаточно примитивные и просчитать было не так уж и сложно.
4. ***Преодоление наступивших рисков - Запланировано 4 часа / Потрачено примерно 8 часов***
* Больше всего проблем вышло с запуском Банковского симулятора. Могу сказать, 
чем больше наступает проблем которые нужно решать, тем больше понимаешь как не стоит делать. 
И как легко решается проблема если знаешь как работает та или иная функция. Потратил намного больше времени 
из за своей неопытности и незнания о возможностях Node и Docker.
5. ***Подготовка тестовой среды - Запланировано 2 часа / Потрачено 9***
* Была проблема с запуском Банковсого симулятора.
6. ***Написание Автотестов - Запланировано 10 часов / Потрачено примерно 10
7. ***Формирование отчета - Запланировано 2 часа / Потрачено 2-4 часа
* Были сложности с внедрением Allure. Пришлось использовать особую комбинацию [версий.](https://github.com/netology-code/aqa-code/blob/master/reporting/allure/build.gradle)