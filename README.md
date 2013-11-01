jdbcUnit
========

JDBC unit is a tool that plugs a recorder in the Java/JDBC framework. It records and plays back SQL statements sent to the database manager.

What can we do with this tool?

* use data that is no longer in the database anymore or that never was
* write code without a connection to the database

The current implementation does not support INSERT/UPDATE/DELETE statements. Meaning that, if the same select statement is executed twice, the same result set is returned, even if some records were added or changed between in between.

Overview
--------

The jdbcUnit system records all the activity for the registered JDBC drivers. While recording, each time the same SQL statement is executed, the recorder returns the previously recorded results set.

>The current version records only SQL statements. So, returning the previous result set or number of affected records each time the same query is preformed does not lead to errors.

>Both prepared and direct statements are supported.

Once a set of SQL statements is recorded the same application can switch to playback mode. The actual database is not accessed anymore, an exception is raised if an unknown query is executed.

### Quick Start

Here we just show a very quick step by step of jdbcUnit usage. For detailed explanation please refer to following parts.

#### Recording

Assuming we wrote a book store application, using the JDBC/ODBC driver. Let's record the new book search, the ones bought this year.

1. setup jdbcUnit to record activity for the desired database

    ```java
    JDBCUnit.registerDriver("sun.jdbc.odbc.JdbcOdbcDriver");
    ``````
    > jdbcUnit does not allow to record the access to one database only, as soon as one registers a driver all the connections are recorded. We think client code wants to record all the database activity...

2. create a store object (a store object is an interface that exposes file-like behavior) where jdbcUnit will save the recorded data

    ```java
    File csv = new File("test-data/test-data.csv");
    
    Store store = new JavaFile(csv);
    
    CSVMedia media = new CSVMedia(store);
    ``````
    > Here the activity is to be saved to CSV (comma separated values) format files, the "test-data.csv" is the repository (aka toc) that refers to other files

3. create and set the recorder that jdbcUnit is going to use, here we use a media recorder. And start the unit so that it is ready to record or play back.

    ```java
    JDBCUnit.start(new MediaRecorder(media));
    ``````

4. start recording, enter the recording mode

    ```java
      JDBCUnit.record();
    ``````

5. when a SQL statement is executed the recorder saves the result set

    ```java
      PreparedStatement stmt = connection.prepareStatement(
                              "SELECT * FROM books WHERE buy_date >= ? ");

      java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
      java.sql.Date jan1 = new java.sql.Date(now.getYear(), 1, 1); 

      stmt.setDate(1, jan1);

      ResultSet rs = stmt.executeQuery();

      ...
    ``````

    >Executing the same code again won't access the database, the recorder is somehow playing back.

    >To find the previous result sets the recording system use a key made of the database url and the SQL statement. The SQL statement is normalized so that syntactic changes to the satements don't result in error (e.g. SELECT * FROM BOOKS is equivalent to select * FROM books.

    >The code shown here to access the database remains unchanged using jdbcUnit or not. We show it here only to trace the lifecycle of a recording session.

6. at the end of the application, stop the recording

    ```java
    JDBCUnit.stop();
    ``````

7. at this stage a new folder named "test-data" was created in the current directory that contains a file named "test-data.csv". This file contains one entry with the "SELECT * FROM BOOKS" linked to another file named "test-data.1.csv". The later file contains the result from the query. 

    >The sequence of JDBCUnit usage is important, see the unit usage [sequence test](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestUnitSequence.java)

#### Playing back

Now that we have a recorded database activity we just can play back.

1. first set the right recorder to jdbcUnit, see previous section

2. switch to playback mode

    ```java
    JDBCUnit.replay();
    ``````

3. when an SQL statement is executed the recorder returns the result set saved from the recording sesssion

    ```java
    ...
    ResultSet rs = stmt.executeQuery();
    ...
    ``````

Architecture
------------

jdbcUnit uses a recorder paradigm.

So, the `JDBCUnit` class is the actor that knows how to capture database activity and provide the current `Recorder`.

The `JDBCUnit` actor installs a fictious JDBC driver (`DriverWrapper`).

The `DriverWrapper` creates `ConnectionWrapper` objects each time a connection is needed.

