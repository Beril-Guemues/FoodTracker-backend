package htw.webtech.foodtracker.foodentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class FoodEntryService {

    @Autowired
    private FoodEntryRepository repository;

    // 1. Alle Einträge
    public List<FoodEntry> getAllEntries() {
        return repository.findAll();
    }

    // 2. Einträge nach Datum
    public List<FoodEntry> getEntriesByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return repository.findByDate(date);
    }

    // 3. Eintrag nach ID
    public FoodEntry getEntryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("FoodEntry not found with id: " + id));
    }

    // 4. Eintrag speichern (mit Validierung)
    public FoodEntry saveEntry(FoodEntry entry) {
        validateEntry(entry);
        return repository.save(entry);
    }

    // 5. Eintrag aktualisieren
    public FoodEntry updateEntry(Long id, FoodEntry entryDetails) {
        FoodEntry existing = getEntryById(id);

        existing.setProduct(entryDetails.getProduct());
        existing.setAmount(entryDetails.getAmount());
        existing.setDate(entryDetails.getDate());

        validateEntry(existing);
        return repository.save(existing);
    }

    // 6. Eintrag löschen
    public void deleteEntry(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("FoodEntry not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // 7. Validierung
    private void validateEntry(FoodEntry entry) {
        if (entry.getProduct() == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (entry.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (entry.getDate() == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }
}