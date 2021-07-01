package com.example.breakingbadchallenge

import androidx.activity.result.contract.ActivityResultContracts
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import com.example.breakingbadchallenge.ui.CharacterAdapter
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest{

    @get:Rule @JvmField @Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)



    @Test
    fun selectCharacter(){

        ActivityResultContracts.StartActivityForResult()

        onView(withId(R.id.rvCharacters)).perform(click())

    }
}