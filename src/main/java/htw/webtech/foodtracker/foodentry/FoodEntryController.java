package htw.webtech.foodtracker.foodentry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class FoodEntryController {

    private static final Logger logger = LoggerFactory.getLogger(FoodEntryController.class);

    @Autowired
    private FoodEntryService service;

    @GetMapping("/foodentries")
    public List<FoodEntryDTO> getAllEntries() {
        logger.info("GET /foodentries - Alle FoodEntries werden abgerufen");
        return service.getAllEntries().stream()
                .map(FoodEntryDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/foodentries/date")
    public List<FoodEntryDTO> getEntriesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        logger.info("GET /foodentries/date - FoodEntries für Datum werden abgerufen: {}", date);
        return service.getEntriesByDate(date).stream()
                .map(FoodEntryDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/foodentries/{id}")
    public FoodEntryDTO getEntryById(@PathVariable Long id) {
        logger.info("GET /foodentries/{} - FoodEntry mit ID wird abgerufen", id);
        FoodEntry entry = service.getEntryById(id);
        return new FoodEntryDTO(entry);
    }

    @PostMapping("/foodentries")
    public FoodEntryDTO createEntry(@RequestBody FoodEntry entry) {
        logger.info("POST /foodentries - Neuer FoodEntry wird erstellt: {}", entry);
        FoodEntry saved = service.saveEntry(entry);
        logger.info("POST /foodentries - FoodEntry erfolgreich erstellt mit ID: {}", saved.getId());
        return new FoodEntryDTO(saved);
    }

    @PutMapping("/foodentries/{id}")
    public FoodEntryDTO updateEntry(@PathVariable Long id, @RequestBody FoodEntry entry) {
        logger.info("PUT /foodentries/{} - FoodEntry wird aktualisiert: {}", id, entry);
        FoodEntry updated = service.updateEntry(id, entry);
        logger.info("PUT /foodentries/{} - FoodEntry erfolgreich aktualisiert", id);
        return new FoodEntryDTO(updated);
    }

    @DeleteMapping("/foodentries/{id}")
    public void deleteEntry(@PathVariable Long id) {
        logger.info("DELETE /foodentries/{} - FoodEntry wird gelöscht", id);
        service.deleteEntry(id);
        logger.info("DELETE /foodentries/{} - FoodEntry erfolgreich gelöscht", id);
    }
}