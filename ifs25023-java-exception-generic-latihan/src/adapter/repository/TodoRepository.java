package adapter.repository;

import domain.entity.Todo;
import domain.repository.ITodoRepository;

/**
 * Implementasi repository todo menggunakan basis generic {@link InMemoryRepository}.
 */
public class TodoRepository extends InMemoryRepository<Todo, Integer> implements ITodoRepository {
    /** Penghitung ID otomatis, bertambah setiap kali todo baru disimpan. */
    private int idCounter = 0;

    public TodoRepository() {
        super(Todo::getId);
    }

    @Override
    public Todo save(String title) {
        Todo todo = new Todo(nextId(), title);
        return save(todo);
    }

    /** Menghasilkan ID unik berikutnya. */
    private int nextId() {
        return ++idCounter;
    }
}
