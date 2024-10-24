import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.Task;
import com.yandex.sprint_4.service.FileBackedTaskManager;
import com.yandex.sprint_4.service.Managers;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

 public static void main(String[] args) {

  try {
   FileBackedTaskManager.loadFromFile(new File("src/com/yandex/sprint_4/resources/Data.csv"));
  } catch (Exception ignored) {

  }


  FileBackedTaskManager taskManager = (FileBackedTaskManager) Managers.getDefault();
  if (taskManager.getDataTask() != null) {
   for (String line : taskManager.getDataTask()) {
    taskManager.fromString(line);
   }
  }

  Task task1 = new Task(1, "Помыть посуду", "Помыть всю посуду в раковине", Status.NEW,
          Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(100));
  Task task2 = new Task(2, "Сходить в магазин", "Купить продукты на ужин", Status.IN_PROGRESS,
          Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(400));

  Epic epic1 = new Epic(3, "Организация дня рождения", "Запланировать день рождения, " +
          "купить подарки, организовать вечеринку", Status.NEW);
  Subtask subtask1 = new Subtask(4, "Запланировать день рождения", "Выбрать дату, место, гостей",
          Status.NEW, epic1, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(50));
  Subtask subtask2 = new Subtask(5, "Купить подарки", "Выбрать подарки для всех гостей", Status.DONE,
          epic1, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(70));
  taskManager.createEpic(epic1);
  taskManager.createSubtask(subtask1);
  taskManager.createSubtask(subtask2);

  Epic epic2 = new Epic(6, "Поездка на море", "Забронировать билеты, снять жилье, купить чемодан",
          Status.NEW);
  Subtask subtask3 = new Subtask(7, "Забронировать билеты", "Найти и забронировать авиабилеты",
          Status.DONE, epic2, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(50));
  taskManager.createEpic(epic2);
  taskManager.createSubtask(subtask3);

  taskManager.createTask(task1);
  taskManager.createTask(task2);

  System.out.println("Список задач: " + taskManager.getAllTasks());
  System.out.println("Список эпиков: " + taskManager.getAllEpics());
  System.out.println("Список подзадач: " + taskManager.getAllSubtasks());

  task1.setStatus(Status.DONE);
  subtask1.setStatus(Status.IN_PROGRESS);
  subtask2.setStatus(Status.DONE);

  System.out.println("Измененная задача 1: " + task1);
  System.out.println("Измененная подзадача 1: " + subtask1);
  System.out.println("Измененный эпик 1: " + epic1);

  taskManager.deleteEpicById(epic2.getId());

  System.out.println("Список задач после удаления: " + taskManager.getAllTasks());
  System.out.println("Список эпиков после удаления: " + taskManager.getAllEpics());
  System.out.println("Список подзадач после удаления: " + taskManager.getAllSubtasks());

  System.out.println("Список эпиков после удаления всех подзадач: " + taskManager.getAllEpics());

  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getEpicById(epic1.getId());
  taskManager.getTaskById(task2.getId());
  taskManager.getEpicById(epic1.getId());

  System.out.println("Список по приоритету по времени: " + taskManager.getPrioritizedTasks());

  System.out.println("История поиска: " + taskManager.getHistory());
 }
}
