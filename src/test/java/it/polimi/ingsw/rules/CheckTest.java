package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.utility.NotInstantiableClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckTest {

    @Test
    void construction() {
        assertThrows(NotInstantiableClass.class, ()-> new Check());
    }
}