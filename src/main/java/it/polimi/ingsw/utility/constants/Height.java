package it.polimi.ingsw.utility.constants;

import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;

/**
 * Constants of height for the towers
 */
public final class Height {

    public static final int GROUND = 0;
    public static final int BOTTOM = 1;
    public static final int MID = 2;
    public static final int TOP = 3;

    /**
     * Private constructor, the class can't be instantiated
     *
     * @throws NotInstantiableClass when instantiated
     */
    private Height() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

}
