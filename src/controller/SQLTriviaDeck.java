package controller;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A trivia deck designed to take input from an SQLite database.
 * @author Joseph Graves
 * @version Fall 2021
 */
public class SQLTriviaDeck implements TriviaDeck {

    /**
     * The list of questions in the deck.
     */
    private final List<Question> myQuestionList = new ArrayList<>();

    /**
     * Given a database file name, selects the Questions table from it to populate the List.
     * Must be a table created by the query:
     * CREATE TABLE "Questions" (
     *     "Question"    TEXT NOT NULL,
     *     "Truth"    TEXT NOT NULL,
     *     "Herring1"    TEXT NOT NULL,
     *     "Herring2"    TEXT NOT NULL,
     *     "Herring3"    TEXT NOT NULL,
     *     PRIMARY KEY("Question")
     * )
     * @param theDatabaseName database file name.
     */
    public SQLTriviaDeck(final String theDatabaseName) {
        final SQLiteDataSource data = createDatasource(theDatabaseName);
        loadListWithResults(data);
    }

    /**
     * Private constructor to make sure a database name is passed in.
     */
    private SQLTriviaDeck() {
        this(null);
    }

    /**
     * Creates a datasource object from a database name.
     * @param theDatabasename the name of the database.
     * @return a datasource object.
     */
    private SQLiteDataSource createDatasource(final String theDatabasename) {
        final SQLiteDataSource source = new SQLiteDataSource();
        try {
            source.setUrl("jdbc:sqlite:" + theDatabasename);
        } catch (final Exception exception) { //TODO Ask instructor for exception type.
            exception.printStackTrace();
        }
        return source;
    }

    /**
     * Uses a datasource object to populate the list.
     * @param theDataSource the datasource object.
     */
    private void loadListWithResults(final SQLiteDataSource theDataSource) {
        try (Connection connection = theDataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM Questions")) {
            while (result.next()) {
                myQuestionList.add(Question.createQuestion(result.getString("Question"),
                        result.getString("Truth"), result.getString("Herring1"),
                        result.getString("Herring2"), result.getString("Herring3")));
            }
        } catch (final SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Question getQuestion() {
        Collections.shuffle(myQuestionList);
        return myQuestionList.get(0);
    }
}
