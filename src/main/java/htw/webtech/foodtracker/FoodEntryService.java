package htw.webtech.foodtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class FoodEntryService {

    @Autowired
    private FoodEntryRepository repository;

    public List<FoodEntry> getAllEntries() {
        return repository.findAll();
    }

    public List<FoodEntry> getEntriesByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public FoodEntry saveEntry(FoodEntry entry) {
        return repository.save(entry);
    }

    public void deleteEntry(Long id) {
        repository.deleteById(id);
    }
}