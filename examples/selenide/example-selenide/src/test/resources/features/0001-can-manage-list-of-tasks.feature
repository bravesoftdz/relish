Feature: A list of tasks can be managed by the application
  As a user
  I want to be able to create, read, update and delete tasks
  So that I can manage my time

  Scenario: Initially the list of tasks is empty
    Given I am on the task list
    Then the list of tasks will be empty

  Scenario: I can add multiple tasks
    Given I am on the task list
    When I choose to add this task
      | Name          |
      | Go to the gym |
    Then I will see this on the list of tasks
      | Name          |
      | Go to the gym |
    When I choose to add this task
      | Name           |
      | Buy some bread |
    Then I will see this on the list of tasks
      | Name           |
      | Buy some bread |
      | Go to the gym  |

  Scenario: I can delete a task
    Given I have the following tasks
      | Name           |
      | Buy some bread |
      | Go to the gym  |
    And I am on the task list
    When I delete the task 'Buy some bread'
    Then I will see this on the list of tasks
      | Name          |
      | Go to the gym |
