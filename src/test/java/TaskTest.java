import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import org.sql2o.*;

public class TaskTest {
  Task myTask;
  @Before
  public void setUp() {
     DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_list_test", null, null);
     myTask = new Task("Mow the lawn", 1);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM tasks *;";
    String deleteCategoriesQuery = "DELETE FROM categories *;";
    con.createQuery(deleteCategoriesQuery).executeUpdate();
    con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void Task_instantiatesCorrectly_true() {
    assertEquals(true, myTask instanceof Task);
  }

  @Test
  public void Task_instantiatesWithDescription_String() {
    assertEquals("Mow the lawn", myTask.getDescription());
  }

  @Test
  public void isCompleted_isFalseAfterInstatiation_false() {
    assertEquals(false, myTask.isCompleted());
  }

  @Test
  public void all_returnsAllInstancesOfTask_true() {
    myTask.save();
    Task secondTask = new Task("buy groceries", 1);
    secondTask.save();
    assertEquals(true, Task.all().get(0).equals(myTask));
    assertEquals(true, Task.all().get(1).equals(secondTask));
  }

  @Test
  public void getId_tasksInstantiateWithAnID_1() {
    myTask.save();
    assertTrue(myTask.getId()> 0);
  }

  @Test
  public void find_returnsTaskWithSameId_secondTask() {
    myTask.save();
    Task secondTask = new Task("buy groceries", 1);
    secondTask.save();
    assertEquals(Task.find(secondTask.getId()), secondTask);
  }
  @Test
  public void equals_returnsTrueIfDescriptionsAretheSame() {
    Task secondTask = new Task("Mow the lawn", 1);
    assertTrue(myTask.equals(secondTask));
  }
@Test
  public void save_returnsTrueIfDescriptionsAretheSame() {
    myTask.save();
    assertTrue(Task.all().get(0).equals(myTask));
  }
  @Test
  public void save_assignsIdToObject() {
    myTask.save();
    Task savedTask = Task.all().get(0);
    assertEquals(myTask.getId(), savedTask.getId());
  }
  @Test
  public void save_savesCategoryIdIntoDB_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    myTask = new Task("Mow the lawn", myCategory.getId());
    myTask.save();
    Task savedTask = Task.find(myTask.getId());
    assertEquals(savedTask.getCategoryId(), myCategory.getId());
  }
}
