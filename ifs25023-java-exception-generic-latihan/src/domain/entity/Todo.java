package domain.entity;

/**
 * Entity inti yang merepresentasikan satu item todo.
 * Berada di layer domain — tidak bergantung pada layer lain dan bebas dari
 * urusan tampilan maupun penyimpanan.
 */
public class Todo {
    /** ID unik todo, tidak boleh diubah setelah dibuat. */
    private final int id;

    /** Judul atau deskripsi tugas. */
    private String title;

    /** Status penyelesaian: true = selesai, false = belum selesai. */
    private boolean finished;

    /** Membuat todo baru dengan status belum selesai. */
    public Todo(int id, String title) {
        this(id, title, false);
    }

    /** Membuat todo dengan status selesai yang sudah ditentukan. */
    public Todo(int id, String title, boolean finished) {
        this.id = id;
        this.title = title;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFinished() {
        return finished;
    }

    /** Mengubah judul todo. */
    public void changeTitle(String title) {
        this.title = title;
    }

    /** Mengubah status selesai todo. */
    public void changeFinished(boolean finished) {
        this.finished = finished;
    }
}
