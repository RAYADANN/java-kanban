public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task(0, "Помыть посуду", "Помыть всю посуду в раковине", Status.NEW);
        Task task2 = new Task(0, "Сходить в магазин", "Купить продукты на ужин", Status.IN_PROGRESS);

        Epic epic1 = new Epic(0, "Организация дня рождения", "Запланировать день рождения, купить подарки, организовать вечеринку", Status.NEW);
        Subtask subtask1 = new Subtask(0, "Запланировать день рождения", "Выбрать дату, место, гостей", Status.NEW, epic1);
        Subtask subtask2 = new Subtask(0, "Купить подарки", "Выбрать подарки для всех гостей", Status.IN_PROGRESS, epic1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic(0, "Поездка на море", "Забронировать билеты, снять жилье, купить чемодан", Status.NEW);
        Subtask subtask3 = new Subtask(0, "Забронировать билеты", "Найти и забронировать авиабилеты", Status.DONE, epic2);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask3);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        System.out.println("Список задач: " + taskManager.getAllTasks());
        System.out.println("Список эпиков: " + taskManager.getAllEpics());
        System.out.println("Список подзадач: " + taskManager.getAllSubtasks());

        task1.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);

        System.out.println("Измененная задача 1: " + task1);
        System.out.println("Измененная подзадача 1: " + subtask1);
        System.out.println("Измененный эпик 1: " + epic1);

        taskManager.deleteTaskById(task1.getId());
        taskManager.deleteEpicById(epic2.getId());

        System.out.println("Список задач после удаления: " + taskManager.getAllTasks());
        System.out.println("Список эпиков после удаления: " + taskManager.getAllEpics());
        System.out.println("Список подзадач после удаления: " + taskManager.getAllSubtasks());
    }
}
