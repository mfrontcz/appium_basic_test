package appium_test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {

    static AndroidDriver<MobileElement> driver;

    public static void main(String[] args) throws Exception {
        openApplication();
        //Scanner scan = new Scanner(System.in);
        //System.out.println("Movie title:");
        //String title = scan.nextLine();
        String title = "";
        for (String arg : args) {
            title += arg + " ";
        }
        ArrayList<String> cast = null;
        if (driver != null){
            cast = getActorsList(title);
        }
        if (cast == null) {
            System.out.println("EMPTY :(");
        } else {
            System.out.println("Cast of movie " + title +":");
            System.out.println(cast);
        }
    }

    public static void openApplication() throws Exception {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName","Android");
        cap.setCapability("platformVersion","11");
        cap.setCapability("deviceName","sdk_gphone_x86");
        cap.setCapability("udid","emulator-5554");
        cap.setCapability("appPackage","com.orange.pl.orangetvgo");
        cap.setCapability("appActivity","pl.orange.ypt.gui.activity.ActivitySplash");

        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        driver = new AndroidDriver<>(url, cap);
        System.out.println("Application started!");
    }

    public static ArrayList<String> getActorsList(String movie_title) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        MobileElement skip_fast_login = driver.findElement(By.id("com.orange.pl.orangetvgo:id/menu_login"));
        skip_fast_login.click();
        TimeUnit.SECONDS.sleep(1);
        // probably optional - first time usage without login
        MobileElement agreement_terms = driver.findElement(By.id("com.orange.pl.orangetvgo:id/welcome_analytics_checkbox"));
        agreement_terms.click();
        MobileElement accept_agreement = driver.findElement(By.id("com.orange.pl.orangetvgo:id/welcome_btn_start"));
        accept_agreement.click();
        TimeUnit.SECONDS.sleep(1);
        // optional section end
        MobileElement vod = driver.findElement(By.id("com.orange.pl.orangetvgo:id/main_btn_vod"));
        vod.click();
        TimeUnit.SECONDS.sleep(1);
        checkForLoadError();
        MobileElement searchbar = driver.findElement(By.id("com.orange.pl.orangetvgo:id/menu_search"));
        searchbar.click();
        TimeUnit.SECONDS.sleep(1);
        MobileElement searchfield = driver.findElement(By.id("com.orange.pl.orangetvgo:id/search_field"));
        searchfield.click();
        searchfield.sendKeys(movie_title);
        driver.pressKey(new KeyEvent(AndroidKey.ENTER));
        TimeUnit.SECONDS.sleep(1);
        checkForLoadError();
        MobileElement first_movie = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[2]/android.widget.FrameLayout/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.ImageView"));
        first_movie.click();
        TimeUnit.SECONDS.sleep(1);
        checkForLoadError();
        // scroll into view - "Obsada"
        driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true))"+".scrollIntoView(new UiSelector().textContains(\"Inne z kategorii\"))"));
        ArrayList<String> actors = new ArrayList<>();
        int i = 1;
        while(i != 0){
            try{
                MobileElement actor_text = driver.findElement(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.ScrollView/androidx.appcompat.widget.LinearLayoutCompat/androidx.appcompat.widget.LinearLayoutCompat/androidx.recyclerview.widget.RecyclerView[1]/androidx.appcompat.widget.LinearLayoutCompat["+i+"]/android.widget.TextView"));
                actors.add(actor_text.getText());
                i++;
            } catch(Exception e){
                System.out.println("Actors list ended.");
                i = 0;
            }
        }
        return actors;
    }

    public static void checkForLoadError(){
        int error = 1;
        while (error != 0) {
            // error may take multiple retries
            try {
                MobileElement load_error = driver.findElement(By.id("com.orange.pl.orangetvgo:id/error_frame_btn"));
                load_error.click();
                error++;
            } catch(Exception e) {
                error--;
            }
        }
    }
}
