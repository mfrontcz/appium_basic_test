# Appium_basic_test
## Projekt dotyczy automatycznego testu polegającemu na wyszykaniu podanego filmu w aplikacji mobilnej i wypisaniu z niego jego obsady
Aby uruchomić aplikację należy włączyć serwer appium na porcie 4723 oraz podłączyć urządzenie mobilne (lub emulować je), następnie wykorzystać komendę:
### Run
```bash
./gradlew run "movie title"
```
W głównym pliku w fukncji openApplication() należy zmienić właściwie dla własnej konfiguracji podane pola: platformVersion, deviceName, udid.
