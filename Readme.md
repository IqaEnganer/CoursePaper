## Инструкция по запуску авто-тестов.
### Требуемые приложения для запуска.
* IDEA
* GIT
* NodeJS. 
* Docker. 
* Google Chrome браузер.
### Шаги для запуска
1. Создать Gradle проект в нужной вам папке
2. В консоли IDEA ввести команду `git init` для создания репозитория.
3. В той же консоли ввести `git clone https://github.com/IqaEnganer/CoursePaper.git` для клонирования удаленного репозитория.
4. В консоли ввести команду `cd gate-simulator`. Для перехода в данную папку и `npm start` для запуска симулятора банка
5. Запустить Docker Desktop. 
6. В консоли прописать `docker-compose up`  для создания контейнера и запуска.
7. В консоли ввести `java -jar artifacts/aqa-shop.jar` Для запуска тестируемого веб-приложения.
8. Проверить работоспособность [SUT](http://localhost:8080/)
9. В IDEA c помощью сочетания клавиш ctrl + shift + f10 запустить тесты.

### Настройки для запуска на СУБД Postgresql.

#### Настройки файла docker-compose.yml
    version: '3.7'
    services:
    postgres:
    image: postgres:latest
    ports:
    - '5432:5432'
    environment:
    - POSTGRES_DB=app
    - POSTGRES_USER=user
    - POSTGRES_PASSWORD=pass

#### Запуск приложения происходит со специальным флагом.
* ` Java -jar artifacts/aqa-shop.jar --spring.profiles.active=post `
* Для тестов меняется конфигурация в поле Environment variable на `url=jdbc:postgresql://localhost:5432/app`

![img.png](img.png)

### Документация 
* [План тестирования](documentation/Plan.md)
* [Проделанная работа](documentation/Summary.md)
* [Отчет](documentation/Report.md)