The `ConnectionWrapper` holds some actual connection and provides `StatementWrapper`s when a statement is create or a `PreparedStatementWrapper`s when a prepared statement is created.

When a statement is executed, the statement wrapper delegates the recording to the current `Recorder`. The picture is, if the _jdbcUnit_ is in recording mode:

* check to see if the track (the result set) is already recorded,
* if the track does not exist execute the query and add the result
* otherwise, ask the recorder the previous result 

If the _jdbcUnit_ is in playback mode:

* ask the recorder the previous result 

The `JDBCUnit` actor, for convenience, also knows how to work with a given `Store`. A `Store` is something like a file, that can be feed with any content and can have children stores. If `JDBCUnit` is given a `Store` it uses the `MediaManager` to get a media able to handle the store, then creates a recorder that uses medias... a `MediaRecorder` object.

`MediaRecorder` objects know how to handle a recording support called a `Media`.

A `Media` is used by the recorder to write tracks (to SQL statement results) and get them back.

Currently two `Media` implementations exist: `CSVMedia` and `MediaStack`.

A `CSVMedia` creates several text files (in CSV format) to store all the result sets. One last file is created to hold the mapping between statements and the files where the results are stored.

A `MediaStack` uses a stack of medias, the top one is one that records new result sets. The stack is used only to retrieve recorded data (from top to bottom).

Availabe tests
--------------

Two categories of tests exist: the classic unit tests, that directly test a class implementation and a conformance test suite.

### Available unit tests

* [TestConnectionWrapping.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestConnectionWrapping.java)
* [TestStatementWrapping.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestStatementWrapping.java)
* [TestUnitSequence.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestUnitSequence.java)
* [TestRecording.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestRecording.java)
* [TestRecordingPreparedStatements.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestRecordingPreparedStatements.java)
* [TestRecordingReusesResults.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestRecordingReusesResults.java)
* [TestPlaybackMode.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestPlaybackMode.java)
* [TestReplaying.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestReplaying.java)
* [TestReplayingPreparedStatements.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestReplayingPreparedStatements.java)
* [TestMemoryUsage.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestMemoryUsage.java)
* [TestMediaStack.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestMediaStack.java)
* [TestCSVMediaConformance.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestCSVMediaConformance.java)
* [TestMediaStackConformance.java](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/TestMediaStackConformance.java)
* [TestCSVFileIterator.java](https://github.com/jeanlazarou/jdbcunit/tree/master/test/com/ap/jdbcunit/csv/TestCSVFileIterator.java)
* [TestCSVMedia.java](https://github.com/jeanlazarou/jdbcunit/tree/master/test/com/ap/jdbcunit/csv/TestCSVMedia.java)
* [TestClearingCSVMedia.java](https://github.com/jeanlazarou/jdbcunit/tree/master/test/com/ap/jdbcunit/csv/TestClearingCSVMedia.java)

### Conformance tests

The conformance tests suite test if a class, that implements the `Media` interface, does fullfill its contract.
All you need to provide is a `MediaFixture` to validate a new `Media` implementation.
Two media implementations exist in _jdbcUnit_ and both do conform (see [CSV Media fixture](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/fixtures/CSVMediaFixture.java) and [CSV Media Stack fixture](https://github.com/jeanlazarou/jdbcunit/blob/master/test/com/ap/jdbcunit/fixtures/MediaStackFixture.java)). Conformance tests are:

* [TestClearMedia.java](https://github.com/jeanlazarou/jdbcunit/tree/master/test/com/ap/jdbcunit/conformance/TestClearMedia.java)
* [TestMediaOpenState.java](https://github.com/jeanlazarou/jdbcunit/tree/master/test/com/ap/jdbcunit/conformance/TestMediaOpenState.java)
* [TestMediaUsage.java](https://github.com/jeanlazarou/jdbcunit/tree/master/test/com/ap/jdbcunit/conformance/TestMediaUsage.java)
* [TestMediaVisitor.java](https://github.com/jeanlazarou/jdbcunit/tree/master/test/com/ap/jdbcunit/conformance/TestMediaVisitor.java)

Task List
---------

- [ ] write documentation (in readme file) (@jeanlazarou)
- [ ] write test for callable statements
- [x] switch to HSQL database for tests
