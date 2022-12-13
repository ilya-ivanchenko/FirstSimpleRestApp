package by.epam.ivanchenko.FirstRestApp.repository;

import by.epam.ivanchenko.FirstRestApp.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

}
