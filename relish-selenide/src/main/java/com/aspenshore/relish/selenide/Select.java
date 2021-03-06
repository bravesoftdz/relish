package com.aspenshore.relish.selenide;

import com.aspenshore.relish.core.Component;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.aspenshore.relish.core.TestUtils.attempt;

public class Select extends SelenideWidget {
    public Select(By selector, Component parent) {
        super(selector, parent);
    }

    public Select(SelenideElement element, Component parent) {
        super(element, parent);
    }

    public void select(String option) {
        assertVisible();
        get().selectOption(option);
    }

    @Override
    public void setStringValue(String value) {
        select(value);
    }

    @Override
    public String getStringValue() {
        assertVisible();
        return get().getSelectedValue();
    }
}
