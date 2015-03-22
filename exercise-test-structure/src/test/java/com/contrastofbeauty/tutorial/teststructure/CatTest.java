package com.contrastofbeauty.tutorial.teststructure;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CatTest extends AnimalBaseTest {

    private final static Logger logger = Logger.getLogger(CatTest.class);

    private Cat cat;

    /**
     * This method will be run before any Test method to setup a new and fresh cat object.
     *
     * The @Before methods of superclasses will be run before those of the current class only
     * if the ones from superclass has unique names. No other ordering is defined within a class.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        logger.info("Run setUp() to create a new fresh cat.");
        cat = new Cat();
    }

    @Test
    public void testMethodOne() throws Exception {

        logger.info("Now the method testMethodOne can be run, everything has been initialized.");
    }

    @After
    public void tearDown() throws Exception {

        logger.info("Run tearDown() to free cat resource.");
        cat = null;
    }
}