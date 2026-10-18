import adapter.presenter.TodoPresenter;
import adapter.repository.TodoRepository;
import domain.repository.ITodoRepository;
import framework.view.TodoView;
import usecase.TodoUseCase;

/**
 * Titik masuk aplikasi (Composition Root).
 * Semua dependency antar layer disusun di sini — satu-satunya tempat
 * yang mengetahui implementasi konkret dari setiap interface.
 */
public class App {
    public static void main(String[] args) {
        // Layer adapter: implementasi konkret repository (penyimpanan in-memory)
        ITodoRepository todoRepository = new TodoRepository();

        // Layer usecase: logika bisnis, hanya bergantung pada interface repository
        TodoUseCase todoUseCase = new TodoUseCase(todoRepository);

        // Layer adapter: presenter untuk memformat output ke layar
        TodoPresenter todoPresenter = new TodoPresenter();

        // Layer framework: UI konsol yang menerima input user
        TodoView todoView = new TodoView(todoUseCase, todoPresenter);

        // Menjalankan loop menu utama aplikasi
        todoView.show();
    }
}
