package com.example.scheduler;

import org.junit.Test;

import static org.junit.Assert.*;

public class MemberTest {

    @Test
    public void getFirstName() {
        String expectedReturn = "David";
        String returned;
        Member member = new Member();

        member.setFirstName("David");

        returned = member.getFirstName();

        assertEquals(expectedReturn, returned);
    }
}