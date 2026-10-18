package adapter.presenter;

import domain.entity.Todo;
import java.util.List;

/**
 * Presenter yang memformat data dari use case menjadi output layar.
 * Memisahkan logika tampilan dari entity, use case, dan view sehingga
 * format output bisa diubah tanpa menyentuh domain.
 */
public class TodoPresenter {

    /** Memformat satu todo menjadi baris teks untuk ditampilkan. */
    private String format(Todo todo) {
        String status = todo.isFinished() ? "[Selesai]" : "[Belum Selesai]";
        return String.format("[%d] %s | %s", todo.getId(), todo.getTitle(), status);
    }

    /**
     * Helper umum untuk menampilkan daftar todo.
     * Menampilkan pesan kosong jika list tidak berisi data.
     */
    private void printList(List<Todo> todos, String header, String emptyMessage) {
        System.out.println(header);

        if (todos.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }

        for (Todo todo : todos) {
            System.out.println(format(todo));
        }
    }

    /** Menampilkan daftar semua todo. */
    public void showTodos(List<Todo> todos) {
        printList(todos, "Daftar Todo:", "- Data todo belum tersedia!");
    }

    /** Menampilkan hasil pencarian berdasarkan kata kunci. */
    public void showSearchResults(List<Todo> todos, String keyword) {
        printList(todos, "Hasil Pencarian: \"" + keyword + "\"", "- Todo tidak ditemukan!");
    }

    /** Menampilkan daftar todo yang sudah diurutkan. */
    public void showSortedTodos(List<Todo> todos) {
        printList(todos, "Daftar Todo (Terurut):", "- Data todo belum tersedia!");
    }

    /** Menampilkan pesan sukses setelah menambah todo. */
    public void showAddSuccess(Todo todo) {
        System.out.printf("Berhasil menambah todo: %s%n", format(todo));
    }

    /** Menampilkan pesan sukses setelah menghapus todo. */
    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus todo.");
    }

    /** Menampilkan pesan gagal saat menghapus todo. */
    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus todo dengan ID: %d.%n", id);
    }

    /** Menampilkan pesan sukses setelah mengubah todo. */
    public void showUpdateSuccess() {
        System.out.println("Berhasil mengubah todo.");
    }

    /** Menampilkan pesan gagal saat mengubah todo. */
    public void showUpdateFailed(int id) {
        System.out.printf("[!] Gagal mengubah todo dengan ID: %d.%n", id);
    }

    /** Menampilkan pesan saat pilihan menu tidak dikenali. */
    public void showInvalidChoice() {
        System.out.println("[!] Pilihan tidak dimengerti.");
    }

    /** Menampilkan pesan saat ID yang dimasukkan tidak valid. */
    public void showInvalidId() {
        System.out.println("[!] ID tidak valid!");
    }

    /** Menampilkan pesan saat opsi pengurutan tidak valid. */
    public void showInvalidSortOption() {
        System.out.println("[!] Pilihan tidak valid!");
    }

    /** Menampilkan pesan saat input status selesai bukan y/n. */
    public void showInvalidFinishedStatus() {
        System.out.println("[!] Pilihan status selesai tidak valid (gunakan y/n)!");
    }
}
