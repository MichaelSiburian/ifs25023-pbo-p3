package usecase;

import domain.entity.SortOption;
import domain.entity.Todo;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.ITodoRepository;
import java.util.List;
import java.util.Optional;

/**
 * Use case yang menangani logika bisnis aplikasi todo.
 * Tidak melakukan I/O — hanya memproses data dan melempar checked exception
 * domain jika operasi tidak valid atau data tidak ditemukan.
 */
public class TodoUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final ITodoRepository todoRepository;

    public TodoUseCase(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /** Mengambil semua todo yang tersedia. */
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    /** Menambahkan todo baru setelah validasi. */
    public Todo addTodo(String title) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Judul todo tidak boleh kosong");
        }
        return todoRepository.save(title);
    }

    /** Menghapus todo berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeTodo(int id) throws EntityNotFoundException {
        boolean deleted = todoRepository.deleteById(id);
        if (!deleted) {
            throw new EntityNotFoundException(id);
        }
    }

    /**
     * Mengubah judul dan/atau status selesai todo.
     * Parameter {@code null} berarti field tersebut tidak diubah.
     */
    public void updateTodo(int id, String title, Boolean finished)
            throws EntityNotFoundException, ValidationException {
        Optional<Todo> found = todoRepository.findById(id);
        if (found.isEmpty()) {
            throw new EntityNotFoundException(id);
        }

        Todo todo = found.get();

        if (title != null) {
            if (title.trim().isEmpty()) {
                throw new ValidationException("Judul todo tidak boleh kosong");
            }
            todo.changeTitle(title);
        }
        if (finished != null) {
            todo.changeFinished(finished);
        }

        todoRepository.update(todo);
    }

    /** Mencari todo yang judulnya mengandung kata kunci (case-insensitive). */
    public List<Todo> searchTodos(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return todoRepository.findBy(todo -> todo.getTitle().toLowerCase().contains(lowerKeyword));
    }

    /** Mengurutkan todo sesuai kriteria {@link SortOption} yang dipilih. */
    public List<Todo> sortTodos(SortOption option) {
        return todoRepository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }
}
