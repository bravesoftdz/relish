package com.example.steps;

import com.aspenshore.relish.core.TableRow;
import com.aspenshore.relish.selenide.Checkbox;
import com.aspenshore.relish.selenide.SelenideWidget;
import com.example.components.AddTaskPage;
import com.example.components.TaskPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class SomeSteps {
    private TaskPage taskPage = new TaskPage();
    private AddTaskPage addTaskPage = new AddTaskPage();

    @Given("^I am on the task list$")
    public void iAmOnTheTaskList() {
        taskPage.launch();
    }

    @Then("^the list of tasks will be empty$")
    public void theListOfTasksWillBeEmpty() {
        taskPage.taskTable().matches(new ArrayList<>());
    }

    @When("^I choose to add this task$")
    public void iChooseToAddThisTask(List<TableRow> taskDetails) {
        taskPage.addButton().click();
        addTaskPage.set(taskDetails.get(0));
        addTaskPage.saveButton().click();
    }

    @Then("^I will see this on the list of tasks$")
    public void iWillSeeThisOnTheListOfTasks(List<TableRow> tasks) {
        taskPage.taskTable().matches(tasks);
    }

    @Given("^I have the following tasks$")
    public void iHaveTheFollowingTasks(List<TableRow> tasks) {
        taskPage.launch();
        String json = new JSONArray(tasks.stream().map(TableRow::toObjectMap).collect(toList())).toString();
        taskPage.executeJavaScript("return (function(tasks){localStorage.setItem('tasks', tasks)})(arguments[0])", json);
        taskPage.refreshPage();
    }

    @When("^I delete the task '([^']+)'$")
    public void iDeleteTheTaskBuySomeBread(String taskName) {
        taskPage.taskTable()
                .rows("name", taskName)
                .stream()
                .map(r -> r.cell("select"))
                .forEach(s -> ((Checkbox)s).check());
        taskPage.deleteButton().click();
    }
}
