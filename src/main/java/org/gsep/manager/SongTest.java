package org.gsep.manager;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SongTest {
	private String f1= getClass().getResource("/test.txt").getFile();
	
	Song s1 = new Song();
	
	@Test
	public void testReadFile() throws IOException {
		
		assertEquals("SongName", s1.readFile(f1,StandardCharsets.UTF_8 ));
	}

}
