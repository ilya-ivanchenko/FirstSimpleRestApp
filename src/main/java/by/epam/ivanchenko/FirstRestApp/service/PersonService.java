package by.epam.ivanchenko.FirstRestApp.service;

import by.epam.ivanchenko.FirstRestApp.model.Person;
import by.epam.ivanchenko.FirstRestApp.repository.PersonRepository;
import by.epam.ivanchenko.FirstRestApp.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final  PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElseThrow(PersonNotFoundException::new);
    }


    // принимаем JSON в контроллере (объект Person) и сохраняем в БД
    @Transactional
    public void save(Person person) {
         enrichPerson(person);                                                                     // Добавляем в объект Person поля, которые не пришли от клиента и которые надо добавить на сервере
         personRepository.save(person);
    }

    public void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
    }


}
