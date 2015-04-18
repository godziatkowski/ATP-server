package pl.konczak.atpserver.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.konczak.atpserver.db.entity.Translation;
import pl.konczak.atpserver.db.repo.ITranslationRepository;
import pl.konczak.atpserver.rest.model.EnglishText;
import pl.konczak.atpserver.rest.model.MorseCode;
import pl.konczak.morsecodetranslator.IMorseCodeTranslator;

@RestController
@RequestMapping("/api/morsecode")
public class MorseCodeAPI {

    @Autowired
    private IMorseCodeTranslator morseCodeTranslator;

    @Autowired
    private ITranslationRepository translationRepository;

    @RequestMapping(value = "/encode",
                    method = RequestMethod.POST)
    public HttpEntity<MorseCode> encode(@RequestBody EnglishText englishText) {
        String value = morseCodeTranslator.encode(englishText.getValue());
        MorseCode morseCode = new MorseCode();
        morseCode.setValue(value);

        Translation translation = new Translation(englishText, morseCode);
        translationRepository.save(translation);

        return new ResponseEntity<>(morseCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/decode",
                    method = RequestMethod.POST)
    public HttpEntity<EnglishText> decode(@RequestBody MorseCode morseCode) {
        String value = morseCodeTranslator.decode(morseCode.getValue());
        EnglishText englishText = new EnglishText();
        englishText.setValue(value);

        Translation translation = new Translation(englishText, morseCode);
        translationRepository.save(translation);

        return new ResponseEntity<>(englishText, HttpStatus.OK);
    }

}
