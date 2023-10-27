package com.example.ratatouille_android;
import static org.junit.Assert.assertEquals;


import com.example.ratatouille_android.models.User;
import com.example.ratatouille_android.views.CreaProdottoActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestProva {
    User user;

    @Before
    public void init(){
        user = new User();
    }

    @Test
    public void autoCompilazione() {
        assertEquals(0, user.getIdUtente());
    }

}