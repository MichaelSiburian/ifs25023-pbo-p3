package framework.view;

import adapter.presenter.TodoPresenter;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import framework.util.InputUtil;
import usecase.TodoUseCase;

/**
 * Tampilan konsol aplikasi todo.
 * Menerima input user, memanggil use case, dan menangani exception domain secara terstruktur.
 * Layer ini tidak mengandung logika bisnis — hanya menangani interaksi user.
 */
public class TodoView {
    /** Use case yang menjalankan operasi bisnis. */
    private final TodoUseCase todoUseCase;

    /** Presenter yang memformat hasil ke output layar. */
    private final TodoPresenter presenter;

    public TodoView(TodoUseCase todoUseCase, TodoPresenter presenter) {
        this.todoUseCase = todoUseCase;
        this.presenter = presenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            // Tampilkan daftar todo terkini sebelum menu
            presenter.showTodos(todoUseCase.getAllTodos());
            printMenu();

            String input = InputUtil.input("Pilih");
            System.out.println();
            switch (input) {
                case "1" -> addTodo();
                case "2" -> updateTodo();
                case "3" -> searchTodo();
                case "4" -> sortTodo();
                case "5" -> removeTodo();
                case "x" -> running = false;
                default -> presenter.showInvalidChoice();
            }

            if (running) {
                System.out.println();
            }
        }
    }

    /** Mencetak opsi menu ke layar. */
    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Tambah");
        System.out.println("2. Ubah");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Hapus");
        System.out.println("x. Keluar");
    }

    /** Form tambah todo baru. */
    private void addTodo() {
        System.out.println("[Menambah Todo]");
        String title = InputUtil.input("Judul (x Jika Batal)");

        if (!title.equals("x")) {
            try {
                presenter.showAddSuccess(todoUseCase.addTodo(title));
            } catch (ValidationException e) {
                // Validasi judul kosong
            }
        }
    }

    /** Form hapus todo berdasarkan ID. */
    private void removeTodo() {
        System.out.println("[Menghapus Todo]");
        String strIdTodo = InputUtil.input("[ID Todo] yang dihapus (x Jika Batal)");

        if (strIdTodo.equals("x")) {
            return;
        }

        try {
            int id = parseId(strIdTodo);
            todoUseCase.removeTodo(id);
            presenter.showRemoveSuccess();
        } catch (ValidationException e) {
            presenter.showInvalidId();
        } catch (EntityNotFoundException e) {
            presenter.showRemoveFailed(e.getId());
        }
    }

    /** Form ubah judul dan/atau status todo. */
    private void updateTodo() {
        System.out.println("[Mengubah Todo]");
        String strIdTodo = InputUtil.input("ID Todo yang diubah (x Jika Batal)");

        if (strIdTodo.equals("x")) {
            return;
        }

        int id;
        try {
            id = parseId(strIdTodo);
        } catch (ValidationException e) {
            presenter.showInvalidId();
            return;
        }

        String newTitle = InputUtil.input("Judul Baru (Kosongkan jika tidak ingin mengubah)");
        String strFinished = InputUtil.input("Selesai? (y/n, Kosongkan jika tidak ingin mengubah)");

        // null berarti field tersebut tidak diubah
        String title = newTitle.isBlank() ? null : newTitle;
        Boolean finished;
        try {
            finished = parseFinished(strFinished);
        } catch (ValidationException e) {
            presenter.showInvalidFinishedStatus();
            return;
        }

        try {
            todoUseCase.updateTodo(id, title, finished);
            presenter.showUpdateSuccess();
        } catch (EntityNotFoundException e) {
            presenter.showUpdateFailed(e.getId());
        } catch (ValidationException e) {
            presenter.showInvalidFinishedStatus();
        }
    }

    /** Form cari todo berdasarkan kata kunci. */
    private void searchTodo() {
        System.out.println("[Mencari Todo]");
        String keyword = InputUtil.input("Kata Kunci (x Jika Batal)");

        if (!keyword.equals("x")) {
            presenter.showSearchResults(todoUseCase.searchTodos(keyword), keyword);
        }
    }

    /** Form urutkan todo berdasarkan pilihan user. */
    private void sortTodo() {
        System.out.println("[Mengurutkan Todo]");
        System.out.println("Pilihan Pengurutan:");
        System.out.println("1. Judul (A-Z)");
        System.out.println("2. Judul (Z-A)");
        System.out.println("3. Status (Selesai -> Belum)");
        System.out.println("4. Status (Belum -> Selesai)");
        System.out.println("x. Batal");

        String input = InputUtil.input("Pilih");
        if (input.equals("x")) {
            return;
        }

        // Konversi input angka ke enum domain
        SortOption option = mapSortOption(input);
        if (option == null) {
            presenter.showInvalidSortOption();
            return;
        }

        presenter.showSortedTodos(todoUseCase.sortTodos(option));
    }

    /**
     * Mengonversi input string menjadi ID.
     *
     * @return ID jika valid
     * @throws ValidationException jika string bukan format angka
     */
    private int parseId(String value) throws ValidationException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ValidationException("ID harus berupa angka numerik");
        }
    }

    /**
     * Mengonversi input y/n menjadi Boolean.
     *
     * @return true/false jika valid, null jika kosong
     * @throws ValidationException jika format status tidak dikenali
     */
    private Boolean parseFinished(String value) throws ValidationException {
        if (value.isBlank()) {
            return null;
        }
        if (value.equalsIgnoreCase("y")) {
            return true;
        }
        if (value.equalsIgnoreCase("n")) {
            return false;
        }
        throw new ValidationException("Format status selesai tidak valid");
    }

    /** Memetakan pilihan menu (1-4) ke {@link SortOption} domain. */
    private SortOption mapSortOption(String input) {
        return switch (input) {
            case "1" -> SortOption.TITLE_ASC;
            case "2" -> SortOption.TITLE_DESC;
            case "3" -> SortOption.FINISHED_FIRST;
            case "4" -> SortOption.UNFINISHED_FIRST;
            default -> null;
        };
    }
}
