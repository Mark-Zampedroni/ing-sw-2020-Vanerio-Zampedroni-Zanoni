package it.polimi.ingsw.MVC.model.rules;

import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckTest {

    @Test
    void construction() {
        assertThrows(NotInstantiableClass.class, Check::new);
    }
}