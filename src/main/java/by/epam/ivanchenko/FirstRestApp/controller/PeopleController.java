package by.epam.ivanchenko.FirstRestApp.controller;

import by.epam.ivanchenko.FirstRestApp.dto.PersonDTO;
import by.epam.ivanchenko.FirstRestApp.model.Person;
import by.epam.ivanchenko.FirstRestApp.service.PersonService;
import by.epam.ivanchenko.FirstRestApp.util.PersonErrorResponse;
import by.epam.ivanchenko.FirstRestApp.util.PersonNotCreatedException;
import by.epam.ivanchenko.FirstRestApp.util.PersonNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;
    private final ModelMapper modelMapper;

    @Autowired
    public PeopleController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<PersonDTO> getPeople() {
        return personService.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList());                                                                  // Jackson конвертирует эти объекты в JSON
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id) {
        return convertToPersonDTO(personService.findOne(id));
    }

    @PostMapping()
    // Можно возвращать объект Person вместо ResponseEntity<HttpStatus>. Jackson сконвертирует аргументы в Java-объект класса Person
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {                                                               // Все ошибки  из bindingResult объединяем в строку
                errorMessage.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new PersonNotCreatedException(errorMessage.toString());
        }

        personService.save(convertToPerson(personDTO));

        return ResponseEntity.ok(HttpStatus.OK);                                                         // Отправляем Http-ответ с пустым телом и статусом 200
    }

    @ExceptionHandler
    // Метод, который ловит исключение(наше конкретное) и возвращает JSON
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException exp) {
        PersonErrorResponse response = new PersonErrorResponse("Person with this ID wasn't found", System.currentTimeMillis());
        // В HTTP-ответе будет тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);                                       // ошибка 404
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException exp) {
        PersonErrorResponse response = new PersonErrorResponse(exp.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        //ModelMapper modelMapper = new ModelMapper();          вместо этого внедрили его в конфиг. класс Spring - singleton будет
        return modelMapper.map(personDTO, Person.class);
        // Указываем, что хотим смапить объект personDTO в объект класса Person (вместо множества getterов и setterов)
    }

    private PersonDTO convertToPersonDTO(Person person) {                                           // Обратное преобразование при отправке данных
        return modelMapper.map(person, PersonDTO.class);
    }
}
