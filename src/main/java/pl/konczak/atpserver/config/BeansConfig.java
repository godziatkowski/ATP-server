package pl.konczak.atpserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.konczak.morsecodetranslator.IMorseCodeTranslator;
import pl.konczak.morsecodetranslator.MorseCodeTranslator;

@Configuration
public class BeansConfig {

    @Bean
    public IMorseCodeTranslator morseCodeTranslator() {
        return new MorseCodeTranslator();
    }
}
