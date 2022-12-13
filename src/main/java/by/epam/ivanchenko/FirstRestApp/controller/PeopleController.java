package by.epam.ivanchenko.FirstRestApp.controller;

import by.epam.ivanchenko.FirstRestApp.model.Person;
import by.epam.ivanchenko.FirstRestApp.service.PersonService;
import by.epam.ivanchenko.FirstRestApp.util.PersonErrorResponse;
import by.epam.ivanchenko.FirstRestApp.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;

    @Autowired
    public PeopleController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public List<Person> getPeople() {
        return personService.findAll();                                                                  // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable("id") int id) {
        return personService.findOne(id);
    }

    @ExceptionHandler
    // Метод, который ловит исключение(наше конкретное) и возвращает JSON
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException exp) {
        PersonErrorResponse response = new PersonErrorResponse("Person with this ID wasn't found", System.currentTimeMillis());
        // В HTTP-ответе будет тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);                                       // ошибка 404
    }
}
