package com.contrastofbeauty.tutorial.teststructure;

import org.junit.After;
import org.junit.Before;

import org.apache.log4j.Logger;

public abstract class AnimalBaseTest {

    private final static Logger logger = Logger.getLogger(AnimalBaseTest.class);

    @Before
    public void setUpSuper() throws Exception {
        logger.info("Run setUpSuper() of the super class.");
    }

    @After
    public void tearDownSuper() throws Exception {
        logger.info("Run tearDownSuper() of the super class.");
    }
}
