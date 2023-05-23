package com.template.repository;


import com.template.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template,Long> {

    public Optional<List<Template>> findByCode(String templateCode);
	public Optional<List<Template>> findByCodeAndType(String value, String sms);
    public Optional<List<Template>> findByDeletedOn(LocalDateTime dateTime);
    Optional<Template> findByIdAndDeletedOn(Long id,LocalDateTime dateTime);
    Optional<List<Template>> findByCodeAndTypeAndDeletedOn(String code, String type, LocalDateTime dateTime);
    Optional<List<Template>> findByCodeAndDeletedOn(String code, LocalDateTime dateTime);
    Optional<List<Template>> findByCodeAndTypeAndDeletedOnAndStatus(String code, String type, LocalDateTime dateTime,Boolean status);
   Optional<Template> findByCodeAndTypeAndDeletedOnAndStatusAndArchive(String code, String type, LocalDateTime dateTime,Boolean status,Boolean archive);

}
