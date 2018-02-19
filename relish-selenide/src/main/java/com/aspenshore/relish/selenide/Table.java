package com.aspenshore.relish.selenide;

import com.aspenshore.relish.core.*;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.aspenshore.relish.core.TestUtils.attempt;
import static com.codeborne.selenide.Selenide.$;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class Table extends SelenideWidget {
    private Map<String, Function<SelenideElement, SelenideWidget>> factories = new HashMap<>();

    public Table(By selector, Component parent) {
        super(selector, parent);
    }

    public Table(SelenideElement element, Component parent) {
        super(element, parent);
    }

    public String[] headings() {
        String[] ths = get().findElements(By.tagName("th")).stream()
                .map(e -> toGetter(e.getText())).collect(toList()).toArray(new String[]{});
        return ths;
    }

    public Table with(String name, Function<SelenideElement, SelenideWidget> factory) {
        Table clone = new Table(get(), getParent());
        clone.factories.put(name, factory);
        return clone;
    }

    /**
     * Get the data from the table as a list of getables
     *
     * This should be useful when checking the table against the contents of a table in a feature.
     *
     * @return list of getable objects
     */
    public List<Getable> data() {
        assertVisible();
        String[] headings = headings();
        List<List<String>> rowsWithTdStrings = get().findElements(By.tagName("tr")).stream()
                // Ignore rows without TD elements
                .filter(e -> !e.findElements(By.tagName("td")).isEmpty())
                // Then turn each row of TDs into a list of their string-contents
                .map(row -> row.findElements(By.tagName("td")).stream().map(e -> e.getText()).collect(toList()))
                .collect(toList());

        List<Getable> result = new ArrayList<>();
        for (List<String> rowWithTdStrings : rowsWithTdStrings) {
            GetableMap aResult = new GetableMap();
            assertEquals("Different number of headings and columns",
                    headings.length, rowWithTdStrings.size());
            for (int i = 0; i < headings.length; i++) {
                aResult.put(headings[i], rowWithTdStrings.get(i));
            }
            result.add(aResult);
        }
        return result;
    }

    public void matches(List<TableRow> sheetTable) {
        attempt(() -> {
            List<Getable> screenRows = data();
            int i = 0;
            for (TableRow assertRow : sheetTable) {
                assertThat(screenRows.get(i++), TableRowMatchers.getableMatchesAll(assertRow));
            }
        }, 500, 8);
    }

    public List<RowInTable> rows() {
        String[] headings = headings();
        return get().findElements(By.tagName("tr")).stream()
                // Ignore rows without TD elements
                .filter(e -> !e.findElements(By.tagName("td")).isEmpty())
                .map(e -> new RowInTable($(e), this).withFactories(factories))
                .collect(toList());
    }

    public List<RowInTable> rows(Predicate<? super RowInTable> filter) {
        return rows().stream().filter(filter).collect(toList());
    }

    public List<RowInTable> rows(String cellName, String value) {
        return rows(r -> value.equals(r.cell(cellName).getStringValue()));
    }


    private String toGetter(String s) {
        String join = String.join("", Arrays.stream(s.replaceAll("/", " ").split("\\s"))
                .map(s1 -> s1.substring(0, 1).toUpperCase() + s1.substring(1)).collect(toList()));
        return join.substring(0, 1).toLowerCase() + join.substring(1);
    }
}
