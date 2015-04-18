package pl.konczak.atpserver.db.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import pl.konczak.atpserver.rest.model.EnglishText;
import pl.konczak.atpserver.rest.model.MorseCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Translation
        implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String englishText;

    private String morseCode;

    private LocalDateTime createdAt;

    protected Translation() {
    }

    public Translation(EnglishText englishText, MorseCode morseCode) {
        this.englishText = englishText.getValue();
        this.morseCode = morseCode.getValue();
        this.createdAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public String getEnglishText() {
        return englishText;
    }

    public String getMorseCode() {
        return morseCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
