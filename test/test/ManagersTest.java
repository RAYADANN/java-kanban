package test;

import com.yandex.sprint_4.service.HistoryManager;
import com.yandex.sprint_4.service.InMemoryHistoryManager;
import com.yandex.sprint_4.service.InMemoryTaskManager;
import com.yandex.sprint_4.service.Managers;
import com.yandex.sprint_4.service.TaskManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    void testGetDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
        assertTrue(taskManager instanceof InMemoryTaskManager);
    }

    @Test
    void testGetDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
        assertTrue(historyManager instanceof InMemoryHistoryManager);
    }


}