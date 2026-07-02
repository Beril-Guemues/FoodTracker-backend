package htw.webtech.foodtracker.foodentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class FoodEntryController {

    @Autowired
    private FoodEntryService service;

    // 1. ALLE Einträge (mit DTO)
    @GetMapping("/foodentries")
    public List<FoodEntryDTO> getAllEntries() {
        return service.getAllEntries().stream()
                .map(FoodEntryDTO::new)
                .collect(Collectors.toList());
    }

    // 2. Einträge nach Datum (mit DTO)
    @GetMapping("/foodentries/date")
    public List<FoodEntryDTO> getEntriesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getEntriesByDate(date).stream()
                .map(FoodEntryDTO::new)
                .collect(Collectors.toList());
    }

    // 3. Eintrag nach ID (mit DTO)
    @GetMapping("/foodentries/{id}")
    public FoodEntryDTO getEntryById(@PathVariable Long id) {
        FoodEntry entry = service.getEntryById(id);
        return new FoodEntryDTO(entry);
    }

    // 4. Eintrag erstellen (mit DTO)
    @PostMapping("/foodentries")
    public FoodEntryDTO createEntry(@RequestBody FoodEntry entry) {
        FoodEntry saved = service.saveEntry(entry);
        return new FoodEntryDTO(saved);
    }

    // 5. Eintrag aktualisieren (mit DTO)
    @PutMapping("/foodentries/{id}")
    public FoodEntryDTO updateEntry(@PathVariable Long id, @RequestBody FoodEntry entry) {
        FoodEntry updated = service.updateEntry(id, entry);
        return new FoodEntryDTO(updated);
    }

    // 6. Eintrag löschen
    @DeleteMapping("/foodentries/{id}")
    public void deleteEntry(@PathVariable Long id) {
        service.deleteEntry(id);
    }
}