package pl.konczak.atpserver.rest;

import pl.konczak.atpserver.rest.model.EnglishText;
import pl.konczak.atpserver.rest.model.MorseCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.konczak.morsecodetranslator.IMorseCodeTranslator;

@RestController
@RequestMapping("/api/morsecode")
public class MorseCodeAPI {

    @Autowired
    private IMorseCodeTranslator morseCodeTranslator;

    @RequestMapping(value = "/encode",
                    method = RequestMethod.POST)
    public HttpEntity<MorseCode> encode(@RequestBody EnglishText englishText) {
        String value = morseCodeTranslator.encode(englishText.getValue());
        MorseCode morseCode = new MorseCode();
        morseCode.setValue(value);

        return new ResponseEntity<>(morseCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/decode",
                    method = RequestMethod.POST)
    public HttpEntity<EnglishText> decode(@RequestBody MorseCode morseCode) {
        String value = morseCodeTranslator.decode(morseCode.getValue());
        EnglishText englishText = new EnglishText();
        englishText.setValue(value);

        return new ResponseEntity<>(englishText, HttpStatus.OK);
    }

}
