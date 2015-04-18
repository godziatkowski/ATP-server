package pl.konczak.atpserver.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.konczak.atpserver.db.entity.Translation;

public interface ITranslationRepository
        extends JpaRepository<Translation, Long> {

}
