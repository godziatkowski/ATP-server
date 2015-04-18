package pl.konczak.atpserver.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.konczak.atpserver.db.entity.Translation;
import pl.konczak.atpserver.db.repo.ITranslationRepository;

@RestController
@RequestMapping("/api/translation")
public class TranslationAPI {

    @Autowired
    private ITranslationRepository translationRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Translation> list() {
        return translationRepository.findAll();
    }

    @RequestMapping(value = "/api/translation/{translationId}",
                    method = RequestMethod.GET)
    public HttpEntity<Translation> get(@PathVariable long translationId) {
        Translation translation = translationRepository.findOne(translationId);

        if (translation == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(translation, HttpStatus.OK);
        }
    }
}
