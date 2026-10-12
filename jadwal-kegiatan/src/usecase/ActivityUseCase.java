package usecase;

import domain.entity.Activity;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IActivityRepository;
import java.util.List;

/**
 * Use case yang menangani logika bisnis aplikasi jadwal kegiatan.
 * Tidak melakukan I/O — hanya memproses data dan melempar checked exception
 * domain jika operasi tidak valid atau data tidak ditemukan.
 */
public class ActivityUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IActivityRepository activityRepository;

    public ActivityUseCase(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /** Mengambil semua kegiatan yang tersedia. */
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    /** Menambahkan kegiatan baru setelah validasi judul. */
    public Activity addActivity(String title, String day, String time) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Judul kegiatan tidak boleh kosong");
        }
        return activityRepository.save(title, day, time);
    }

    /** Menghapus kegiatan berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeActivity(int id) throws EntityNotFoundException {
        boolean deleted = activityRepository.deleteById(id);
        if (!deleted) {
            throw new EntityNotFoundException(id);
        }
    }

    /**
     * Mengubah judul, hari, dan/atau waktu kegiatan.
     * Parameter {@code null} berarti field tersebut tidak diubah.
     *
     * @throws EntityNotFoundException jika kegiatan tidak ditemukan
     */
    public void updateActivity(int id, String title, String day, String time)
            throws EntityNotFoundException {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        if (title != null) {
            activity.changeTitle(title);
        }
        if (day != null) {
            activity.changeDay(day);
        }
        if (time != null) {
            activity.changeTime(time);
        }

        activityRepository.update(activity);
    }

    /** Mencari kegiatan yang judulnya mengandung kata kunci (case-insensitive). */
    public List<Activity> searchActivities(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return activityRepository.findBy(
                activity -> activity.getTitle().toLowerCase().contains(lowerKeyword));
    }

    /** Mengurutkan kegiatan sesuai kriteria {@link SortOption} yang dipilih. */
    public List<Activity> sortActivities(SortOption option) {
        return activityRepository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }
}
