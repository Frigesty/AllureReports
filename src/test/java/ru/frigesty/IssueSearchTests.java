package ru.frigesty;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.frigesty.pages.WebSteps;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.By.linkText;

@Feature("Issue в репозитории")
@Story("Отображение названия у Issue с номером")
@Owner("Frigesty")
@Severity(SeverityLevel.CRITICAL)
@Link(value = "testing", url = "https://github.com")
public class IssueSearchTests {

    private final WebSteps steps = new WebSteps();

    private static final String REPOSITORY = "Frigesty/WorkingWithFiles",
                              ISSUE_NUMBER = "1",
                                ISSUE_NAME = "IssueTests";

    @Test
    @DisplayName("Чистый Selenide (с Listener)")
    public void testClearSearch() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        open("https://github.com");
        $(".header-search-input").click();
        $(".header-search-input").sendKeys(REPOSITORY);
        $(".header-search-input").submit();
        $(linkText(REPOSITORY)).click();
        $("#issues-tab").click();
        $(String.format("#issue_%s_link", ISSUE_NUMBER)).shouldHave(text(ISSUE_NAME));
    }

    @Test
    @DisplayName("Лямбда шаги через step (name, () -> {})")
    public void testLambdaStep() {
        SelenideLogger.addListener("allure", new AllureSelenide());

        step("Открываем главную страницу", () -> {
            open("https://github.com");
        });

        step("Ищем репозиторий " + REPOSITORY, () -> {
            $(".header-search-input").click();
            $(".header-search-input").sendKeys(REPOSITORY);
            $(".header-search-input").submit();
        });

        step("Кликаем по ссылке репозитория " + REPOSITORY, () -> {
            $(linkText(REPOSITORY)).click();
        });
        step("Открывает таб Issue", () -> {
            $("#issues-tab").click();
        });

        step("Провереяем что Issue с номером " + ISSUE_NUMBER + " Имеет название " + ISSUE_NAME, () -> {
            $(String.format("#issue_%s_link", ISSUE_NUMBER)).shouldHave(text(ISSUE_NAME));
        });
    }

    @Test
    @DisplayName("Шаги с аннотацией @Step")
    public void testWebSteps() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        steps.openMainPage();
        steps.searchForRepository(REPOSITORY);
        steps.clickOnRepositoryLink(REPOSITORY);
        steps.openIssuesTab();
        steps.checkIssueNumberHasName(ISSUE_NUMBER, ISSUE_NAME);
    }
}