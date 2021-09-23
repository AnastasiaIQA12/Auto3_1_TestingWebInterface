[![Build status](https://ci.appveyor.com/api/projects/status/8ii04kele85j916t/branch/master?svg=true)](https://ci.appveyor.com/project/AnastasiaIQA12/automation3-1/branch/master)
# Домашнее задание к занятию «2.1. Тестирование веб-интерфейсов»
## Настройка
### 1. Целевой сервис
Ваш целевой сервис (SUT - System under test), расположен в файле `app-order.jar`. Нужно его скачать и положить в каталог `artifacts` вашего проекта. Поскольку файлы с расширением `.jar` находят в списках `.gitignore` нужно принудительно заставить git следить за ним: `git add -f artifacts/app-order.jar`. После этого сделать `git push`. Удостовериться, что файл попал в репозиторий.

### 2. `build.gradle`
Ваш `build.gradle` должен выглядеть следующим образом:
```groovy
plugins {
    id 'java'
}
group 'ru.netology'
version '1.0-SNAPSHOT'
sourceCompatibility = 1.8
// кодировка файлов (если используете русский язык в файлах)
compileJava.options.encoding = "UTF-8"
compileTestJava.options.encoding = "UTF-8"
repositories {
    mavenCentral()
}
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.6.1'
    testImplementation 'com.codeborne:selenide:5.19.0'
}
test {
    useJUnitPlatform()
    // в тестах, вызывая `gradlew test -Dselenide.headless=true` будем передавать этот параметр в JVM (где его подтянет Selenide)
    systemProperty 'selenide.headless', System.getProperty('selenide.headless')
}
```

#### **HEADLESS режим браузера**
На серверах сборки чаще всего нет графического интерфейса, поэтому запуская браузер в режиме **headless** мы отключаем графический интерфейс (при этот все процессы браузера продолжают работать так же). При использовании **selenium** данный режим настраивается непосредственно в коде во время инициализации драйвера, примеры инициализации ниже.
Включение **headless** режима при использовании **selenium** необходимо реализовать в коде во время создания экземпляра webdriver:  
```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--disable-dev-shm-usage");
options.addArguments("--no-sandbox");
options.addArguments("--headless");
driver = new ChromeDriver(options);
```

Для **selenide** **headless** режим активируется при запуске тестов с определенным параметром:  
```
gradlew test -Dselenide.headless=true
```
### 3. `.appveyor.yml`
AppVeyor настраивается аналогично предыдущей лекции за исключением того, что тесты нужно запускать так, чтобы **selenide** запускался в headless-режиме. Если тесты написаны с использованием **selenium**, то после включения headless режима в коде никаких дополнительных флагов в командной строке передавать не нужно.

## Задача №1 - Заказ карты
Необходимо автоматизировать тестирование формы заказа карты:
<img width="513" alt="order" src="https://user-images.githubusercontent.com/72652840/134561618-ce8f2085-3b69-4031-bb9d-d742e00cc06e.png">

Требования к содержимому полей:
1. Поле Фамилия и имя - разрешены только русские буквы, дефисы и пробелы.
2. Поле телефон - только цифры (11 цифр), символ + (на первом месте).
3. Флажок согласия должен быть выставлен.

Тестируемая функциональность: отправка формы.

Условия: если все поля заполнены корректно, то получаем сообщение об успешно отправленной заявке:
![success](https://user-images.githubusercontent.com/72652840/134561682-6aad673e-ee8a-467a-b803-f066085fe9d9.jpg)

Проект с авто-тестами должен быть выполнен на базе Gradle, с использованием Selenide/Selenium (по выбору).

Для запуска тестируемого приложения скачайте jar-файл из текущего каталога и запускайте его командой:
`java -jar app-order.jar`

Приложение будет запущено на порту 9999. Если по каким-то причинам порт 9999 на вашей машине используется другим приложением, используйте:

`java -jar app-order.jar -port=7777`

Убедиться, что приложение работает, вы можете открыв в браузере страницу: http://localhost:9999

## Задача №2 - Проверка валидации 

После того, как вы протестировали Happy Path, необходимо протестировать остальные варианты.

Тестируемая функциональность: валидация полей перед отправкой.

Условия: если какое-то поле не заполнено, или заполнено неверно, то при нажатии на кнопку "Продолжить" должны появляться сообщения об ошибке (будет подсвечено только первое неправильно заполненное поле):
<img width="511" alt="error" src="https://user-images.githubusercontent.com/72652840/134561738-61530793-75ba-4f21-befb-0afdb72cf690.png">

