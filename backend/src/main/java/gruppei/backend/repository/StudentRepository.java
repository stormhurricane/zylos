package gruppei.backend.repository;

import gruppei.backend.database.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findStudentByEmail(String email);

    Student findStudentByMatrikelnr(int matrikelnr);

    Student findStudentByVornameAndNachname(String vorname, String nachname);

}
