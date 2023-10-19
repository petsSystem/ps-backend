package br.com.petshop.system.poc;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sys/poc")
public class PocController {

    @Autowired private PocService pocService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public PocEntity create(@RequestBody PocDto request) {
        return pocService.create(request);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<PocDto> getAll (@RequestParam("lat") Double lat,
                                @RequestParam("lon") Double lon,
                                @RequestParam("radius") Double radius) {
        return pocService.findAround(lat, lon, radius);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public PocDto activate(@PathVariable UUID id, @RequestBody JsonPatch patch) {
        return pocService.patchMethod(id, patch);
    }

}
