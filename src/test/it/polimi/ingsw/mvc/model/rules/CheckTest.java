package it.polimi.ingsw.mvc.model.rules;

import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckTest {

    //Checks the construction of exceptions
    @Test
    void construction() {
        assertThrows(NotInstantiableClass.class, Check::new);
    }
}