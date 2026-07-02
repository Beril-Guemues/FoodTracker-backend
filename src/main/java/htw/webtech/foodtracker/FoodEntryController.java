package htw.webtech.foodtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class FoodEntryController {

    @Autowired
    private FoodEntryService service;

    @GetMapping("/foodentries")
    public List<FoodEntry> getAllEntries() {
        return service.getAllEntries();
    }

    @GetMapping("/foodentries/date")
    public List<FoodEntry> getEntriesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getEntriesByDate(date);
    }

    @PostMapping("/foodentries")
    public FoodEntry createEntry(@RequestBody FoodEntry entry) {
        return service.saveEntry(entry);
    }

    @DeleteMapping("/foodentries/{id}")
    public void deleteEntry(@PathVariable Long id) {
        service.deleteEntry(id);
    }
}