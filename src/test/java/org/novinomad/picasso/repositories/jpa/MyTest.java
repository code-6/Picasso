package org.novinomad.picasso.repositories.jpa;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.novinomad.picasso.sandbox.EntityA;
import org.novinomad.picasso.sandbox.EntityARepository;
import org.novinomad.picasso.sandbox.EntityB;
import org.novinomad.picasso.sandbox.EntityBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyTest {

    @Autowired
    EntityARepository aRepository;

    @Autowired
    EntityBRepository bRepository;

    @Test
    void test() {

        EntityA a = new EntityA();

        a.setName("a entry");

        EntityB b = new EntityB();
        b.setName("b entry");

        b = bRepository.save(b);

        a.getEntityBWithValues().put(b, "val 1");

        a = aRepository.save(a);

        System.out.println(a);
        System.out.println(b);

    }
}
