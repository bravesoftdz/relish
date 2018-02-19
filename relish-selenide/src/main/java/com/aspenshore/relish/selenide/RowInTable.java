package com.aspenshore.relish.selenide;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.codeborne.selenide.Selenide.$;

public class RowInTable extends SelenideWidget {

    private Map<String, Function<SelenideElement, SelenideWidget>> factories;

    public RowInTable(By selector, Table parent) {
        super(selector, parent);
        factories = new HashMap<>();
    }

    public RowInTable(SelenideElement peer, Table parent) {
        super(peer, parent);
    }

    public RowInTable withFactories(Map<String, Function<SelenideElement, SelenideWidget>> factories) {
        RowInTable clone = new RowInTable(get(), (Table) getParent());
        clone.factories = factories;
        return clone;
    }

    public SelenideWidget cell(String cellName) {
        List<String> headings = Arrays.asList(((Table) getParent()).headings());
        if (!headings.contains(cellName)) {
            throw new IllegalStateException("Cannot find cell with name '" + cellName + "'");
        }
        int i = headings.indexOf(cellName);
        SelenideElement td = $(get().findElements(By.tagName("td")).get(i));
        if (factories.containsKey(cellName)) {
            return factories.get(cellName).apply(td);
        }
        return new SelenideWidget(td, this);
    }
}
