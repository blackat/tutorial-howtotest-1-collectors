package com.contrastofbeauty.tutorial.teststructure;

import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.rules.ExpectedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CatPersistTest {

    private final static Logger logger = Logger.getLogger(CatPersistTest.class);

    private static final String CAT_FELIX = "felix";
    public static final long CAT_ID = 1L;

    private static EntityManagerFactory emf;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private EntityManager em;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        logger.info("SetUpBeforeClass.");
        emf = Persistence.createEntityManagerFactory("h2");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        logger.info("TearDownAfterClass.");
        if (emf != null) {
            emf.close();
        }
    }

    @Before
    public void setUp() throws Exception {
        logger.info("SetUp().");
        em = emf.createEntityManager();
        insertTestData();
    }

    @After
    public void tearDown() throws Exception {

        logger.info("TearDown().");

        if (em != null) {
            removeTestData();
            em.close();
        }
    }

    @Test
    public void testSave() throws Exception {

        logger.info("A test.");

        String query = "select c from Cat c";
        List<Cat> houses = em.createQuery(query).getResultList();
        assertNotNull(houses);
        assertEquals(1, houses.size());

        Cat cat = houses.get(0);

        assertNotNull(cat);

    }

    @Test
    public void testVoid() throws Exception {

        logger.info("Another test.");
    }

    private void insertTestData() throws Exception {

        logger.info("...insert data");

        Cat cat = new Cat();
        cat.setId(CAT_ID);
        cat.setName(CAT_FELIX);

        em.getTransaction().begin();
        em.persist(cat);
        em.getTransaction().commit();
    }

    private void removeTestData() throws Exception {

        logger.info("...remove data");

        em.getTransaction().begin();

        Cat cat = em.find(Cat.class, CAT_ID);
        if(cat != null) {
            em.remove(cat);
        }
        em.getTransaction().commit();
    }
}
