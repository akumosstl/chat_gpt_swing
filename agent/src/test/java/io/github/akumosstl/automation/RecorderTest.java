package io.github.akumosstl.automation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RecorderTest {

    @Before
    public void setUp() {
        // Initialize any necessary components or state before each test
        Recorder.startRecording();
    }

    @After
    public void tearDown() {
        // Clean up after each test
        Recorder.stopRecording();
    }

    @Test
    public void testStartRecording() {

    }

}