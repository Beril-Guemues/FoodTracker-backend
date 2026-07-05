package htw.webtech.foodtracker.proxy;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/proxy")
@CrossOrigin(origins = "*")
public class ProxyController {

    @GetMapping("/openfoodfacts")
    public String proxy(@RequestParam String query) {
        try {
            String url = "https://de.openfoodfacts.org/cgi/search.pl?search_terms="
                    + query
                    + "&search_simple=1&action=process&json=1&page_size=10&lc=de";

            RestTemplate rest = new RestTemplate();
            return rest.getForObject(url, String.class);
        } catch (Exception e) {
            return "{}";
        }
    }
}