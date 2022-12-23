package by.epam.ivanchenko.FirstRestApp.util;

public class PersonNotCreatedException extends RuntimeException {
    public PersonNotCreatedException (String message) {
        super(message);
    }
}
