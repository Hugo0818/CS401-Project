package suite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import client.ClientTest;
import library.BookTest;
import library.LogTest;
import library.MemberTest;
import library.MessageTest;
import library.MovieTest;
import server.LibraryServerTest;

@Suite
@SelectClasses({
	ClientTest.class, 
	BookTest.class, 
	LogTest.class, 
	MemberTest.class, 
	MessageTest.class, 
	MovieTest.class, 
	LibraryServerTest.class
})

public class AllTests {}